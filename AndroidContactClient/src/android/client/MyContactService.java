package android.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.IBinder;
import android.provider.Contacts;
import android.util.Log;
import android.widget.Toast;
import android.client.R;

public class MyContactService extends Service implements ServiceConnection{
	
	private PositionService posService;
	private Notification not;
	private FriendsService friendService;
	private NotificationManager mNM;
//	private FriendThread ft = null;
	
	/*@Override
	protected void onStart(int arg1, Bundle arguments){
		bindService(new Intent(MyContactService.this, PositionService.class), new ServiceConnection(){

			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				posService = ((PositionService.LocalBinder)service).getService();
				Toast.makeText(MyContactService.this, "PositionService bound", Toast.LENGTH_SHORT).show();
				
			}

			@Override
			public void onServiceDisconnected(ComponentName arg0) {
				// TODO Auto-generated method stub
				
			}
    		
    	}, BIND_AUTO_CREATE);
    	
    	bindService(new Intent(MyContactService.this, FriendsService.class), new ServiceConnection(){

			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				friendService = ((FriendsService.LocalBinder)service).getService();
				Toast.makeText(MyContactService.this, "FriendsService bound", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onServiceDisconnected(ComponentName arg0) {
				// TODO Auto-generated method stub
				
			}
    		
    	}, BIND_AUTO_CREATE);
    	
	}*/
	
	
	
	
    private ServiceConnection friendConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			friendService = ((FriendsService.LocalBinder)service).getService();
			Log.v("MyContactService", "FriendConnection stabilita");
			
		}

		public void onServiceDisconnected(ComponentName className) {
			friendService = null;
			Toast.makeText(MyContactService.this, "Servizio morto",
					Toast.LENGTH_SHORT).show();
		}
	};

	private final ServiceInterface.Stub mBinder = new ServiceInterface.Stub() {
		
		private PrintWriter out;
		private BufferedReader in;
		private Socket socket;
		private String username = "";
		private FriendThread ft = new FriendThread(this);

		@Override
		public boolean register(String uname, String pwd, String mobile,
				String home, String work, String mail, String im) {
			try {
				in.readLine();
				in.readLine();
				in.readLine();
				out.println("REGISTER");
				out.println(uname);
				out.println(pwd);
				if (mobile == null)
					mobile = " ";
				out.println(mobile);
				if (home == null)
					home = " ";
				out.println(home);
				if (work == null)
					work = " ";
				out.println(work);
				if (mail == null)
					mail = " ";
				out.println(mail);
				if (im == null)
					im = " ";
				out.println(im);
				out.println("END");
				String ret;
				ret = in.readLine();
				if (ret.contains("OK")) {
					this.username = uname;
					if (!ft.isAlive()){
						ft.start();
						Log.v("MyContactService" , "Thread di ascolto degli amici fatto partire");
						
					return true;
				}
				}
				return false;
			} catch (IOException e) {
				return false;
			}
		}

		@Override
		public boolean login(String uname, String pwd) {
			try {
				in.readLine();
				in.readLine();
				in.readLine();
				out.println("LOGIN");
				out.println(uname);
				out.println(pwd);
				out.println("END");
				String ret;
				ret = in.readLine();
				if (ret.contains("OK")) {
					this.username = uname;
					if (!ft.isAlive()){
						ft.start();
						Log.v("MyContactService" , "Thread di ascolto degli amici fatto partire");
						return true;
					}
				}
				return false;
			} catch (IOException e) {
				return false;
			}
		}

		@Override
		public boolean changepersonal(String username, String mobile,
				String home, String work, String mail, String im) {
			try {
				out.println("CHANGEPERSONAL");
				out.println(username);
				out.println(mobile);
				out.println(home);
				out.println(work);
				out.println(mail);
				out.println(im);
				out.println("END");
				String ret;
				ret = in.readLine();
				if (ret.contains("OK")) {
					return true;
				}
				return false;
			} catch (IOException e) {
				return false;
			}
		}

		@Override
		public boolean setpreferred(String username, String mode) {
			try {
				out.println("SETPREFERRED");
				out.println(username);
				out.println(mode);
				out.println("END");
				String ret;
				ret = in.readLine();
				if (ret.contains("OK")) {
					return true;
				}
				return false;
			} catch (IOException e) {
				return false;
			}
		}

		@Override
		public String getpreferred(String username) {
			try {
				out.println("GETPREFERRED");
				out.println(username);
				out.println("END");
				String ret;
				ret = in.readLine();
				return ret;
			} catch (IOException e) {
				return null;
			}
		}

		@Override
		public boolean addFriend(String friendName) {
			try {
				if (username != "") {
					out.println("ADDFRIEND");
					out.println(username);
					out.println(friendName);
					out.println("END");
					String tmp = in.readLine();
					if (tmp.contains("OK")) {
						ft.addFriendRequest(friendName);
						return true;
					}
				}
				return false;
			} catch (Exception e) {
				return false;
			}
		}

		@Override
		public void denyFriend(String friendName) {
			try {
				if (username != "") {
					out.println("DENYFRIEND");
					out.println(username);
					out.println(friendName);
					out.println("END");
					in.readLine();
				}
			} catch (Exception e) {
			}
		}

		@Override
		public void acceptFriend(String friendName) {
			try {
				if (username != "") {
					out.println("ACCEPTFRIEND");
					out.println(username);
					out.println(friendName);
					out.println("END");
					in.readLine();
				}
			} catch (Exception e) {
			}
		}

		@Override
		public List<String> getFriends() {
			List<String> retV = new LinkedList<String>();
			if (username != "") {
				out.println("GETFRIENDS");
				out.println(username);
				out.println("END");
				String ret;
				try {
					ret = in.readLine();
					StringTokenizer tok = new StringTokenizer(ret, "$");
					while (tok.hasMoreTokens()) {
						retV.add(tok.nextToken());
					}
					return retV;
				} catch (IOException e) {
					return retV;
				}
			}
			return retV;
		}

		@Override
		public List<String> getUserDetails(String friend) {
			try {
				if (username != "") {
					out.println("GETUSERDATA");
					out.println(username);
					out.println(friend);
					out.println("END");
					String ret;
					ret = in.readLine();
					if (!ret.contains("ERROR")) {
						return new ContactUser(ret).toList();
					}
				}
				return null;
			} catch (Exception e) {
				return null;
			}
		}

		@Override
		public List<String> getUsers() {
			List<String> retV = new LinkedList<String>();
			if (username != "") {
				out.println("GETUSERS");
				out.println("END");
				String ret;
				try {
					ret = in.readLine();
					StringTokenizer tok = new StringTokenizer(ret, "$");
					while (tok.hasMoreTokens()) {
						String toAdd = tok.nextToken();
						if (!toAdd.equals(username))
							retV.add(toAdd);
					}
					return retV;
				} catch (IOException e) {
					return retV;
				}
			}
			return retV;
		}

		@Override
		public List<String> pendingFriends() {
			List<String> retV = new LinkedList<String>();
			if (username != "") {
				out.println("PENDINGFRIENDS");
				out.println(username);
				out.println("END");
				String ret;
				try {
					ret = in.readLine();
					StringTokenizer tok = new StringTokenizer(ret, "$");
					while (tok.hasMoreTokens()) {
						retV.add(tok.nextToken());
					}
					return retV;
				} catch (IOException e) {
					return retV;
				}
			}
			return retV;
		}

		@Override
		public boolean updatePosition(double x_position, double y_position) {
			if (username != "") {
				out.println("UPDATEPOSITION");
				out.println(username);
				out.println("@" + x_position + "," + y_position);
				out.println("END");
				String ret;
				try {
					ret = in.readLine();
					if (ret.contains("OK")) {
						return true;
					}
					return false;
				} catch (IOException e) {
					return false;
				}
			}
			return false;
		}

		@Override
		public boolean connect(String server) {
			try {
				socket = new Socket(server, 4444);
				out = new PrintWriter(socket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(socket
						.getInputStream()));
				return true;
			} catch (Exception e) {
				return false;
			}
		}

		@Override
		public boolean isRunning() {
			if (!username.equals("") && socket.isConnected())
				return true;
			return false;
		}

		@Override
		public boolean insertContact(String friend) throws DeadObjectException {
			List<String> info = getUserDetails(friend);
			ContentValues person = new ContentValues();
			person.put(Contacts.People.NAME, info.get(0));
			Uri newPerson = getContentResolver().insert(
					Contacts.People.CONTENT_URI, person);
			if (newPerson != null) {
				List<String> pathList = newPerson.getPathSegments();
				String pathLeaf = pathList.get(pathList.size() - 1);
				ContentValues number = new ContentValues();
				number.put(Contacts.Phones.PERSON_ID, pathLeaf);
				number.put(Contacts.Phones.NUMBER, info.get(1));
				number.put(Contacts.Phones.TYPE, Contacts.Phones.MOBILE_TYPE);
				Uri phoneUpdate = getContentResolver().insert(
						Contacts.Phones.CONTENT_URI, number);
				if (phoneUpdate == null) {
					return false;
				}
				number = new ContentValues();
				number.put(Contacts.Phones.PERSON_ID, pathLeaf);
				number.put(Contacts.Phones.NUMBER, info.get(3));
				number.put(Contacts.Phones.TYPE, Contacts.Phones.WORK_TYPE);
				phoneUpdate = getContentResolver().insert(
						Contacts.Phones.CONTENT_URI, number);
				if (phoneUpdate == null) {
					return false;
				}
				number = new ContentValues();
				number.put(Contacts.Phones.PERSON_ID, pathLeaf);
				number.put(Contacts.Phones.NUMBER, info.get(2));
				number.put(Contacts.Phones.TYPE, Contacts.Phones.HOME_TYPE);
				phoneUpdate = getContentResolver().insert(
						Contacts.Phones.CONTENT_URI, number);
				if (phoneUpdate == null) {
					return false;
				}
				ContentValues email = new ContentValues();
				email.put(Contacts.ContactMethods.PERSON_ID, pathLeaf);
				email.put(Contacts.ContactMethods.KIND,
						Contacts.ContactMethods.EMAIL_KIND);
				email.put(Contacts.ContactMethods.DATA, info.get(4));
				email.put(Contacts.ContactMethods.TYPE,
						Contacts.ContactMethods.EMAIL_KIND_OTHER_TYPE);
				email.put(Contacts.ContactMethods.LABEL, "Mail");
				Uri emailUpdate = getContentResolver().insert(
						Uri.withAppendedPath(newPerson,
								Contacts.ContactMethods.CONTENT_URI.getPath()
										.substring(1)), email);
				if (emailUpdate == null) {
					return false;
				}
				ContentValues im = new ContentValues();
				im.put(Contacts.ContactMethods.PERSON_ID, pathLeaf);
				im.put(Contacts.ContactMethods.KIND,
						Contacts.ContactMethods.EMAIL_KIND);
				im.put(Contacts.ContactMethods.DATA, info.get(5));
				im.put(Contacts.ContactMethods.TYPE,
						Contacts.ContactMethods.EMAIL_KIND_OTHER_TYPE);
				im.put(Contacts.ContactMethods.LABEL, "Messenger");
				Uri imUpdate = getContentResolver().insert(
						Uri.withAppendedPath(newPerson,
								Contacts.ContactMethods.CONTENT_URI.getPath()
										.substring(1)), im);
				if (imUpdate == null) {
					return false;
				}
				ContentValues geo = new ContentValues();
				geo.put(Contacts.ContactMethods.PERSON_ID, pathLeaf);
				geo.put(Contacts.ContactMethods.KIND,
						Contacts.ContactMethods.POSTAL_KIND);
				geo.put(Contacts.ContactMethods.DATA, info.get(6));
				geo.put(Contacts.ContactMethods.TYPE,
						Contacts.ContactMethods.POSTAL_KIND_OTHER_TYPE);
				geo.put(Contacts.ContactMethods.LABEL, "Current Position");
				Uri geoUpdate = getContentResolver().insert(
						Uri.withAppendedPath(newPerson,
								Contacts.ContactMethods.CONTENT_URI.getPath()
										.substring(1)), geo);
				if (geoUpdate == null) {
					return false;
				}
			}
			return true;
		}

		@Override
		public boolean modifyContact(String friend) throws DeadObjectException {
			return false;
		}
	};

	@Override
    protected void onCreate() {
    	
    	
    	
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        
        // Display a notification about us starting.  We put an icon in the status bar.
        showNotification();
//        Log.v("MyContactService", "Ho appena notificato");
//        bindService(new Intent("android.client.FRIEND_SERVICE"), friendConnection, BIND_AUTO_CREATE);
//        Log.v("MyContactService", "Eseguita la bindService del servizio locale");
	}

    @Override
    protected void onDestroy() {
        // Cancel the persistent notification.
        mNM.cancel(R.string.local_service_started);
        

        // Tell the user we stopped.
        Toast.makeText(this, R.string.local_service_stopped, Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		posService = ((PositionService.LocalBinder)service).getService();
		Toast.makeText(this, "PositionService bound", Toast.LENGTH_SHORT).show();
		
	}

	@Override
	public void onServiceDisconnected(ComponentName arg0) {
		// TODO Auto-generated method stub
		
	}
    /**
     * Show a notification while this service is running.
     */
    private void showNotification() {
        // This is who should be launched if the user selects our notification.
        Intent contentIntent = new Intent(this, MyContatsClient.class);

        // This is who should be launched if the user selects the app icon in the notification,
        // (in this case, we launch the same activity for both)
        Intent appIntent = new Intent(this, MyContatsClient.class);

        // In this sample, we'll use the same text for the ticker and the expanded notification
        CharSequence text = getText(R.string.local_service_started);

        // we use a string id because it is a unique
                                                    // number.  we use it later to cancel the
                                                    // notification
                   not = new Notification(
                       this,                        // our context
                       R.drawable.small_icon_inactive,      // the icon for the status bar
                       text,                        // the text to display in the ticker
                       System.currentTimeMillis(),  // the timestamp for the notification
                       getText(R.string.local_service_notification), // the title for the notification
                       text,                        // the details to display in the notification
                       contentIntent,               // the contentIntent (see above)
                       R.drawable.icon,  // the app icon
                       getText(R.string.app_name), // the name of the app
                       appIntent);                 // the appIntent (see above)

                   mNM.notify(R.string.local_service_started, not);
    }

}
