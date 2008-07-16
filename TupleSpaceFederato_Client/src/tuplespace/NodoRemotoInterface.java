package tuplespace;


import java.rmi.Remote;
import java.rmi.RemoteException;
import net.jini.core.entry.Entry;
import net.jini.core.transaction.Transaction;

/**
 * Questa interfaccia rappresenta le funzionalita' remote offerte dalla classe NodoLocale ed utilizzate nella federazione da remoto.
 *
 * @author Vincenzo Frascino
 * @author Nicolas Tagliani
 *
 */
public interface NodoRemotoInterface extends Remote {

	/**
	 * Metodo richiamato da remoto pe le letture non bloccandi nella federazione
	 * 
	 * @param e L'entry da leggere
	 * @param t la transazione associata alla lettura
	 * @param l il tempo di lease associato alla lettura
	 * @return l'entry letta
	 * @throws RemoteException
	 */
	public Entry remoteReadIfExists(Entry e, Transaction t, long l) throws RemoteException;
	
	/**
	 * Metodo richiamato da remoto pe le take non bloccandi nella federazione
	 * 
	 * @param e L'entry da leggere
	 * @param t la transazione associata alla lettura
	 * @param l il tempo di lease associato alla lettura
	 * @return l'entry letta
	 * @throws RemoteException
	 */
	public Entry remoteTakeIfExists(Entry e, Transaction t, long l) throws RemoteException;
	
	/**
	 * Metodo utilizzato da remoto per notificare l'ottenimento di un qualche risultato dopo una read o una take bloccanti.
	 * 
	 * @param e L'entry ottenuta come risultato di una read o una take bloccanti
	 * @throws RemoteException
	 */
	public void putResult(Entry e) throws RemoteException;
	
	/**
	 * Metodo utilizzato da remoto per notificare un'eccezione dopo una read o una take bloccanti.
	 * 
	 * @param e l'eccezzione accaduta sul nodo remoto
	 * @throws RemoteException
	 */
	public void throwException(Exception e) throws RemoteException;
	
	/**
	 * Metodo remoto usato dalla federazione per ottenere l'indirizzo pubblico del tuplespace locale
	 * 
	 * @return L'indirizzo pubblico del tuplespace
	 * @throws RemoteException
	 */
	public String getJavaSpaceAddress() throws RemoteException;
}
