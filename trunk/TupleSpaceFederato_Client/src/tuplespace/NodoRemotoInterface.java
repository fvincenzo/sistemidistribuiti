package tuplespace;



import java.rmi.Remote;
import java.rmi.RemoteException;

import net.jini.core.entry.Entry;
import net.jini.core.transaction.Transaction;

public interface NodoRemotoInterface extends Remote {

	public Entry remoteReadIfExists(Entry e, Transaction t, long l) throws RemoteException;
	public Entry remoteTakeIfExists(Entry e, Transaction t, long l) throws RemoteException;
	public void putResult(Entry e) throws RemoteException;
	public void throwException(Exception e) throws RemoteException;
	public String getJavaSpaceAddress() throws RemoteException;
}
