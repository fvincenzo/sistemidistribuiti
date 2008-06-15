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
					addContact(s.getUserDetails(selected));
					removeEntry(selected);
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
//
//		}
	}
	
	private boolean addContact(List<String> info){

		String[] projection = new String[] {
			    android.provider.Contacts.PeopleColumns.NAME,
			};

		Cursor cur =managedQuery(People.CONTENT_URI, projection, null, null, null);
		if (cur.count() > 0){
			return false;
		}
		ContentValues values = new ContentValues();
		values.put(Contacts.People.NAME, info.get(0));
		// add it to the database
		Uri newContact = getContentResolver().insert(Contacts.People.CONTENT_URI, values);
		if (newContact != null){
			return true;
		}

		
//		ret.add(username);
//		ret.add(mobile);
//		ret.add(home);
//		ret.add(work);
//		ret.add(mail);
//		ret.add(IM);
//		ret.add(position);
		
		return false;
	}
	
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
				for(String str: s.getUsers()){
						v.add(str);
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