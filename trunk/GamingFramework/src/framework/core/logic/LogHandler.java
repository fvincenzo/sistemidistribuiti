package framework.core.logic;

/**
 * Rappresenta l'interfaccia di un gestore di log,ovvero di un oggetto
 * in grado di accettare messaggi dall'applicazione e notificarli all'utente
 * o registrarli per permetterne la consultazione successiva.
 * @author Vincenzo Frascino
 */
public interface LogHandler {
	/**
	 * Gestisce un messaggio dell'applicazione.
	 * @param text Il messaggio da gestire.
	 */
	public void onLogMessage(String text);
}
