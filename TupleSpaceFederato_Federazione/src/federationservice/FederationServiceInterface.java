package federationservice;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

import federazione.FederazioneInterface;

import tuplespace.NodoRemotoInterface;

/**
 * Interfaccia remota implementata dal Federation Service
 * Contiene le operazioni effettuabili sul federation service.
 * Queste operazioni non saranno comunque visibili all'utente finale in quanto filtrate dall'oggetto NodoLocale
 * 
 * @author Vincenzo Frascino
 * @author Nicolas Tagliani
 *
 */
public interface FederationServiceInterface extends Remote {
	
	/**
	 * Crea una nuova federazione con nome nome
	 * 
	 * @param nome Il nome della federazione da creare
	 * @throws RemoteException
	 */
	public void creaFederazione(String nome) throws RemoteException;
	
	/**
	 * Cerca una federazione dato il nome
	 * 
	 * @param nome Il nome della federazione da cercare
	 * @return true se trova la federazione con quel nome false antrimenti
	 * @throws RemoteException
	 */
	public boolean cercaFederazione(String nome) throws RemoteException;
	
	/**
	 * Restituisce tutti i nomi delle federazioni presenti nel sistema come un insieme di stringhe
	 * 
	 * @return L'insieme di stringhe contenente tutti i nomi delle federazioni presenti
	 * @throws RemoteException
	 */
	public Set<String> federazioniPresenti() throws RemoteException;
	
	/**
	 * Restituisce una federazione a cui ci si è allacciati
	 * 
	 * @param n Il proprio identificativo sotto forma di NodoRemotoInterface per far si che la federazione possa invocare delle operazioni sui nodi locali
	 * @param nome Il nome della federazione a cui allacciarsi
	 * @return Il Proxy della federazione su cui poter effettuare tutte le operazioni necessarie
	 * @throws RemoteException
	 */
	public FederazioneInterface join(NodoRemotoInterface n, String nome) throws RemoteException;
	
}
