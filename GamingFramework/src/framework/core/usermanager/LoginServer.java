package framework.core.usermanager;
import java.rmi.*;

/**
 * @author Vincenzo Frascino
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public interface LoginServer extends Remote {
	
	public static final long serialVersionUID = 0;

	/**
	 * Effettua il login di un utente.
	 * @param client Interfaccia UserClient del client che st� effettuando la
	 * richiesta di login.
	 * @param username Il nome dell'utente che st� effetuando il login.
	 * @param password La password dell'utente che st� effettuando il login.
	 * @return UN'istanza della classe UserServer che rappresenta l'utente
	 * collegato al server in caso di successo, null non � stato possibile stabilire
	 * la connessione.
	 */
	public UserServer login(UserClient client, String username, String password) throws RemoteException;
	/**
	 * Registra un nuovo giocatore.
	 */
	public UserServer register(UserClient client, String username, 
								  String password) throws RemoteException ;
}
