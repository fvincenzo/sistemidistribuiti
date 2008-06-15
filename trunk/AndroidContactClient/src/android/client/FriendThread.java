package android.client;

import java.util.LinkedList;
import java.util.List;

import android.os.DeadObjectException;

public class FriendThread extends Thread {

	private ServiceInterface.Stub servInt =  null;
	private boolean run = true;
	private long waitMinutes = 1;
	private List<String> friendReq = new LinkedList<String>();
	private Object Lock;


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

					List<String> friendsList = servInt.getFriends();
					List<String> friendsReqCopy = friendReq;
					for (String friend : friendReq){
						if (friendsList.contains(friend)){
							insertFriend(friend);
							friendsReqCopy.remove(friend);
						}
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
	private void insertFriend(String f){
		//TODO: implementare l'inserimento in rubrica con tutti i parametri
	}


	public void quit(){
		run = false;
	}
}
