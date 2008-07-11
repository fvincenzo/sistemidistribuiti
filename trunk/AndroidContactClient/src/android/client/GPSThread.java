package android.client;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.DeadObjectException;
import android.util.Log;


public class GPSThread extends Thread {

	private ServiceInterface.Stub servInt =  null;
	private boolean run = true;
	private long waitMinutes = 1;
	
	protected LocationManager myLocationManager = null;
    protected Location myLocation = null; 
	
    protected final long MINIMUM_DISTANCECHANGE_FOR_UPDATE = 25; // in Metri
    protected final long MINIMUM_TIME_BETWEEN_UPDATE = 2500; // in Millisecondi 

	public GPSThread(ServiceInterface.Stub s,LocationManager loc){
		super();
		servInt = s;
		
		//Setup Location Manager
		this.myLocationManager=loc;

	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		while (run){
			try {
				if(myLocationManager!=null) {
					
					//Prendo la mia posizione gps dal locationa Manager
					this.myLocation = (Location)myLocationManager.getCurrentLocation("gps");
					
					DecimalFormat df = new DecimalFormat("#00.000000");
	
					String lat_str = df.format(this.myLocation.getLatitude());
			        String lng_str = df.format(this.myLocation.getLongitude());
					
					Log.v("GPSThread","posizione corrente: "+lat_str+" "+lng_str);
					
					Thread.sleep(MINIMUM_TIME_BETWEEN_UPDATE);
				
				} else {
					System.out.println("GPSThread.run():nullpointerexception");
				}

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 

		}
	}



	public void quit(){
		run = false;
	}
	
}
