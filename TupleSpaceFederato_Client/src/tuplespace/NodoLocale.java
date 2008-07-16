package tuplespace;

import tuplespace.NodoRemotoInterface;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import federationservice.FederationServiceInterface;
import federazione.FederazioneInterface;
import net.jini.core.discovery.LookupLocator;
import net.jini.core.entry.Entry;
import net.jini.core.entry.UnusableEntryException;
import net.jini.core.lease.Lease;
import net.jini.core.lookup.ServiceRegistrar;
import net.jini.core.lookup.ServiceTemplate;
import net.jini.core.transaction.Transaction;
import net.jini.core.transaction.TransactionException;
import net.jini.core.transaction.TransactionFactory;
import net.jini.core.transaction.UnknownTransactionException;
import net.jini.core.transaction.Transaction.Created;
import net.jini.core.transaction.server.TransactionManager;
import net.jini.core.transaction.server.TransactionParticipant;
import net.jini.lookup.entry.Name;
import net.jini.space.JavaSpace;

/**
 * Classe principale per ogni client.
 * Ogni nodo locale si colleghera' a un javaspace su localhost e rendera' accessibile il javaspace all'esterno con un indirizzo remoto
 * Il fatto di essere connessi o meno a una federazione e' trasparente all'utente
 * 
 * @author Vincenzo Frascino
 * @author Nicolas Tagliani 
 *
 */
@SuppressWarnings("serial")
public class NodoLocale implements NodoLocaleInterface , NodoRemotoInterface, Serializable {


	/**
	 * Indirizzo di default del Federation Service
	 */
	private String federationServiceAddress = "localhost";
	
	/**
	 * Riferimento all'javaspace locale
	 */
	private JavaSpace js;
	
	/**
	 * Riferimento al transaction manager
	 */
	private TransactionManager trManager;
	/**
	 * riferimento alla federazione remota
	 */
	private FederazioneInterface f = null;
	/**
	 * riferimento al federation service remoto
	 */
	private FederationServiceInterface remFedService = null;
	
	/**
	 * Valore temporaneo di ritorno per una read o take bloccanti
	 */
	private Entry result = null;
	
	/**
	 * Valore temporaneo delle eccezioni di ritorno da una read o una take bloccanti
	 */
	private Exception resultException = null;

	/**
	 * Status in cui si trova il nodo locale:
	 * 0  = bloccato e in attesa di risultati
	 * -1 = eccezione remota
	 * 1  = risultato remoto
	 */
	private int result_status = 0;
	
	/**
	 * Indirizzo pubblico del javaspace locale
	 */
	private String jSpaceRemoteAddress;
	
	/**
	 * Flag per l'attivazione dei messaggi di debug
	 */
	private boolean debug = false;

	/**
	 * Costruttore della classe nodo locale
	 * 
	 * @param externalAddress L'indirizzo pubblico da cui sar� raggiungibile il javaspace
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws RemoteException
	 */
	public NodoLocale(String externalAddress) throws IOException, ClassNotFoundException, RemoteException{
		
		LookupLocator l = new LookupLocator("jini://localhost");
		ServiceRegistrar r = l.getRegistrar();
		js = (JavaSpace)r.lookup(new ServiceTemplate(null, new Class[]{ JavaSpace.class }, null));
		jSpaceRemoteAddress = externalAddress;

//		Entry[] serverAttributes = new Entry[1];
//        serverAttributes[0] = new Name ("TransactionManager");
//        ServiceTemplate template = new ServiceTemplate (null, new Class[]{TransactionManager.class} , null);
//        trManager = (TransactionManager) r.lookup (template);
        
	}

	/**
	 * Cerca se esiste una federazione con nome "nome"
	 * 
	 * @param nome Il nome della federazione da cercare
	 * @return true se trova la federazione o false se non esiste la federazione o se accade qualche errore sul server
	 */
	public boolean cercaFederazione(String nome) {
		try{
			if (remFedService == null){
				remFedService = (FederationServiceInterface)Naming.lookup("rmi://"+federationServiceAddress+"/FederationService");
			}

			return remFedService.cercaFederazione(nome);
		} catch (RemoteException e){
			return false;
		} catch (NotBoundException e){
			return false;
		}
		catch (MalformedURLException e){
			return false;
		}
	}

