package android.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.ipc.Message;
import android.os.Binder;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.widget.Toast;
import android.client.R;

public class MyContactService extends Service  {
	
	
	private NotificationManager mNM;
	private FriendThread ft = null;

    @Override
    protected void onCreate() {
    	
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        
        // Display a notification about us starting.  We put an icon in the status bar.
        showNotification();
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

    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final ServiceInterface.Stub mBinder = new ServiceInterface.Stub(){

    	
    	private PrintWriter out;
    	private BufferedReader in;
    	private Socket socket;
    	private String username = "";
    	

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
    			if (mobile == null) mobile = " ";
    			out.println(mobile);
    			if (home == null) home = " ";
    			out.println(home);
    			if (work == null) work = " ";
    			out.println(work);
    			if (mail == null) mail = " ";
    			out.println(mail);
    			if (im == null) im = " ";
    			out.println(im);
//    			out.println("geo:"+x_position+","+y_position);
    			out.println("END");
    			String ret;
    			ret = in.readLine();
    			if (ret.contains("OK")){
    				this.username = uname;
    				return true;
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
    			if (ret.contains("OK")){
    				this.username = uname;
    				return true;
    			}
    			return false;
    		} catch (IOException e) {
    			return false;
    		}
    	}

    	@Override
    	public boolean addFriend(String friendName) {
    		
    		try{
    			if (username != ""){
    				out.println("ADDFRIEND");
    				out.println(username);
    				out.println(friendName);
    				out.println("END");
    				if (ft == null){
    					ft = new FriendThread(this);
    					ft.start();
    				}
    				ft.addFriendRequest(friendName);
    				return true;
    			}
    			return false;
    		}
    		catch (Exception e){
    			return false;
    		}
    	}

    	@Override
    	public void denyFriend(String friendName) {
    		try {
    			if (username != ""){
    				out.println("DENYFRIEND");
    				out.println(username);
    				out.println(friendName);
    				out.println("END");
    				in.readLine();
    				//TODO controllare il messaggio di ritorno
    			}	
    		} catch(Exception e){

    		}
    	}

    	@Override
    	public void acceptFriend( String friendName) {
    		try{
    			if (username != ""){
    				out.println("ACCEPTFRIEND");
    				out.println(username);
    				out.println(friendName);
    				out.println("END");
    				in.readLine();
    				//TODO controllare il messaggio di ritorno
    			}	
    		}catch (Exception e){

    		}
    	}

    	@Override
    	public List<String> getFriends() {
    		List<String> retV = new LinkedList<String>();
    		if (username != ""){
    			out.println("GETFRIENDS");
    			out.println(username);
    			out.println("END");
    			String ret;
    			try {
    				ret = in.readLine();
    				StringTokenizer tok =new StringTokenizer(ret, "$");
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
    			if (username != ""){
    				out.println("GETUSERDATA");
    				out.print(username);
    				out.println(friend);
    				out.println("END");
    				String ret;
    				ret = in.readLine();
    				return new ContactUser(ret).toList();
    			}
    			return null;
    		} catch (Exception e) {
    			return null;
    		}
    	}


    	@Override
    	public List<String> getUsers() {
    		List<String> retV = new LinkedList<String>();
    		if (username != ""){
    			out.println("GETUSERS");
    			out.println("END");
    			String ret;
    			try {
    				ret = in.readLine();
    				StringTokenizer tok =new StringTokenizer(ret, "$");
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
    		if (username != ""){
    			out.println("PENDINGFRIENDS");
    			out.println(username);
    			out.println("END");
    			String ret;
    			try {
    				ret = in.readLine();
    				StringTokenizer tok =new StringTokenizer(ret, "$");
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
    	public boolean updatePosition( double x_position, double y_position) {
    		if (username != ""){
    			out.println("UPDATEPOSITION");
    			out.println(username);
    			out.println("geo:"+x_position+","+y_position);
    			out.println("END");
    			String ret;
    			try {
    				ret = in.readLine();
    				if (ret.contains("OK")){
    					return true;
    				}
    				return false;
    			} catch (IOException e) {
    				return false;
    			}
    		}
    		return false;
    	}

    	public boolean connect(String server){
    		try {
    		socket = new Socket(server, 4444);
    		out = new PrintWriter(socket.getOutputStream(), true);
    		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    		return true;
    		}
    		catch (Exception e){
    			return false;
    		}
    	}
    	
    };

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

        mNM.notify(R.string.local_service_started,  // we use a string id because it is a unique
                                                    // number.  we use it later to cancel the
                                                    // notification
                   new Notification(
                       this,                        // our context
                       R.drawable.small_icon,      // the icon for the status bar
                       text,                        // the text to display in the ticker
                       System.currentTimeMillis(),  // the timestamp for the notification
                       getText(R.string.local_service_notification), // the title for the notification
                       text,                        // the details to display in the notification
                       contentIntent,               // the contentIntent (see above)
                       R.drawable.icon,  // the app icon
                       getText(R.string.app_name), // the name of the app
                       appIntent));                 // the appIntent (see above)
    }
}
