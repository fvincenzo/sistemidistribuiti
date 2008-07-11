package android.client;


import java.io.IOException;


import org.xml.sax.SAXException;
import com.google.android.maps.Point;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Xml;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.Menu.Item;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class LocationSelection extends MapActivity implements OnClickListener{

	public static final String SELECT_LOCATION_ACTION =
		"android.client.action.SELECT_LOCATION";

	private final int SEARCH = Menu.FIRST;
	private final int CHOOSE = Menu.FIRST+1;
	private final int VIEW_LOCATIONS = Menu.FIRST+2;
	private final int CHOOSE_DEFAULT = Menu.FIRST+3;
	
	private MapView myMap = null;
	private Button select;
	private LinearLayout searchBar;
	private EditText search;
	private Button go;
	private Point lastSearch;

	private DBHelper db;

	private String lastAddress;
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
		db = new DBHelper(this);
	}

	public void onClick(View arg0) {

		if (arg0 == go){
			String text = search.getText().toString();
			if (text.length() > 0){
				gotoAddress(text);
				searchBar.setVisibility(View.INVISIBLE);
			}
		}
		if (arg0 == select){
			selectCurrentPosition();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, SEARCH, "Search", R.drawable.search_icon);
		menu.add(0, CHOOSE, "Choose here", R.drawable.here_icon);
		menu.add(0, VIEW_LOCATIONS, "View locations", android.R.drawable.icon_highlight_square);
		menu.add(0, CHOOSE_DEFAULT, "Set Default", android.R.drawable.arrow_down_float);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(Item item) {
		switch (item.getId()) {
		case SEARCH:
			searchBar.setVisibility(View.VISIBLE);
			break;
		case CHOOSE:
			selectCurrentPosition();
			break;
		case CHOOSE_DEFAULT:
			selectDefaultPosition();
			break;
		case VIEW_LOCATIONS:
			startActivity(new Intent(ViewLocations.VIEW_LOCATIONS_ACTION, getIntent().getData()));
			break;

		}
		return super.onOptionsItemSelected(item);
	}
	private void selectCurrentPosition(){
		Point p = myMap.getMapCenter();
		new LocationDialog(this, p, false).show();
	}
	
	private void selectDefaultPosition(){
		Point p = myMap.getMapCenter();
		new LocationDialog(this, p, true).show();
	}
	

	private void gotoAddress(String address){
		try {
			String xmlCode = YahooGeoAPI.getGeoCode(address);
			YahooGeocodeHandler handler = new YahooGeocodeHandler();
			Xml.parse(xmlCode, handler);
			Point p = new Point((int)handler.getLatitudeAsLong(), (int)handler.getLongitudeAsLong());
			MapController mc = myMap.getController();
			lastSearch = p;
			lastAddress = address;
			mc.animateTo(p);
			mc.zoomTo(21);
		} catch (IOException e){

		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	class LocationDialog extends Dialog implements OnClickListener{


		private Point p;
		private Button setHome;
		private Button setMobile;
		private Button setWork;
		private Button setMail;
		private Button setIM;
		private boolean defaultLocation;
		private Geocoder geoCoder;



		public LocationDialog(Context context, Point p, boolean defaultLocation) {
			super(context);
			this.p=p;
			this.defaultLocation = defaultLocation;
			this.setTitle("Current position is:");
			setContentView(R.layout.choose_dialog);
			geoCoder = new Geocoder();
			setMobile = (Button)findViewById(R.id.set_mobile);
			setHome = (Button)findViewById(R.id.set_home);
			setWork = (Button)findViewById(R.id.set_work);
			setMail = (Button)findViewById(R.id.set_email);
			setIM = (Button)findViewById(R.id.set_im);

			setMobile.setOnClickListener(this);
			setHome.setOnClickListener(this);
			setWork.setOnClickListener(this);
			setMail.setOnClickListener(this);
			setIM.setOnClickListener(this);

		}

		public void onClick(View arg0) {
			if (arg0 == setHome){
				setHome();
				this.dismiss();
			} else
				if (arg0 == setWork){
					setWork();
					this.dismiss();
				} else
					if (arg0 == setMobile){
						setMobile();
						this.dismiss();
					} else
						if (arg0 == setMail){
							setMail();
							this.dismiss();
						} else
							if (arg0 == setIM){
								setIm();
								this.dismiss();
							}

		}


		private void setHome(){
			if (defaultLocation){
				db.setDefaultLocation("HOME");
			}
			else {
			String res = "";
			try {
				Address[] addr = geoCoder.getFromLocation(p.getLatitudeE6(), p.getLongitudeE6());
				if (addr != null){
					if (addr.length > 0){
						res = addr[0].toString();
					}
				}
			} catch (IOException e) {
				
			}
			if (res.equals("")){
				if (this.p.equals(lastSearch)){
					res = lastAddress;
				}
			}
			
			
			db.addLocation(this.p, res, "HOME");
			}
		}
		private void setWork(){
			if (defaultLocation){
				db.setDefaultLocation("WORK");
			}
			else {
			String res = "";
			try {
				Address[] addr = geoCoder.getFromLocation(p.getLatitudeE6(), p.getLongitudeE6());
				if (addr != null){
					if (addr.length > 0){
						res = addr[0].toString();
					}
				}
			} catch (IOException e) {
				
			}
			if (res.equals("")){
				if (this.p.equals(lastSearch)){
					res = lastAddress;
				}
			}
			
			db.addLocation(this.p, res, "WORK");
		}
		}
		private void setMobile(){
			if (defaultLocation){
				db.setDefaultLocation("MOBILE");
			}
			else {
			String res = "";
			try {
				Address[] addr = geoCoder.getFromLocation(p.getLatitudeE6(), p.getLongitudeE6());
				if (addr != null){
					if (addr.length > 0){
						res = addr[0].toString();
					}
				}
			} catch (IOException e) {
				
			}
			if (res.equals("")){
				if (this.p.equals(lastSearch)){
					res = lastAddress;
				}
			}
			
			db.addLocation(this.p, res,  "MOBILE");
			}
		}
		private void setMail(){
			if (defaultLocation){
				db.setDefaultLocation("MAIL");
			}
			else {
			String res = "";
			try {
				Address[] addr = geoCoder.getFromLocation(p.getLatitudeE6(), p.getLongitudeE6());
				if (addr != null){
					if (addr.length > 0){
						res = addr[0].toString();
					}
				}
			} catch (IOException e) {
				
			}
			if (res.equals("")){
				if (this.p.equals(lastSearch)){
					res = lastAddress;
				}
			}
			
			db.addLocation(this.p, res, "MAIL");
		}
		}
		private void setIm(){
			if (defaultLocation){
				db.setDefaultLocation("IM");
			}
			else {
			String res = "";
			try {
				Address[] addr = geoCoder.getFromLocation(p.getLatitudeE6(), p.getLongitudeE6());
				if (addr != null){
					if (addr.length > 0){
						res = addr[0].toString();
					}
				}
			} catch (IOException e) {
				
			}
			
			if (res.equals("")){
				if (this.p.equals(lastSearch)){
					res = lastAddress;
				}
			}
			db.addLocation(this.p, res, "IM");
			}
		}
	}
}
