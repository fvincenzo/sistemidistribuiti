package android.client;
/* 
 * Copyright (C) 2007 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



import java.util.List;
import java.util.Vector;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.IBinder;
import android.provider.Contacts;
import android.provider.Contacts.People;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;



/**
 * A list view example where the 
 * data for the list comes from an array of strings.
 */
public class FriendsList extends ListActivity implements OnClickListener, ServiceConnection {


	//TODO Inserire la gestione della lista degli utenti
	private ServiceInterface s;

	public static final String PENDING_ACTION =
		"android.client.action.PENDING";

	public static final String ALL_USERS_ACTION =
		"android.client.action.ALL_USERS";

	private static final int PENDING = 0;
	private static final int ALL_USERS = 1;

	private int mState;

	private ArrayAdapter<String> arrAd;
	private String selected;
	private Vector<String> v = new Vector<String>();

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		final Intent intent = getIntent();
		final String action = intent.getAction();
		if (action.equals(PENDING_ACTION)) {
			mState = PENDING;
			setTitle(R.string.title_pending_friends);
			// Use an existing ListAdapter that will map an array
			// of strings to TextViews

		}
		if (action.equals(ALL_USERS_ACTION)) {
			mState = ALL_USERS;
			setTitle(R.string.all_users);
		}
		bindService(new Intent("android.client.MY_SERVICE"),this,0);

	}
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id){
		if (mState == PENDING){
			//    	if (l.getSelectedItem()!= null){

//			String selezionato 	= l.getSelectedItem().toString();
			setSelection(position);

			selected = this.v.get(getSelectedItemPosition()) ;
//			AlertDialog.show(this, "Titolo", 0,mStrings[getSelectedItemPosition()] , "ACCEPT" ,this, "DENY",this, false, null);
			AlertDialog.show(this, "Accept or Deny?", 0, "What do you want to do with:\n"+selected, "ACCEPT",this,"DENY", this, true, null);
//			}
		}
		if (mState == ALL_USERS) {

			setSelection(position);

			selected = this.v.get(getSelectedItemPosition()) ;
			AlertDialog.show(this, "Add a friend?", 0, "Do you want "+selected+" in your friends list?", "YES",this,"NO", this, true, null);

		}
	}


	@Override
	public void onClick(DialogInterface dialog, int which){
		if (mState == PENDING){
			if (which == DialogInterface.BUTTON1){
				try {
					s.acceptFriend(selected);
					if (s.insertContact(selected)){
						removeEntry(selected);						
					}
					else {
						//TODO controllare e segnalare l'errore;
					}
				} catch (DeadObjectException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (which == DialogInterface.BUTTON2){
				try {
					s.denyFriend(selected);
					removeEntry(selected);
				} catch (DeadObjectException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		if (mState == ALL_USERS) {
			if (which == DialogInterface.BUTTON1){
				try {
					if (s.addFriend(selected)){
						AlertDialog.show(this, "OK", 0, selected+" received a request to become your friend", "OK",false);
						removeEntry(selected);
					}
					else {
						AlertDialog.show(this, "ERROR", 0, "Unable to perform the request", "OK",false);
					}
				} catch (DeadObjectException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}

	public void removeEntry(String toRemove){
//		if (mState == PENDING){
		v.remove(toRemove);
		if (v.size() > 0){
			arrAd = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, v.toArray(new String[0]));
			setListAdapter(arrAd);
		}
		else {
			startActivity(new Intent(MainLoopActivity.MAIN_LOOP_ACTION, getIntent().getData()));
			finish();
		}
//		}
//		if (mState == ALL_USERS) {

//		}
	}

	/**
	 * Inserisce i dati di un utente in rubrica
	 * 
	 * @param info dati dell'utente sotto forma di lista così come viene ritornata 
	 * da getUserdetails
	 * 
	 * @return
	 */
	/*private boolean addContact(List<String> info){
		ContentValues person = new ContentValues();
		person.put(Contacts.People.NAME, info.get(0));
//		person.put(Contacts.People.COMPANY, "Test Company");
//		person.put(Contacts.People.NOTES,"note");

		Uri newPerson = getContentResolver().insert(
				Contacts.People.CONTENT_URI, person);

		if (newPerson != null) {
			List<String> pathList = newPerson.getPathSegments();
			String pathLeaf = pathList.get(pathList.size() - 1);


			// add mobile phone number

			ContentValues number = new ContentValues();
			number.put(Contacts.Phones.PERSON_ID, pathLeaf);
			number.put(Contacts.Phones.NUMBER, info.get(1));
			number.put(Contacts.Phones.TYPE, Contacts.Phones.MOBILE_TYPE);
			Uri phoneUpdate = getContentResolver().insert(
					Contacts.Phones.CONTENT_URI, number);
			if (phoneUpdate == null) {
//				throw new Exception("Failed to insert mobile phone number");
				return false;
			}


			// add work number ok

			number = new ContentValues();
			number.put(Contacts.Phones.PERSON_ID, pathLeaf);
			number.put(Contacts.Phones.NUMBER, info.get(3));
			number.put(Contacts.Phones.TYPE, Contacts.Phones.WORK_TYPE);
			phoneUpdate = getContentResolver().insert(
					Contacts.Phones.CONTENT_URI, number);
			if (phoneUpdate == null) {
//				throw new Exception("Failed to insert work number");
				return false;
			}

			//ADD home number
			number = new ContentValues();
			number.put(Contacts.Phones.PERSON_ID, pathLeaf);
			number.put(Contacts.Phones.NUMBER, info.get(2));
			number.put(Contacts.Phones.TYPE, Contacts.Phones.HOME_TYPE);
			phoneUpdate = getContentResolver().insert(
					Contacts.Phones.CONTENT_URI, number);
			if (phoneUpdate == null) {
//				throw new Exception("Failed to insert work number");
				return false;
			}


			// add email ok

			ContentValues email = new ContentValues();
			email.put(Contacts.ContactMethods.PERSON_ID, pathLeaf);
			email.put(Contacts.ContactMethods.KIND,
					Contacts.ContactMethods.EMAIL_KIND);
			email.put(Contacts.ContactMethods.DATA,
			info.get(4));
			email.put(Contacts.ContactMethods.TYPE, Contacts.ContactMethods.EMAIL_KIND_OTHER_TYPE);
			email.put(Contacts.ContactMethods.LABEL, "Mail");
			Uri emailUpdate = getContentResolver()
			.insert(
					Uri.withAppendedPath(newPerson,
							Contacts.ContactMethods.CONTENT_URI.getPath()
							.substring(1)), email);
			if (emailUpdate == null) {
//				throw new Exception("Failed to insert primary email");
				return false;
			}




			// add im contact ok

			ContentValues im = new ContentValues();
			im.put(Contacts.ContactMethods.PERSON_ID, pathLeaf);
			im.put(Contacts.ContactMethods.KIND,
					Contacts.ContactMethods.EMAIL_KIND);
			im.put(Contacts.ContactMethods.DATA,
			info.get(5));
			im.put(Contacts.ContactMethods.TYPE, Contacts.ContactMethods.EMAIL_KIND_OTHER_TYPE);
			im.put(Contacts.ContactMethods.LABEL, "Messenger");
			Uri imUpdate = getContentResolver()
			.insert(
					Uri.withAppendedPath(newPerson,
							Contacts.ContactMethods.CONTENT_URI.getPath()
							.substring(1)), im);
			if (imUpdate == null) {
//				throw new Exception("Failed to insert primary email");
				return false;
			}



			ContentValues geo = new ContentValues();
			geo.put(Contacts.ContactMethods.PERSON_ID, pathLeaf);
			geo.put(Contacts.ContactMethods.KIND,
					Contacts.ContactMethods.POSTAL_KIND);
			geo.put(Contacts.ContactMethods.DATA,
			info.get(6));
			geo.put(Contacts.ContactMethods.TYPE, Contacts.ContactMethods.POSTAL_KIND_OTHER_TYPE);
			geo.put(Contacts.ContactMethods.LABEL, "Current Position");
			Uri geoUpdate = getContentResolver()
			.insert(
					Uri.withAppendedPath(newPerson,
							Contacts.ContactMethods.CONTENT_URI.getPath()
							.substring(1)), geo);
			if (geoUpdate == null) {
//				throw new Exception("Failed to insert primary email");
				return false;
			}
		}
		return true;

	}
*/


	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		s = ServiceInterface.Stub.asInterface(service);
		if (mState == PENDING){
			try {
				for(String str: s.pendingFriends()){
					v.add(str);
				}
			} catch (DeadObjectException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (mState == ALL_USERS){
			try {
				List<String> friends = s.getFriends();
				for(String str: s.getUsers()){
					if (!friends.contains(str)) v.add(str);
				}
			} catch (DeadObjectException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (v.size() > 0){
			arrAd = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, v.toArray(new String[0]));
			setListAdapter(arrAd);

		}
		else {
			startActivity(new Intent(MainLoopActivity.MAIN_LOOP_ACTION, getIntent().getData()));
			finish();
		}
	}


	@Override
	public void onServiceDisconnected(ComponentName arg0) {
		// TODO Auto-generated method stub

	}

}