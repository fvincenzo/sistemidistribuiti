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


@SuppressWarnings("serial")
public class FederationService extends UnicastRemoteObject implements FederationServiceInterface {

	private static Registry r;
	private Map<String, Federazione> federazioni = new HashMap<String, Federazione>();

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

	public FederationService() throws RemoteException{

	}
	
	public boolean cercaFederazione(String nome) throws RemoteException {
		return federazioni.containsKey(nome);
	}

	public void creaFederazione(String nome) throws RemoteException {
//		System.out.println("FederationService.creaFederazione()");
		if (!federazioni.containsKey(nome)){
			System.out.println("Creo "+nome);
			Federazione f = new Federazione();
			federazioni.put(nome, f);
		}
	}

	public FederazioneInterface join(NodoRemotoInterface n, String nome) throws RemoteException {
//		System.out.println("FederationService.join() di "+n.hashCode());
		Federazione f = federazioni.get(nome);
		f.join(n);
		return f;
	}

	public Set<String> federazioniPresenti() throws RemoteException {
		return federazioni.keySet();
	}


}
