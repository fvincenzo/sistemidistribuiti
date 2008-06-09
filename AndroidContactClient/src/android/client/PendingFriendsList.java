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



import java.util.Vector;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;



/**
 * A list view example where the 
 * data for the list comes from an array of strings.
 */
public class PendingFriendsList extends ListActivity implements OnClickListener {
	

	public static final String PENDING_ACTION =
		"android.client.action.PENDING";
	
	private ContactClientInterface contactList = ContactClient.getHistance();
	private ArrayAdapter<String> arrAd;
	private String selected;
	private Vector<String> v = new Vector<String>();

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Use an existing ListAdapter that will map an array
        // of strings to TextViews
        for(String s: contactList.pendingFriends()){
        	v.add(s);
        }
        if (v.size() > 0){
        arrAd = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, v.toArray(new String[0]));
        setListAdapter(arrAd);

        }
        else {
        	startActivity(new Intent(MainLoopActivity.MAIN_LOOP_ACTION, null));
        	finish();
        }
    }
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id){
//    	if (l.getSelectedItem()!= null){
    	
//    	String selezionato 	= l.getSelectedItem().toString();
    		setSelection(position);
    		
    		selected = this.v.get(getSelectedItemPosition()) ;
//    		AlertDialog.show(this, "Titolo", 0,mStrings[getSelectedItemPosition()] , "ACCEPT" ,this, "DENY",this, false, null);
    		AlertDialog.show(this, "Accept or Deny?", 0, "What do you want to do with:\n"+selected, "ACCEPT",this,"DENY", this, true, null);
//    	}
    }

    @Override
    public void onClick(DialogInterface dialog, int which){
    	if (which == DialogInterface.BUTTON1){
    		contactList.acceptFriend(selected);
    		removeEntry(selected);
    	}
    	if (which == DialogInterface.BUTTON2){
    		contactList.denyFriend(selected);
    		removeEntry(selected);
    	}
    }
    
    public void removeEntry(String toRemove){
    	v.remove(toRemove);
    	if (v.size() > 0){
        arrAd = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, v.toArray(new String[0]));
        setListAdapter(arrAd);
    	}
    	else {
        	startActivity(new Intent(MainLoopActivity.MAIN_LOOP_ACTION, null));
        	finish();
    	}
    }
   
}