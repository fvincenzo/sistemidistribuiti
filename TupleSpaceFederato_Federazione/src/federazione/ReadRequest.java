package federazione;

import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;
import tuplespace.NodoRemotoInterface;
import net.jini.core.entry.Entry;
import net.jini.core.transaction.Transaction;

/**
 * Classe che si occupa istanziare e gestire una read bloccante sulla federazione. Per ogni read bloccante verra' istanziata una classe di tipo ReadRequest
 * @author  Vincenzo Frascino
 * @author  Nicolas Tagliani
 */
public class ReadRequest  {

	/**
	 * Lista di nodi su cui propagare la read request
	 */
	private List<NodoRemotoInterface> partecipants;
	/**
	 * Lista di Thread di lettura correntemente attivi
	 */
	private List<ReadThread> readThreads = new LinkedList<ReadThread>();
	/**
	 * Riferimento al listener per le risposte dai thread
	 */
	private Response listener;
	/**
	 * riferimento al nodo che ha effettuato la richiesta di read
	 */
	private NodoRemotoInterface source;
	/**
	 * Entry ricevuta durante la read request
	 */
	private Entry e;
	/**
	 * Transazione associata all'entry
	 */
	private Transaction t;
	/**
	 * Durata della lease di lettura
	 */
	private long l;
	/**
	 * Flag per determinare la terminazione della readrequest
	 */
	private boolean done = false;

	/**
	 * Costruttore della classe ReadRequest
	 * 
	 * @param n Riferimento al nodo remoto che ha effettuato la richiesta
	 * @param partecipants Lista di nodi remoti su cui viene propagata la read request
	 * @param listener Riferimento del listener per le risposte dai read thread
	 * @throws RemoteException
	 */
	public ReadRequest(NodoRemotoInterface n, List<NodoRemotoInterface> partecipants, Response listener) throws RemoteException{
		this.source = n;
		this.partecipants = partecipants;
		this.listener = listener;

	}

	/**
	 * Effettua la lettura bloccante di una entry sulla federazione
	 * 
	 * @param e L'entry da leggere
	 * @param t La transazione associata all'entry
	 * @param l Tempo di lease associato alla lettura
	 */
	public synchronized void read(Entry e , Transaction t, long l){
		this.e=e;
		this.t=t;
		this.l=l;
		for (int i= 0; i< partecipants.size(); i++){
			try{
				ReadThread rt = new ReadThread( this, source, partecipants.get(i).getJavaSpaceAddress(), e,t,l);
				rt.start();
				readThreads.add(rt);
			}catch (RemoteException ex){
				System.out.println("ReadRequest: impossibile raggiungere un nodo");
			}
		}
	}
	
	/**
	 * Aggiunge un nodo alla read request. Questo metodo viene richiamato in fase di join se esistono gia' delle readrequest sul sistema.
	 * 
	 * @param n Il nodo da aggiungere alla readrequest
	 */
	public synchronized void addNode(NodoRemotoInterface n){
		try {
			ReadThread rt = new ReadThread( this, source, n.getJavaSpaceAddress(), e,t,l);
			rt.start();
			readThreads.add(rt);
		}
		catch (RemoteException e){
			System.out.println("nodo disconnesso, impossibile aggiungere");
		}
	}

	/**
	 * Metodo utilizzato per propagare un risultato da un readthread
	 * 
	 * @param source Il nodo sorgente che deve ricevere il risultato
	 * @param e Il risultato da trasmettere
	 */
	public void gotResult(NodoRemotoInterface source, Entry e){
		for (ReadThread rt : readThreads){
			rt.halt();
		}
		done = true;
		listener.response(source, e);
	}
	
	/**
	 * Metodo utilizzato per propagare un'eccezione da un readthread
	 * 
	 * @param source Il nodo sorgente che deve ricevere l'eccezione
	 * @param e L'eccezione da trasmettere
	 */
	public void throwException(NodoRemotoInterface source, Exception e){
		for (ReadThread rt : readThreads){
			rt.halt();
		}
		done = true;
		listener.throwException(source, e);
	}

	/**
	 * Metodo che resitituisce lo stato della readrequest Se un risultato o una eccezione e' stata propagata la Read Request sara' nello stato done e il flag sara' true.
	 * @return  True se la read request ha finito il suo lavoro false altrimenti
	 * @uml.property  name="done"
	 */
	public boolean isDone(){
		return done;
	}


}
