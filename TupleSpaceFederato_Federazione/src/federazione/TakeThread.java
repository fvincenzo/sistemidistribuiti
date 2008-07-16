package federazione;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import tuplespace.NodoRemotoInterface;
import net.jini.core.discovery.LookupLocator;
import net.jini.core.entry.Entry;
import net.jini.core.entry.UnusableEntryException;
import net.jini.core.lookup.ServiceRegistrar;
import net.jini.core.lookup.ServiceTemplate;
import net.jini.core.transaction.Transaction;
import net.jini.core.transaction.TransactionException;
import net.jini.space.JavaSpace;

/**
 * Questa classe si occupa di effettuare una read blocante su un nodo remoto per conto di una federazione.
 * Una volta ottenuto un risultato questo verra' propagato indietro alla federazione chiamante.
 * 
 * @author Vincenzo Frascino
 * @author Nicolas Tagliani
 *
 */
public class TakeThread extends Thread {

	/**
	 * Il javaspace remoto su cui effettuare la read
	 */
	private JavaSpace js;
	/**
	 * l'entry da richiedere al javaspace remoto
	 */
	private Entry e;
	/**
	 * La trasazione associata all'entry
	 */
	private Transaction t; 
	/**
	 * Tempo massimo per rimanere in attesa di una risposta
	 */
	private long l;
	/**
	 * Flag per imporre la terminazione del thread
	 */
	private boolean quit = false;
	/**
	 * Riferimento all'oggetto che ricevera' la notifica della risposta
	 */
	private TakeRequest tq;
	/**
	 * Tempo massimo di attesa per singola richiesta cosi' da non saturare il server con thread sempre attivi
	 */
	private long updateRate = 10000;
	/**
	 * Riferimento dell'oggetto che dovra' ottenere il risultato
	 */
	private NodoRemotoInterface nodoSource;

	/**
	 * Costruttore della classe takethread. 
	 * Viene inizializzato con tutti i parametri necessari a effettuare una take
	 * 
	 * @param rq Riferimento dell'oggetto che ha istanziato questo takethread
	 * @param nodoSource Nodo remoto che dovra' ottenere l'eventuale risposta
	 * @param address indirizzo del javaspace su cui effettuare la take
	 * @param e l'entry da ottenere
	 * @param t la trasazione associata alla take
	 * @param l il tempo massimo di attesa per la take
	 */
	public TakeThread(TakeRequest tq, NodoRemotoInterface nodoSource, String address, Entry e, Transaction t, long l){
		try {

			LookupLocator lookupL = new LookupLocator(address);
			ServiceRegistrar registrar = lookupL.getRegistrar();
			js = (JavaSpace)registrar.lookup(new ServiceTemplate(null, new Class[]{ JavaSpace.class }, null));
			this.tq=tq;
			this.nodoSource = nodoSource;
			this.e=e;
			this.t=t;
			this.l=l;

		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (RemoteException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}	
	}

	/**
	 * Metodo ereditato dalla classe thread per mandare in esecuzione il takeThread.
	 */
	public void run(){
		boolean run = true;
		Entry ret = null;
		while (!quit & run & ret == null){
			try {
				if (l > updateRate){
					ret = js.take(e,t,updateRate);
					l = l-updateRate;
				}
				else {
					ret = js.take(e,t,l);
					run = false;
				}
			} catch (RemoteException e) {
				tq.throwException(nodoSource, e);
			} catch (UnusableEntryException e) {
				tq.throwException(nodoSource, e);
			} catch (TransactionException e) {
				tq.throwException(nodoSource, e);
			} catch (InterruptedException e) {
				tq.throwException(nodoSource, e);
			}
		}
		if (!quit) tq.gotResult(nodoSource, ret);
	}
	
	/**
	 * Ferma il readThread. L'operazione sara' effettuata entro 10 secondi.
	 */
	public void halt(){
		quit = true;
	}

}
