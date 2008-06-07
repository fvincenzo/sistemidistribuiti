package federazione;

import java.rmi.Remote;
import java.rmi.RemoteException;

import tuplespace.NodoRemotoInterface;
import net.jini.core.entry.Entry;
import net.jini.core.transaction.Transaction;

/**
 * Interfaccia per la Federazione per permettere ai nodi remodi di invocare dei metodi tramite RMI
 * 
 * @author Vincenzo Frascino
 * @author Nicolas Tagliani
 *
 */
public interface FederazioneInterface extends Remote {

	/**
	 * Permette a un nodo remoto di lasciare una federazione
	 * 
	 * @param n il riferimento del nodo remoto che vuole lasciare la federazione
	 * @throws RemoteException
	 */
	public boolean leave(NodoRemotoInterface n)throws RemoteException;
	
	/**
	 * Effettua una read bloccante sulla federazione
	 * 
	 * @param n il nodo remoto che ha richiesto la read
	 * @param e L'Entry richiesta dal nodo remoto
	 * @param t la transazione associata all'entry
	 * @param l il periodo di lease associato alla read
	 * @throws RemoteException
	 * 
	 */
	public void read(NodoRemotoInterface n, Entry e, Transaction t, long l) throws RemoteException;
	
	/**
	 * Effettua una take bloccante sulla federazione
	 * 
	 * @param n il nodo remoto che ha richiesto la take
	 * @param e L'Entry richiesta dal nodo remoto
	 * @param t la transazione associata all'entry
	 * @param l il periodo di lease associato alla take
	 * @throws RemoteException
	 * 
	 */
	public void take(NodoRemotoInterface n, Entry e, Transaction t, long l) throws RemoteException;
	
	/**
	 * Effettua una read non bloccante sulla federazione
	 * 
	 * @param n il nodo remoto che ha richiesto la read
	 * @param e L'Entry richiesta dal nodo remoto
	 * @param t la transazione associata all'entry
	 * @param l il periodo di lease associato alla read
	 * @throws RemoteException
	 * 
	 */
	public Entry readIfExists(NodoRemotoInterface n, Entry e, Transaction t, long l) throws RemoteException;
	
	/**
	 * Effettua una take non bloccante sulla federazione
	 * 
	 * @param n il nodo remoto che ha richiesto la take
	 * @param e L'Entry richiesta dal nodo remoto
	 * @param t la transazione associata all'entry
	 * @param l il periodo di lease associato alla take
	 * @throws RemoteException
	 * 
	 */
	public Entry takeIfExists(NodoRemotoInterface n, Entry e, Transaction t, long l) throws RemoteException;
	
}
