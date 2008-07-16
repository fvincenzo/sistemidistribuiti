package federazione;

import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;
import tuplespace.NodoRemotoInterface;
import net.jini.core.entry.Entry;
import net.jini.core.transaction.Transaction;


/**
 * Classe che si occupa istanziare e gestire una take bloccante sulla federazione.
 * Per ogni take bloccante verra' istanziata una classe di tipo TakeRequest
 * 
 * @author Vincenzo Frascino
 * @author Nicolas Tagliani
 *
 */
public class TakeRequest  {
	

	/**
	 * Lista di nodi su cui propagare la take request
	 */
	private List<NodoRemotoInterface> partecipants;
	/**
	 * Lista di Thread di take correntemente attivi
	 */
	private List<TakeThread> takeThreads = new LinkedList<TakeThread>();
	/**
	 * Riferimento al listener per le risposte dai thread
	 */
	private Response listener;
	/**
	 * riferimento al nodo che ha effettuato la richiesta di take
	 */
	private NodoRemotoInterface source;
	/**
	 * Entry ricevuta durante la take request
	 */
	private Entry e;
	/**
	 * Transazione associata all'entry
	 */
	private Transaction t;
	/**
	 * Durata della lease di take
	 */
	private long l;
	/**
	 * Flag per determinare la terminazione della takerequest
	 */
	private boolean done = false;

	
	/**
	 * Costruttore della classe TakeRequest
	 * 
	 * @param n Riferimento al nodo remoto che ha effettuato la richiesta
	 * @param partecipants Lista di nodi remoti su cui viene propagata la take request
	 * @param listener Riferimento del listener per le risposte dai take thread
	 * @throws RemoteException
	 */
	public TakeRequest(NodoRemotoInterface n, List<NodoRemotoInterface> partecipants, Response listener) throws RemoteException{
		System.out.println("TakeRequest.TakeRequest()");
		this.source = n;
		this.partecipants = partecipants;
		this.listener = listener;
	}

	/**
	 * Effettua la take bloccante di una entry sulla federazione
	 * 
	 * @param e L'entry da ottenere
	 * @param t La transazione associata all'entry
	 * @param l Tempo di lease associato alla take
	 */
	public void take(Entry e , Transaction t, long l){
		this.e=e;
		this.t=t;
		this.l=l;
		for (int i= 0; i< partecipants.size(); i++){
			try{
				TakeThread tt = new TakeThread( this, source, partecipants.get(i).getJavaSpaceAddress(), e,t,l);
				tt.start();
				takeThreads.add(tt);
			}catch (RemoteException ex){
				System.out.println("TakeRequest: impossibile raggiungere un nodo");
			}
		}
	}

	/**
	 * Aggiunge un nodo alla take request. Questo metodo viene richiamato in fase di join se esistono gia' delle takerequest sul sistema.
	 * 
	 * @param n Il nodo da aggiungere alla takerequest
	 */
	public void addNode(NodoRemotoInterface n){
		try {
			TakeThread tt = new TakeThread( this, source, n.getJavaSpaceAddress(), e,t,l);
			tt.start();
			takeThreads.add(tt);		
		}
		catch (RemoteException e){
			System.out.println("nodo disconnesso, impossibile aggiungere");
		}
	}

	/**
	 * Metodo utilizzato per propagare un risultato da un takethread
	 * 
	 * @param source Il nodo sorgente che deve ricevere il risultato
	 * @param e Il risultato da trasmettere
	 */
	public void gotResult(NodoRemotoInterface source, Entry e){
		for (TakeThread tt : takeThreads){
			tt.halt();
		}
		done = true;
		listener.response(source, e);
	}
	
	/**
	 * Metodo utilizzato per propagare un'eccezione da un takethread
	 * 
	 * @param source Il nodo sorgente che deve ricevere l'eccezione
	 * @param e L'eccezione da trasmettere
	 */
	public void throwException(NodoRemotoInterface source, Exception e){
		for (TakeThread tt : takeThreads){
			tt.halt();
		}
		done = true;
		listener.throwException(source, e);
	}

	/**
	 * Metodo che resitituisce lo stato della takerequest.
	 * Se un risultato o una eccezione e' stata propagata la Take Request sara' nello stato done e il flag sara' true.
	 * 
	 * @return True se la take request ha finito il suo lavoro false altrimenti
	 */
	public boolean isDone(){
		return done;
	}

}