	/**
	 * Crea una nuova federazione con nome nome
	 * 
	 * @param nome Il nome della federazione da creare
	 * @return true se la federazione � stata creata o false se � capitato qualche problema che non ha permesso di creare la federazione
	 */
	public boolean creaFederazione(String nome) {
		try{
			if (remFedService == null){
				remFedService = (FederationServiceInterface)Naming.lookup("rmi://"+federationServiceAddress+"/FederationService");
			}

			remFedService.creaFederazione(nome);
			return true;
		} catch (RemoteException e){
			return false;
		}catch (NotBoundException e){
			return false;
		}
		catch (MalformedURLException e){
			return false;
		}
	}

	
	/**
	 * Aggiunge il nodo locale a una federazione specificandone il nome
	 * 
	 * @param nome Il nome della federazione a cui collegarsi
	 * @return true se � andato tutto bene o false se � capitato qualche problema che ha impedito il collegamento con la federazione
	 */
	public boolean join(String nome) {
		try{
			if (remFedService == null){
				remFedService = (FederationServiceInterface)Naming.lookup("rmi://"+federationServiceAddress+"/FederationService");
			}
			UnicastRemoteObject.exportObject(this,0);
			if (debug) System.out.println("hash dell'oggetto locale: "+this.hashCode());
			f = remFedService.join((NodoRemotoInterface)this, nome);
			return true;
		} catch (RemoteException e){
			return false;
		}catch (NotBoundException e){
			return false;
		}
		catch (MalformedURLException e){
			return false;
		}
	}

	/**
	 * Lascia una federazione a cui ci si era precedentemente connessi.
	 * 
	 * @return true se abbiamo lasciato con successo la federazione false altrimenti. Se non siamo connessi a nessuna federazione Leave restituisce comunque il valore true
	 */
	public boolean leave() {
		if (f != null){
			try{
				if (f.leave(this)){
					f = null;
					return true;
				}
			}catch(RemoteException e){
				return false;
			}
		}
		return true;
	}
	
	
	/**
	 * Getter dell'indirizzo remodo del Federation Service
	 * 
	 * @return L'indirizzo remodo del Federation Service
	 */
	public String getFederationServiceAddress() {
		return federationServiceAddress;
	}

	/**
	 * Setter dell'indirizzo remoto del Federation service
	 * 
	 * @param federationServiceAddress L'indirizzo remoto del Federation Service
	 */
	public void setFederationServiceAddress(String federationServiceAddress) {
		this.federationServiceAddress = federationServiceAddress;
	}
	
	/**
	 * Getter dell'indirizzo remoto a cui � raggiungibile il javaspace locale
	 * Questo metodo � tipicamente richiamato da remoto dalla federazione
	 * 
	 * @return L'indirizzo remoto del javaspace locale
	 */
	public String getJavaSpaceAddress() throws RemoteException {
		return jSpaceRemoteAddress;
	}
	
	/**
	 * Read bloccante da tuple space
	 * 
	 * @param e Entry da leggere
	 * @param t transazione associata alla lettura
	 * @param l tempo di lease per questa lettura
	 * @return L'Entry letta
	 * @throws Exception  
	 */
	public Entry read(Entry e, Transaction t, long l) throws Exception {
		if (f!=null){
//			Entry ret = js.readIfExists(e,t,l);
//			if (ret != null){
//				return ret;
//			}
//			else{
				f.read(this, e, t, l);
				return getResult();
//			}
		}
		return js.read(e,t,l);
	}

	/**
	 * Read non bloccante da tuple space
	 * 
	 * @param e Entry da leggere
	 * @param t transazione associata alla lettura
	 * @param l tempo di lease per questa lettura
	 * @return L'Entry letta
	 * @throws Exception 
	 */
	public Entry readIfExists(Entry e, Transaction t, long l) throws Exception {
		if (f!=null){
			return f.readIfExists(this, e, t, l);
		}
		return js.readIfExists(e,t,l);
	}

	/**
	 * Take bloccante da tuple space
	 * 
	 * @param e Entry da ottenere
	 * @param t transazione associata alla take
	 * @param l tempo di lease per questa take
	 * @return L'Entry ottenuta
	 * @throws Exception  
	 */
	public Entry take(Entry e, Transaction t, long l) throws Exception {
		
		if (f!=null){
//			Entry ret = null;
//			Entry ret = js.takeIfExists(e,t,l);
//
//			if (ret != null){
//				return ret;
//			}
//			else{
				if (debug) System.out.println("Eseguo la take sulla federazione");
				if (debug) System.out.println("Take da parte di :"+this.hashCode());
				f.take(this, e, t, l);
				if (debug) System.out.println("Fatto");
				return getResult();
			}
//		}

		return js.take(e,t,l);
	}
	/**
	 * Take non  bloccante da tuple space
	 * 
	 * @param e Entry da ottenere
	 * @param t transazione associata alla take
	 * @param l tempo di lease per questa take
	 * @return L'Entry ottenuta
	 * @throws Exception  
	 */
	public Entry takeIfExists(Entry e, Transaction t, long l) throws Exception {
		if (f!=null){
			return f.takeIfExists(this, e, t, l);
		}
		return js.takeIfExists(e,t,l);
	}

