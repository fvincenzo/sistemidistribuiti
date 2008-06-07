package federationservice;


import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import federazione.Federazione;
import federazione.FederazioneInterface;
import tuplespace.NodoRemotoInterface;

/**
 * Classe che implementa il Federation Service come servizio remoto accessibile tramite rmi.
 * Non c'e' bisogno di far partire l'rmi registry come servizio a parte in quanto lo lancia direttamente questo programma
 * 
 * 
 * @author Vincenzo Frascino
 * @author Nicolas Tagliani
 *
 */
@SuppressWarnings("serial")
public class FederationService extends UnicastRemoteObject implements FederationServiceInterface {

	/**
	 * Riferimento al rmiregistry
	 */
	private static Registry r;
	/**
	 * Mappa che associa ogni federazione al suo nome
	 */
	private Map<String, Federazione> federazioni = new HashMap<String, Federazione>();

	/**
	 * Metodo main che si occupa di istanziare un Federation Service e di renderlo accessibile tramite rmi
	 * 
	 */
	public static void main(String[] args) {
		
		if (System.getSecurityManager() == null) {
		    System.setSecurityManager(new SecurityManager());
		}
		/*
		 * Faccio partire l'rmi registry
		 */
		try{
			r = LocateRegistry.createRegistry(1099);

		}catch (RemoteException e){
			try {
				r = LocateRegistry.getRegistry(1099);
			}
			catch (RemoteException ex){
				System.out.println("Impossibile avviare rmiregistry");
			}
		}
		/*
		 * Installo il server
		 */
		try {
			FederationService fedService = new FederationService();
			r.rebind("FederationService", fedService);
			System.out.println("FederationService caricato correttamente");
		}

		catch (RemoteException exx){
			System.out.println("Errore durante la creazione del server!\n");
		}

	}

	/**
	 * Costruttore vuoto necessario per RMI
	 * @throws RemoteException
	 */
	public FederationService() throws RemoteException{

	}
	
	/**
	 * Cerca la federazione tramite il nome
	 * 
	 * @return true se la federazione e' presente false altrimenti
	 * @throws RemoteException
	 * 
	 */
	public boolean cercaFederazione(String nome) throws RemoteException {
		return federazioni.containsKey(nome);
	}

	/**
	 * Crea una federzione chiamata nome
	 * 
	 * @param nome il nome della federazione da creare
	 * @throws RemoteException
	 */
	public void creaFederazione(String nome) throws RemoteException {
		if (!federazioni.containsKey(nome)){
			Federazione f = new Federazione();
			federazioni.put(nome, f);
		}
	}

	/**
	 * Ottiene l'insieme delle federazioni presenti
	 * 
	 * @return L'insieme delle federazioni presenti come Set di String
	 * @throws RemoteException
	 */
	public Set<String> federazioniPresenti() throws RemoteException {
		return federazioni.keySet();
	}
	
	/**
	 * Effettua il join di un NodoLocale a una federazione specificata tramite il nome
	 * 
	 * @param n Riferimento al nodo locale sotto forma di NodoRemotoInterface
	 * @param nome nome della federazione a cui collegarsi
	 * @return il proxy della Federazione su cui poi eseguire le varie operazioni
	 * @throws RemoteException
	 */
	public FederazioneInterface join(NodoRemotoInterface n, String nome) throws RemoteException {
		Federazione f = federazioni.get(nome);
		f.join(n);
		return f;
	}



}
