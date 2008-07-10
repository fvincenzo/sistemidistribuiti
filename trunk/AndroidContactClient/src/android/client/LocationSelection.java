package android.client;


import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.Menu.Item;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

public class LocationSelection extends MapActivity implements OnClickListener{

	public static final String SELECT_LOCATION_ACTION =
		"android.client.action.SELECT_LOCATION";

	private final int SEARCH = Menu.FIRST;
	private final int CHOOSE = Menu.FIRST+1;

	private MapView myMap = null;
	private Button select;
	private LinearLayout searchBar;
	private EditText search;
	private Button go;

	@Override
	protected void onCreate(Bundle icicle) {
		// TODO Auto-generated method stub
		super.onCreate(icicle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.location_layout);
		myMap = (MapView)findViewById(R.id.map);
		select = (Button)findViewById(R.id.map_select);
		searchBar = (LinearLayout)findViewById(R.id.search_bar);
		go = (Button)findViewById(R.id.go_search);
		search = (EditText)findViewById(R.id.search_text);

		go.setOnClickListener(this);
		select.setOnClickListener(this);
	}

	public void onClick(View arg0) {

		if (arg0 == go){
			String text = search.getText().toString();
			if (text.length() > 0){
				gotoAddress(text);
				searchBar.setVisibility(View.INVISIBLE);
			}
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, SEARCH, "Search", R.drawable.search_icon);
		menu.add(0, CHOOSE, "Choose here", R.drawable.here_icon);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(Item item) {
		switch (item.getId()) {
		case SEARCH:
			searchBar.setVisibility(View.VISIBLE);
			break;
		case CHOOSE:
			break;

		}
		return super.onOptionsItemSelected(item);
	}

	private void gotoAddress(String address){

	}

}