	/**
	 * Scrittura nel tuple space locale
	 * 
	 * @param e Entry da scrivere
	 * @param t transazione associata alla scrittura
	 * @param l tempo di lease per questa scrittura
	 * @return La Lease associata alla scrittura
	 * @throws Exception  
	 */
	public Lease write(Entry e, Transaction t, long l) throws Exception {

//		if (t == null) {
//		Created ctx = TransactionFactory.create(trManager, l);
//		Lease ret = js.write(e,ctx.transaction,l);
//		ctx.transaction.commit();
//		return ret;
//		}
//		else 
			return js.write(e,t,l);

	}

	/**
	 * Lettura non bloccante da remoto effettuata dalla federazione
	 *
	 * @param e Entry da leggere
	 * @param t transazione associata alla lettura
	 * @param l tempo di lease per questa lettura
	 * @return L'Entry letta
	 * @throws Exception  
	 * 
	 */
	public Entry remoteReadIfExists(Entry e, Transaction t, long l)
	throws RemoteException {
		try {
			return js.readIfExists(e,t,l);
		} catch (UnusableEntryException e1) {
			throw new RemoteException(e1.getMessage());
		} catch (TransactionException e1) {
			throw new RemoteException(e1.getMessage());
		} catch (InterruptedException e1) {
			throw new RemoteException(e1.getMessage());
		}
	}


	/**
	 * Take non bloccante da remoto effettuata dalla federazione
	 *
	 * @param e Entry da ottenere
	 * @param t transazione associata alla take
	 * @param l tempo di lease per questa take
	 * @return L'Entry ottenuta
	 * @throws Exception  
	 * 
	 */
	public Entry remoteTakeIfExists(Entry e, Transaction t, long l)
	throws RemoteException {
		try {
			return js.takeIfExists(e,t,l);
		} catch (UnusableEntryException e1) {
			throw new RemoteException(e1.getMessage());
		} catch (TransactionException e1) {
			throw new RemoteException(e1.getMessage());
		} catch (InterruptedException e1) {
			throw new RemoteException(e1.getMessage());
		}
	}




	private Entry getResult() throws Exception {
		if (debug) System.out.println("NodoLocale.getResult()");
		
		while (result_status == 0){
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (result_status == 1){
			result_status = 0;
			return result;
		}
		else {
			result_status = 0;
			throw new Exception(resultException);
		}
		

		
	}
	
	/**
	 * Questo metodo tipicamente richiamato dalla federazione notifica l'ottenimento di un qualche risultato al nodo locale e sblocca lo stato del nodo durante read e take bloccanti
	 * 
	 * @param e L'entry ottenuta da remoto
	 * 
	 */
	public void putResult(Entry e) throws RemoteException{
		if (debug) System.out.println("NodoLocale.putResult()");
		result = e;
		result_status = 1;
	}

	/**
	 * Questo metodo tipicamente richiamato dalla federazione notifica l'accadimento di qualche eccezione sul nodo remoto e sblocca lo stato del nodo durante read e take bloccanti
	 * 
	 * @param e L'eccezione accaduta sul nodo remoto
	 * 
	 */
	public void throwException(Exception e) throws RemoteException {
		this.resultException = e;
		result_status  = -1;
		
	}
/*
	public void abort(TransactionManager arg0, long arg1) throws UnknownTransactionException, RemoteException {
		System.out.println( "Write aborted!!" );

		
	}

	public void commit(TransactionManager arg0, long arg1) throws UnknownTransactionException, RemoteException {
		System.out.println("Committed");
		
	}

	public int prepare(TransactionManager arg0, long arg1) throws UnknownTransactionException, RemoteException {
		return PREPARED;
	}

	public int prepareAndCommit(TransactionManager arg0, long arg1) throws UnknownTransactionException, RemoteException {
		int result = prepare( arg0, arg1 );
	      if ( result == PREPARED ) {
	         commit(arg0, arg1);
	         result = COMMITTED;
	      }
	      return result;
	}

*/
}
