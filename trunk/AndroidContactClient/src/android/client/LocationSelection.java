package android.client;


import java.io.IOException;
import java.util.Iterator;
import java.util.List;


import org.xml.sax.SAXException;
import com.google.android.maps.Point;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
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
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayController;
import com.google.android.maps.Overlay.PixelCalculator;

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
	protected OverlayController myOverlayController = null; 

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
		
		myOverlayController = myMap.createOverlayController();
        MyLocationOverlay myLocationOverlay = new MyLocationOverlay();
        myOverlayController.add(myLocationOverlay, true); 
		
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
	
	protected class MyLocationOverlay extends Overlay {
        @Override
        public void draw(Canvas canvas, PixelCalculator calculator, boolean shadow) {
          super.draw(canvas, calculator, shadow);
          
          // Setup our "brush"/"pencil"/ whatever...
          Paint paint = new Paint();
          paint.setTextSize(14);
         
          LocationManager myLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
          
          Location l = myLocationManager.getCurrentLocation("gps");
          
          // Create a Point that represents our GPS-Location
          Double lat = l.getLatitude() * 1E6;
          Double lng = l.getLongitude() * 1E6;
          Point point = new Point(lat.intValue(), lng.intValue());
         
          int[] myScreenCoords = new int[2];
          // Converts lat/lng-Point to OUR coordinates on the screen.
          calculator.getPointXY(point, myScreenCoords);
          
          // Draw a circle for our location
          RectF oval = new RectF(myScreenCoords[0] - 7, myScreenCoords[1] + 7,
                                   myScreenCoords[0] + 7, myScreenCoords[1] - 7);
          
          // Setup a color for our location
          paint.setStyle(Style.FILL);
          paint.setARGB(255, 80, 150, 30); // Nice strong Android-Green    
          // Draw our name
          canvas.drawText(getString(R.string.map_overlay_own_name),
                                   myScreenCoords[0] +9, myScreenCoords[1], paint);
          
          // Change the paint to a 'Lookthrough' Android-Green
          paint.setARGB(80, 156, 192, 36);
          paint.setStrokeWidth(1);
          // draw an oval around our location
          canvas.drawOval(oval, paint);
          
           // With a black stroke around the oval we drew before.
          paint.setARGB(255,0,0,0);
          paint.setStyle(Style.STROKE);
          canvas.drawCircle(myScreenCoords[0], myScreenCoords[1], 7, paint);
          
          //Aggiungo le mie posizioni
          
          List<DBHelper.Location> ll = db.fetchAllRows();
          Iterator<DBHelper.Location> i = ll.iterator();
          
          while(i.hasNext()) {
        	  
        	  // Setup our "brush"/"pencil"/ whatever...
              Paint paint1 = new Paint();
              paint.setTextSize(14);
             
              DBHelper.Location loc = i.next();
              
              // Create a Point that represents our GPS-Location
              Double lat1 = Double.parseDouble(String.valueOf(loc.latitude));
              Double lng1 = Double.parseDouble(String.valueOf(loc.longitude));
              Point point1 = new Point(lat1.intValue(), lng1.intValue());
             
              int[] myScreenCoords1 = new int[2];
              // Converts lat/lng-Point to OUR coordinates on the screen.
              calculator.getPointXY(point1, myScreenCoords);
              
              // Draw a circle for our location
              RectF oval1 = new RectF(myScreenCoords[0] - 7, myScreenCoords[1] + 7,
                                       myScreenCoords[0] + 7, myScreenCoords[1] - 7);
              
              // Setup a color for our location
              paint1.setStyle(Style.FILL);
              paint1.setARGB(255, 90, 150, 100); // Nice strong Android-Green    
              // Draw our name
              canvas.drawText(loc.preferred,
                                       myScreenCoords[0] +9, myScreenCoords[1], paint1);
              
              // Change the paint to a 'Lookthrough' Android-Green
              paint1.setARGB(80, 56, 192, 106);
              paint1.setStrokeWidth(1);
              // draw an oval around our location
              canvas.drawOval(oval1, paint1);
              
               // With a black stroke around the oval we drew before.
              paint1.setARGB(255,0,0,0);
              paint1.setStyle(Style.STROKE);
              canvas.drawCircle(myScreenCoords[0], myScreenCoords[1], 7, paint1);
        	  
          }
          
          /*
          int[] friendScreenCoords = new int[2];
          //Draw each friend with a line pointing to our own location.
          for(Friend aFriend : FriendFinderMap.this.nearFriends){
               lat = aFriend.itsLocation.getLatitude() * 1E6;
               lng = aFriend.itsLocation.getLongitude() * 1E6;
               point = new Point(lat.intValue(), lng.intValue());

               // Converts lat/lng-Point to coordinates on the screen.
               calculator.getPointXY(point, friendScreenCoords);
               if(Math.abs(friendScreenCoords[0]) < 2000 && Math.abs(friendScreenCoords[1]) < 2000){
                    // Draw a circle for this friend and his name
                    oval = new RectF(friendScreenCoords[0] - 7, friendScreenCoords[1] + 7,
                                        friendScreenCoords[0] + 7, friendScreenCoords[1] - 7);
                    
                    // Setup a color for all friends
                    paint.setStyle(Style.FILL);
                    paint.setARGB(255, 255, 0, 0); // Nice red             
                    canvas.drawText(aFriend.itsName, friendScreenCoords[0] +9,
                                             friendScreenCoords[1], paint);
                    
                    // Draw a line connecting us to the current Friend
                    paint.setARGB(80, 255, 0, 0); // Nice red, more look through...

                    paint.setStrokeWidth(2);
                    canvas.drawLine(myScreenCoords[0], myScreenCoords[1],
                                        friendScreenCoords[0], friendScreenCoords[1], paint);
                    paint.setStrokeWidth(1);
                    // draw an oval around our friends location
                    canvas.drawOval(oval, paint);
                    
                     // With a black stroke around the oval we drew before.
                    paint.setARGB(255,0,0,0);
                    paint.setStyle(Style.STROKE);
                    canvas.drawCircle(friendScreenCoords[0], friendScreenCoords[1], 7, paint);
               }*/
          } 
	}
}
