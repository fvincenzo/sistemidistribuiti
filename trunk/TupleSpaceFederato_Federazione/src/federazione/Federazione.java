package federazione;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

//import federationservice.EventListener;

import tuplespace.NodoRemotoInterface;

import net.jini.core.entry.Entry;
//import net.jini.core.event.RemoteEvent;
//import net.jini.core.event.UnknownEventException;
import net.jini.core.transaction.Transaction;

@SuppressWarnings("serial")
public class Federazione extends UnicastRemoteObject implements FederazioneInterface , Response {

	private List<NodoRemotoInterface> nodiFederati;
	private List<TakeRequest> takeQueue = new LinkedList<TakeRequest>();
	private List<ReadRequest> readQueue = new LinkedList<ReadRequest>();

	public Federazione() throws RemoteException{
		nodiFederati= new LinkedList<NodoRemotoInterface>();
	}
	public  synchronized void join(NodoRemotoInterface n){
		System.out.println("Federazione.join() del nodo: "+n);
		nodiFederati.add(n);
		Set<ReadRequest> toRemoveRead = new HashSet<ReadRequest>();
		for(ReadRequest rrq: readQueue){
			if (!rrq.isDone()){
				System.out.println("Read Request pending: adding node"+n);
				rrq.addNode(n);
			}
			else {
				toRemoveRead.add(rrq);
			}
		}
		for (ReadRequest rrq : toRemoveRead){
			readQueue.remove(rrq);
		}
		
		Set<TakeRequest> toRemoveTake = new HashSet<TakeRequest>();
		for(TakeRequest trq: takeQueue){
			if (!trq.isDone()){
				System.out.println("Take Request pending: adding node"+n);
				trq.addNode(n);
			}
			else {
				toRemoveTake.add(trq);
			}
		}
		for (TakeRequest trq : toRemoveTake){
			readQueue.remove(trq);
		}
		
		
	}
	
	
	public boolean leave(NodoRemotoInterface n) throws RemoteException {
		System.out.println("Federazione.leave() del nodo: "+n.hashCode());
		return nodiFederati.remove(n);
	}


	public void read(NodoRemotoInterface n, Entry e, Transaction t, long l)
	throws RemoteException {

			
		System.out.println("Federazione.read()da nodo:"+n);
		ReadRequest rrq = new ReadRequest(n, nodiFederati, this);
		rrq.read(e, t, l);
		readQueue.add(rrq);
		

	}

	public Entry readIfExists(NodoRemotoInterface n, Entry e, Transaction t,
			long l) throws RemoteException {
		System.out.println("Federazione.readIfExists()");
		Entry ret = null;
		for (int i = 0; i<nodiFederati.size() && ret == null; i++){
			ret = nodiFederati.get(i).remoteReadIfExists(e, t, l);
		}
		return ret;
	}

	public void take(NodoRemotoInterface n, Entry e, Transaction t, long l)
	throws RemoteException {

			
		System.out.println("Federazione.take() da nodo:"+n);
		TakeRequest trq = new TakeRequest(n, nodiFederati, this);
		trq.take(e, t, l);
		takeQueue.add(trq);
		
	}


	public Entry takeIfExists(NodoRemotoInterface n, Entry e, Transaction t,
			long l) throws RemoteException {
		System.out.println("Federazione.takeIfExists()");
		Entry ret = null;
		for (int i = 0; i<nodiFederati.size() && ret == null; i++){
			ret = nodiFederati.get(i).remoteTakeIfExists(e, t, l);
		}
		return ret;
	}

	public synchronized void response(NodoRemotoInterface source, Entry e) {
		System.out.println("Federazione.response()");
		Set<NodoRemotoInterface> toRemove = new HashSet<NodoRemotoInterface>();
		for (NodoRemotoInterface nr : nodiFederati ){
			try {
			if (nr.equals(source)){
				nr.putResult(e);
				break;
			}
			} catch (RemoteException ex) {
				System.out.println("Nodo non più presente e quindi eliminato");
				toRemove.add(nr);
			}
		}
		if (!toRemove.isEmpty()){
			for (NodoRemotoInterface n : toRemove){
				nodiFederati.remove(n);
			}
			toRemove.clear();
		}
		
	}
	public synchronized void throwException(NodoRemotoInterface source, Exception e) {
		System.out.println("Federazione.response()");
		Set<NodoRemotoInterface> toRemove = new HashSet<NodoRemotoInterface>();
		for (NodoRemotoInterface nr : nodiFederati ){
			try {
			if (nr.equals(source)){
				nr.throwException(e);
				break;
			}
			} catch (RemoteException ex) {
				System.out.println("Nodo non più presente e quindi eliminato");
				toRemove.add(nr);
			}
		}
		if (!toRemove.isEmpty()){
			for (NodoRemotoInterface n : toRemove){
				nodiFederati.remove(n);
			}
			toRemove.clear();
		}
	}



}
