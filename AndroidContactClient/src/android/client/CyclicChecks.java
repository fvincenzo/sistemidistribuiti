package android.client;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import android.os.DeadObjectException;
import android.util.Log;

public class CyclicChecks extends Thread {

	private ServiceInterface.Stub service =  null;
	private long minutesToSleep = 2;
	private boolean run = true;


	private int NOTIFY = 1;
	private int NORMAL = 0;
	private int status = 0;
	private Map<String, String> localMap = new HashMap<String, String>();
	private Set<String> friendSet = new HashSet<String>();

	public CyclicChecks(ServiceInterface.Stub service) {
		super();
		this.service = service;
	}

	public void setNormal(){
		status = NORMAL;
	}

	
	@Override
	public void run(){
		while (run){
			try {
				Thread.sleep(60000*minutesToSleep);
				//Check for pending friends
				if (status == NORMAL){
					if ( service.pendingFriends().size() > 0 ){
						service.notifyPendings();
						status = NOTIFY;
					}
				}

				//
				for (String friend : service.getFriends()){

					//Aggiorno all'avvio la lista degli amici
//					if (!friendSet.contains(friend)){
//						friendSet.add(friend);
//						service.insertContact(friend);
//					}
					String pref = service.getpreferred(friend);
					//L'utente è già presente nella lista locale degli amici
					if (localMap.containsKey(friend)){
						
						if (!pref.equals(localMap.get(friend))){
							//se è cambiato il modo di essere contattati richiedo anche gli altri dati
							service.insertContact(friend);
							String s = service.checkposition(friend);
							service.setPosition(friend, s);
							setPreferred(friend, pref);
						}
					}
					else {
//						L'utente non è presente nella lista quindi lo inserisco e mi scairco i sui dati per sicurezza
						localMap.put(friend, pref);
						service.insertContact(friend);
						setPreferred(friend, pref);
						String s = service.checkposition(friend);
						service.setPosition(friend, s);

					}
				}
			}
			catch (InterruptedException e) {

			}
			catch (DeadObjectException e){

			}
		}
	}


	public void quit(){
		run = false;
	}


	private void setPreferred(String friend, String pref){
		try {
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
					}else
						if (pref.equals("MAIL")){
							Log.v("CheckPreferredMode", "Devo settare MAIL ---");
							service.setMail(friend);
						}else
							if (pref.equals("IM")){
								Log.v("CheckPreferredMode", "Devo settare IM ---");
								service.setIm(friend);
							}
		}catch (DeadObjectException e){

		}
	}
}
