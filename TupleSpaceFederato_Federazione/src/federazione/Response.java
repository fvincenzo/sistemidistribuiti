package federazione;

import tuplespace.NodoRemotoInterface;
import net.jini.core.entry.Entry;

/**
 * Interfaccia che mostra quali metodi devono essere  implementati dal response listener.
 * Il response listener si occupa di ricevere notifiche da ReadRequest e TakeRequest
 * 
 * @author Vincenzo Frascino
 * @author Nicolas Tagliani
 *
 */
public interface Response {
	
	/**
	 * Notifica della ricezione di un'entry
	 * 
	 * @param source Il nodo remoto che deve essere notificato
	 * @param e L'entry da notificare
	 */
	public void response(NodoRemotoInterface source, Entry e);
	
	/**
	 * Notifica di un'eccezione
	 * 
	 * @param source Il nodo a cui deve essere notificata l'eccezione
	 * @param e L'eccezione da notificare
	 */
	public void throwException(NodoRemotoInterface source, Exception e);
}
