package federazione;

import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;

import tuplespace.NodoRemotoInterface;

import net.jini.core.entry.Entry;
import net.jini.core.transaction.Transaction;

public class ReadRequest  {
//	private String source;
//	private int sourceHash;
	private List<NodoRemotoInterface> partecipants;
	private List<ReadThread> readThreads = new LinkedList<ReadThread>();
	private Response listener;
	private NodoRemotoInterface source;
	private Entry e;
	private Transaction t;
	private long l;
	private boolean done = false;

	public ReadRequest(NodoRemotoInterface n, List<NodoRemotoInterface> partecipants, Response listener) throws RemoteException{
		System.out.println("ReadRequest.ReadRequest()");
//		this.source = n.getJavaSpaceAddress();
		this.source = n;
//		this.sourceHash = n.hashCode();
		this.partecipants = partecipants;
		this.listener = listener;

	}

	public void read(Entry e , Transaction t, long l){
		this.e=e;
		this.t=t;
		this.l=l;
		for (int i= 0; i< partecipants.size(); i++){
			try{
			ReadThread rt = new ReadThread( this, source, partecipants.get(i).getJavaSpaceAddress(), e,t,l);
			rt.start();
			readThreads.add(rt);
			System.out.println("Read Threads: "+readThreads.size());
			
			}catch (RemoteException ex){
				System.out.println("ReadRequest: impossibile raggiungere un nodo");
			}
		}

	
	}
	public void addNode(NodoRemotoInterface n){
		System.out.println("ReadRequest.addNode()");
		try {
		ReadThread rt = new ReadThread( this, source, n.getJavaSpaceAddress(), e,t,l);
		rt.start();
		readThreads.add(rt);
		System.out.println("Read Threads: "+readThreads.size());
		}
		catch (RemoteException e){
			System.out.println("nodo disconnesso, impossibile aggiungere");
		}
	}
	
	
	public void gotResult(NodoRemotoInterface source, Entry e){
		for (ReadThread rt : readThreads){
			rt.halt();
		}
		done = true;
		listener.response(source, e);
	}
	public void throwException(NodoRemotoInterface source, Exception e){
		for (ReadThread rt : readThreads){
			rt.halt();
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
