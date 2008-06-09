/**
 * Package che contiene le classi per il progetto MyContacts che finiranno sul telefono.
 * Magari un domani scrivo qualcosa di meglio
 */
package android.client;

import java.io.IOException;
import java.net.UnknownHostException;
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
	 * @param mobile il numero di cellulare
	 * @param home il numero di casa
	 * @param work il numero del lavoro
	 * @param mail l'indirizzo email
	 * @param im l'account su qualche istant messenger
	 * @param x_position la coordinata x della posizione attuale
	 * @param y_position la coordinata y della posizione attuale
	 * @return true se la login ha avuto successo false altrimenti
	 */
	public boolean register(String uname, String pwd, String mobile, String home, String work, String mail, String im, double x_position, double y_position);
	
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
	 * @return La lista degli amici dell'utente uname
	 */
	public Vector<String> getFriends();
	
	/**
	 * Aggiorna la posizione del dispositivo android
	 * 
	 * @param position la posizione da aggiornare
	 * @return true se la posizione è aggiornata con successo false altrimenti
	 */
	public boolean updatePosition(double x_position, double y_position);	
	
	/**
	 * Aggiunge un amico alla lista dell'utente uname
	 * 
	 * @param friendName l'amico da aggiungere alla lista di uname
	 * @return true se la addfriend ha avuto successo false altrimentis
	 */
	public boolean addFriend(String friendName);
	
	/**
	 * Riceve la lista degli utenti in attesa di autorizzazione
	 * 
	 * @return La lista degli utenti in attesa di autorizzazione
	 */
	public Vector<String> pendingFriends();
	
	/**
	 * Accetta un amico nella propria lista di amici
	 * 
	 * @param friendName l'amico che viene accettato
	 */
	public void acceptFriend(String friendName);

	/**
	 * Rifiuta un amico dalla lista degli amici
	 * 
	 * @param friendName l'amico che viene rifiutato
	 */
	public void denyFriend( String friendName);
	
	/**
	 * Restituisce i dettagli di un amico
	 * 
	 * @param friend Lo username dell'amico di cui si vogliono conoscere i dettagli
	 * @return Le informazioni dell'amico
	 */
	public UserInterface getUserDetails(String friend);
	
	/**
	 * Esegue una connessione al server specificato sulla porta 4444
	 * 
	 * @param server Indirizzo remoto del server
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public void connect(String server) throws UnknownHostException, IOException;
	

}
