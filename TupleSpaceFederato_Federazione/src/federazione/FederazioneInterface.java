package federazione;

import java.rmi.Remote;
import java.rmi.RemoteException;

import tuplespace.NodoRemotoInterface;
import net.jini.core.entry.Entry;
import net.jini.core.transaction.Transaction;

public interface FederazioneInterface extends Remote {

	public boolean leave(NodoRemotoInterface n)throws RemoteException;
	
	public void read(NodoRemotoInterface n, Entry e, Transaction t, long l) throws RemoteException;
	public void take(NodoRemotoInterface n, Entry e, Transaction t, long l) throws RemoteException;
	public Entry readIfExists(NodoRemotoInterface n, Entry e, Transaction t, long l) throws RemoteException;
	public Entry takeIfExists(NodoRemotoInterface n, Entry e, Transaction t, long l) throws RemoteException;
	
}
