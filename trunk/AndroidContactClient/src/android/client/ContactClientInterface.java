/**
 * Package che contiene le classi per il progetto MyContacts che finiranno sul telefono.
 * Magari un domani scrivo qualcosa di meglio
 */
package android.client;

import java.util.Vector;

/**
 * Interfaccia da implementare per accedere alle funzionalità del MyContacts server
 * 
 * @author Nicolas Tagliani
 * @author Vincenzo Frascino
 *
 */
public interface ContactClientInterface {
	
	/**
	 * Registra un utente e lo logga in automatico
	 * 
	 * @param uname username da registrare
	 * @param pwd password dell'utente
	 * @param geo coordinate della posizione attuale
	 * @return true se la login ha avuto successo false altrimenti
	 */
	public boolean register(String uname, String pwd, String geo);
	
	/**
	 * Logga un utente nel sistema
	 * 
	 * @param uname username dell'utente
	 * @param pwd password dell'utente
	 * @return true se l'utente si logga con successo false altrimenti
	 */
	public boolean login(String uname, String pwd);
	
	/**
	 * Restituisce la lista di tutti gli utenti del sistema
	 * 
	 * @return La lista degli utenti del sistema
	 */
	public Vector<String> getUsers();
	
	/**
	 * Riceve la lista degli amici dell'utente uname
	 * 
	 * @param uname Lo username dell'utente
	 * @return La lista degli amici dell'utente uname
	 */
	public Vector<String> getFriends(String uname);
	
	/**
	 * Aggiorna la posizione del dispositivo android
	 * 
	 * @param uname lo username dell'utente che vuole aggiornare la propria posizione
	 * @param position la posizione da aggiornare
	 * @return true se la posizione è aggiornata con successo false altrimenti
	 */
	public boolean updatePosition(String uname, float x_position, float y_position);	
	
	/**
	 * Aggiunge un amico alla lista dell'utente uname
	 * 
	 * @param uname l'utente che vuole aggiungere un amico
	 * @param friendName l'amico da aggiungere alla lista di uname
	 * @return true se la addfriend ha avuto successo false altrimentis
	 */
	public boolean addFriend(String uname, String friendName);
	
	/**
	 * Riceve la lista degli utenti in attesa di autorizzazione
	 * 
	 * @param uname L'utente che ha richiesto la lista
	 * @return La lista degli utenti in attesa di autorizzazione
	 */
	public Vector<String> pendingFriends(String uname);
	
	/**
	 * Accetta un amico nella propria lista di amici
	 * 
	 * @param uname l'utente che accetta l'amico
	 * @param friendName l'amico che viene accettato
	 */
	public void acceptFriend(String uname, String friendName);

	/**
	 * Rifiuta un amico dalla lista degli amici
	 * 
	 * @param uname L'utente che rifiuta l'amico
	 * @param friendName l'amico che viene rifiutato
	 */
	public void denyFriend(String uname, String friendName);
	
	/**
	 * Restituisce i dettagli di un amico
	 * 
	 * @param friend Lo username dell'amico di cui si vogliono conoscere i dettagli
	 * @return Le informazioni dell'amico
	 */
	public UserInterface getUserDetails(String friend);
	
	

}
