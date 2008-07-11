package android.client;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.DeadObjectException;
import android.util.Log;
import java.lang.Math;

public class GPSThread extends Thread {

	private ServiceInterface.Stub servInt =  null;
	private boolean run = true;
	private long waitMinutes = 1;
	
	protected LocationManager myLocationManager = null;
    protected Location myLocation = null; 
    protected DBHelper db;
	
    protected final long MINIMUM_TIME_BETWEEN_UPDATE = 2500; // in Millisecondi 

	public GPSThread(ServiceInterface.Stub s,LocationManager loc,DBHelper db){
		super();
		servInt = s;
		
		//Setup Location Manager
		this.myLocationManager=loc;

		//Setup Database
		this.db=db;
	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		double apos = 0;
		double pos = 1000000; 
		String Position = null;
		
		super.run();
		while (run){
			try {
				if(myLocationManager!=null) {
					
					//Istanzio il DB
					List<DBHelper.Location> location = db.fetchAllRows();
					
					//Prendo la mia posizione gps dal locationa Manager
					this.myLocation = (Location)myLocationManager.getCurrentLocation("gps");
					
					DecimalFormat df = new DecimalFormat("#00.000000");
	
					String lat_str = df.format(this.myLocation.getLatitude());
			        String lng_str = df.format(this.myLocation.getLongitude());
					
			        double lat_flo = Double.parseDouble(lat_str);
			        double lng_flo = Double.parseDouble(lng_str);
					
					Thread.sleep(MINIMUM_TIME_BETWEEN_UPDATE);
					
					Iterator<DBHelper.Location> i = location.iterator();
					
					while(i.hasNext()) {
						
						DBHelper.Location l = (DBHelper.Location) i.next();
						
						apos = disgeod(((double)l.latitude/1000000),((double)l.longitude/1000000),lat_flo,lng_flo);
						
						Log.v("GPSThread","posizione corrente: "+ apos );
						
						if(apos<pos) {
							
							pos = apos;
							Position = l.preferred;
							
						}
						
					}
					
					Log.v("GPSThread","posizione corrente: "+ pos +" "+ Position);
				
				} else {
					System.out.println("GPSThread.run():nullpointerexception");
				}

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 

		}
	}

	public double disgeod (double latA, double lonA, double latB, double lonB) {
		 
		/* Definisce le costanti e le variabili */
		  double R = 6371;
		  double pigreco = 3.1415927;
		  double lat_alfa, lat_beta;
		  double lon_alfa, lon_beta;
		  double fi;
		  double p, d;
		  /* Converte i gradi in radianti */
		  lat_alfa = pigreco * latA / 180;
		  lat_beta = pigreco * latB / 180;
		  lon_alfa = pigreco * lonA / 180;
		  lon_beta = pigreco * lonB / 180;
		  /* Calcola l'angolo compreso fi */
		  fi = Math.abs(lon_alfa - lon_beta);
		  /* Calcola il terzo lato del triangolo sferico */
		  p = Math.acos(Math.sin(lat_beta) * Math.sin(lat_alfa) + Math.cos(lat_beta) * Math.cos(lat_alfa) * Math.cos(fi));
		  /* Calcola la distanza sulla superficie terrestre R = ~6371 km */
		  d = p * R;
		  return d;
	}


	public void quit(){
		run = false;
	}
	
}
