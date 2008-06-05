package tuplespace;


import net.jini.core.entry.Entry;
import net.jini.core.lease.Lease;
import net.jini.core.transaction.Transaction;

/**
 * Questa interfaccia mostra le funzionalità che devono essere disponibili in un qualsiasi nodo locale
 * Il fatto di essere connessi o meno a una federazione è trasparente all'utente
 * 
 * @author Vincenzo Frascino
 * @author Nicolas Tagliani
 *
 */
public interface NodoLocaleInterface{
	
	/**
	 * Scrittura nel tuple space locale
	 * 
	 * @param e Entry da scrivere
	 * @param t transazione associata alla scrittura
	 * @param l tempo di lease per questa scrittura
	 * @return La Lease associata alla scrittura
	 * @throws Exception  
	 */
	public Lease write(Entry e, Transaction t, long l) throws Exception;
	
	/**
	 * Read bloccante da tuple space
	 * 
	 * @param e Entry da leggere
	 * @param t transazione associata alla lettura
	 * @param l tempo di lease per questa lettura
	 * @return L'Entry letta
	 * @throws Exception  
	 */
	public Entry read(Entry e, Transaction t, long l) throws Exception;

	/**
	 * Take bloccante da tuple space
	 * 
	 * @param e Entry da ottenere
	 * @param t transazione associata alla take
	 * @param l tempo di lease per questa take
	 * @return L'Entry ottenuta
	 * @throws Exception  
	 */
	public Entry take(Entry e, Transaction t, long l) throws Exception;
	
	/**
	 * Read non bloccante da tuple space
	 * 
	 * @param e Entry da leggere
	 * @param t transazione associata alla lettura
	 * @param l tempo di lease per questa lettura
	 * @return L'Entry letta
	 * @throws Exception 
	 */
	public Entry readIfExists(Entry e, Transaction t, long l) throws Exception;
	
	/**
	 * Take non bloccante da tuple space
	 * 
	 * @param e Entry da ottenere
	 * @param t transazione associata alla take
	 * @param l tempo di lease per questa take
	 * @return L'Entry ottenuta
	 * @throws Exception 
	 */
	public Entry takeIfExists(Entry e, Transaction t, long l) throws Exception;
	
	/**
	 * Crea una nuova federazione con nome nome
	 * 
	 * @param nome Il nome della federazione da creare
	 * @return true se la federazione è stata creata o false se è capitato qualche problema che non ha permesso di creare la federazione
	 */
	public boolean creaFederazione(String nome);
	
	/**
	 * Cerca se esiste una federazione con nome "nome"
	 * 
	 * @param nome Il nome della federazione da cercare
	 * @return true se trova la federazione o false se non esiste la federazione o se accade qualche errore sul server
	 */
	public boolean cercaFederazione( String nome);
	
	/**
	 * Aggiunge il nodo locale a una federazione specificandone il nome
	 * 
	 * @param nome Il nome della federazione a cui collegarsi
	 * @return true se è andato tutto bene o false se è capitato qualche problema che ha impedito il collegamento con la federazione
	 */
	public boolean join(String nome); 
	
	/**
	 * Lascia una federazione a cui ci si era precedentemente connessi.
	 * 
	 * @return true se abbiamo lasciato con successo la federazione false altrimenti. Se non siamo connessi a nessuna federazione Leave restituisce comunque il valore true
	 */
	public boolean leave();
	
}
