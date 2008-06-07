package federazione;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import tuplespace.NodoRemotoInterface;
import net.jini.core.entry.Entry;
import net.jini.core.transaction.Transaction;

/**
 * Questa classe implementa tutte le funzionalita' richieste a una federazione.
 * Si tratta in tutto e per tutto di un oggetto remoto.
 * I client devono fornire un riferimetno ad un NodoLocale tramite NodoRemotoInterface per permettere alla federazione di invocare metodi sui processi client
 * 
 * @author Vincenzo Frascino
 * @author Nicolas Tagliani
 *
 */
@SuppressWarnings("serial")
public class Federazione extends UnicastRemoteObject implements FederazioneInterface , Response {

	/**
	 * Lista di nodi attualmante appartenenti a questa federazione
	 */
	private List<NodoRemotoInterface> nodiFederati;

	/**
	 * Lista di take bloccanti attualmente richieste a questa federazione
	 */
	private List<TakeRequest> takeQueue = new LinkedList<TakeRequest>();

	/**
	 * Lista di read bloccanti attualmente richieste a questa federazione
	 */
	private List<ReadRequest> readQueue = new LinkedList<ReadRequest>();

	/**
	 * Costruttore vuoto che semplicemente inizializza la lista dei nodi federati
	 * 
	 * @throws RemoteException
	 */
	public Federazione() throws RemoteException{
		nodiFederati= new LinkedList<NodoRemotoInterface>();
	}

	/**
	 * Permette a un nodo remoto di aggiungersi a questa federazione.
	 * Nella fase di join inoltre ad ogni nodo vengono inoltrate le read e le take bloccanti attualmente presenti sulla federazione
	 * 
	 * @param n il riferimento del nodo remoto che vuole unirsi alla federazione
	 */
	public  synchronized void join(NodoRemotoInterface n){
		nodiFederati.add(n);
		Set<ReadRequest> toRemoveRead = new HashSet<ReadRequest>();
		for(ReadRequest rrq: readQueue){
			if (!rrq.isDone()){
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

	/**
	 * Permette a un nodo remoto di lasciare una federazione
	 * 
	 * @param n il riferimento del nodo remoto che vuole lasciare la federazione
	 * @throws RemoteException
	 */
	public boolean leave(NodoRemotoInterface n) throws RemoteException {
		return nodiFederati.remove(n);
	}


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
	public void read(NodoRemotoInterface n, Entry e, Transaction t, long l)
	throws RemoteException {
		ReadRequest rrq = new ReadRequest(n, nodiFederati, this);
		rrq.read(e, t, l);
		readQueue.add(rrq);


	}

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
	public Entry readIfExists(NodoRemotoInterface n, Entry e, Transaction t, long l) 
	throws RemoteException {
		Entry ret = null;
		for (int i = 0; i<nodiFederati.size() && ret == null; i++){
			ret = nodiFederati.get(i).remoteReadIfExists(e, t, l);
		}
		return ret;
	}

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
	public void take(NodoRemotoInterface n, Entry e, Transaction t, long l)
	throws RemoteException {
		TakeRequest trq = new TakeRequest(n, nodiFederati, this);
		trq.take(e, t, l);
		takeQueue.add(trq);

	}

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
	public Entry takeIfExists(NodoRemotoInterface n, Entry e, Transaction t, long l) 
	throws RemoteException {
		Entry ret = null;
		for (int i = 0; i<nodiFederati.size() && ret == null; i++){
			ret = nodiFederati.get(i).remoteTakeIfExists(e, t, l);
		}
		return ret;
	}

	/**
	 * Metodo invocato da ReadRequest e TakeRequest in caso di risposta di un nodo remoto dopo una read bloccante o una take bloccante
	 * 
	 * @param source Il nodo che aveva effettuato la richiesta di read o take bloccanti
	 * @param e Il risultato della read o della take bloccanti
	 */
	public synchronized void response(NodoRemotoInterface source, Entry e) {
		Set<NodoRemotoInterface> toRemove = new HashSet<NodoRemotoInterface>();
		for (NodoRemotoInterface nr : nodiFederati ){
			try {
				if (nr.equals(source)){
					nr.putResult(e);
					break;
				}
			} catch (RemoteException ex) {
				System.out.println("Nodo non piu' presente e quindi eliminato");
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
	
	/**
	 * Metodo invocato da ReadRequest e TakeRequest in caso di eccezione scaturita a seguito di una read bloccante o una take bloccante
	 * 
	 * @param source il nodo che aveva effettuato la read o la take
	 * @e l'eccezione scaturita dalla richiesta
	 */
	public synchronized void throwException(NodoRemotoInterface source, Exception e) {
		Set<NodoRemotoInterface> toRemove = new HashSet<NodoRemotoInterface>();
		for (NodoRemotoInterface nr : nodiFederati ){
			try {
				if (nr.equals(source)){
					nr.throwException(e);
					break;
				}
			} catch (RemoteException ex) {
				System.out.println("Nodo non piu' presente e quindi eliminato");
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
