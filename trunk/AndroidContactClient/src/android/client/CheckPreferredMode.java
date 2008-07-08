package android.client;

import java.util.HashMap;
import java.util.Map;

import android.os.DeadObjectException;
import android.util.Log;

public class CheckPreferredMode extends Thread {

	private ServiceInterface.Stub service =  null;
	private long minutesToSleep = 2;
	private boolean run = true;

	private Map<String, String> localMap = new HashMap<String, String>();

	public CheckPreferredMode(ServiceInterface.Stub service) {
		super();
		this.service = service;
	}

	@Override
	public void run(){
		while (run){
			try {
				Thread.sleep(60000*minutesToSleep);

				for (String friend : service.getFriends()){
					String pref = service.getpreferred(friend);

					if (localMap.containsKey(friend)){
						Log.v("CheckPreferredMode", "Utente già presente in lista");
						if (!pref.equals(localMap.get(friend))){
							if (pref.equals("HOME")){
								Log.v("CheckPreferredMode", "Devo settare HOME");
								service.setHome(friend);
							} else
								if (pref.equals("MOBILE")){
									Log.v("CheckPreferredMode", "Devo settare MOBILE");
									service.setMobile(friend);
								} else
									if (pref.equals("WORK")){
										Log.v("CheckPreferredMode", "Devo settare WORK");
										service.setWork(friend);
									}
						}
					}
					else {
//						String pref = service.getpreferred(friend);
						Log.v("CheckPreferredMode", "Utente non presente in lista, lo inserisco");
						
						localMap.put(friend, pref);
						if (pref.equals("HOME")){
							Log.v("CheckPreferredMode", "Devo settare HOME ---");
							service.setHome(friend);
						} else
							if (pref.equals("MOBILE")){
								Log.v("CheckPreferredMode", "Devo settare MOBILE ---");
								service.setMobile(friend);
							} else
								if (pref.equals("WORK")){
									Log.v("CheckPreferredMode", "Devo settare WORK ---");
									service.setWork(friend);
								}
					}
				}
			} catch (InterruptedException e) {

			}
			catch (DeadObjectException e){

			}
		}
	}

	public void quit(){
		run = false;
	}


}
