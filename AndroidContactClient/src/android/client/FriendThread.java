package android.client;

import java.util.LinkedList;
import java.util.List;

import android.os.DeadObjectException;
import android.util.Log;

public class FriendThread extends Thread {

	private ServiceInterface.Stub servInt =  null;
	private boolean run = true;
	private long waitMinutes = 1;
	private List<String> friendReq = new LinkedList<String>();
	private Object Lock = new Object();


	public boolean addFriendRequest(String friend){
		synchronized (Lock){
			return friendReq.add(friend);
		}
	}

	public FriendThread(ServiceInterface.Stub s){
		super();
		servInt = s;

	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		while (run){
			try {
				Thread.sleep(waitMinutes*60000);
				synchronized (Lock) {
					if (friendReq.size() > 0) {
						//Ho inserito qualche richiesta nella lista degli amici
						Log.v("FriendThread", "Controllo se c'e' qualcuno di nuovo nella lista degli amici");
						List<String> friendsList = servInt.getFriends();
						List<String> friendsReqCopy = friendReq;
						for (String friend : friendReq){
							//per ogni richiesta effettuata controllo se compare nella lista degli amici e quindi è stata accettata
							if (friendsList.contains(friend)){
								servInt.insertContact(friend);
								friendsReqCopy.remove(friend);
							}
						}
						friendReq = friendsReqCopy;
					}
				}

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (DeadObjectException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}



	public void quit(){
		run = false;
	}
}
