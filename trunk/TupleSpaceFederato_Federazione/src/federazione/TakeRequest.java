package federazione;

import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;

import tuplespace.NodoRemotoInterface;

import net.jini.core.entry.Entry;
import net.jini.core.transaction.Transaction;

public class TakeRequest  {
//	private String source;
//	private int sourceHash;
	private List<NodoRemotoInterface> partecipants;
	private List<TakeThread> takeThreads = new LinkedList<TakeThread>();
	private Response listener;
	private NodoRemotoInterface source;
	private Entry e;
	private Transaction t;
	private long l;
	private boolean done = false;


	public TakeRequest(NodoRemotoInterface n, List<NodoRemotoInterface> partecipants, Response listener) throws RemoteException{
		System.out.println("TakeRequest.TakeRequest()");
		this.source = n;
//		this.sourceHash = n.hashCode();
		this.partecipants = partecipants;
		this.listener = listener;
	}

	public void take(Entry e , Transaction t, long l){
		this.e=e;
		this.t=t;
		this.l=l;
		for (int i= 0; i< partecipants.size(); i++){
			try{
			TakeThread tt = new TakeThread( this, source, partecipants.get(i).getJavaSpaceAddress(), e,t,l);
			tt.start();
			takeThreads.add(tt);
			System.out.println("Take Threads: "+takeThreads.size());
			}catch (RemoteException ex){
				System.out.println("TakeRequest: impossibile raggiungere un nodo");
			}
		}
	}
	
	public void addNode(NodoRemotoInterface n){
	System.out.println("TakeRequest.addNode()");
		try {
		TakeThread tt = new TakeThread( this, source, n.getJavaSpaceAddress(), e,t,l);
		tt.start();
		takeThreads.add(tt);
		System.out.println("Take Threads: "+takeThreads.size());
		
		}
		catch (RemoteException e){
			System.out.println("nodo disconnesso, impossibile aggiungere");
		}
	}
	
	
	public void gotResult(NodoRemotoInterface source, Entry e){
		System.out.println("TakeRequest.gotResult()");
		for (TakeThread tt : takeThreads){
			tt.halt();
		}
		done = true;
		listener.response(source, e);
	}
	public void throwException(NodoRemotoInterface source, Exception e){
		for (TakeThread tt : takeThreads){
			tt.halt();
		}
		done = true;
		listener.throwException(source, e);
	}
	
	public boolean isDone(){
		return done;
	}

//	public void response(String source, Entry e) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	public void throwException(Exception e) {
//		// TODO Auto-generated method stub
//		
//	}

}
