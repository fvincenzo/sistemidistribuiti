package framework.core.usermanager;
import java.rmi.*;
import java.util.*;
/**
 * Interfaccia comune alle classi UserClient e UserServer, utilizzata per
 * l'invocazione dei metodi remoti.
 * Sia l'interfaccia lato client (UserClient) che quella lato server (UserServer)
 * implementano l'interfaccia UserInterface che, oltre a rappresentare il contratto
 * dei servizi offerti da UserServer a UserClient, rappresenta anche l'interfaccia 
 * RMI utilizzata per effettuare la comunicazione vera e propria tra utente e server
 * Arena.
 * 
 * @author Vincenzo Frascino
*/
public interface UserServer extends Remote {
	/**
	 * Effettua il logout dell'utente.
	 */
	public void logout() throws RemoteException;
	/**
	 * Restituisce una dizionario contenente un elenco di utenti ed una serie
	 * di attributi (Online, Admin) associato ad ogni utente.
	 * Il metodo ha comportamenti diversi se a chiamarlo � un'amministratore
	 * o un utente comune.
	 * @return Una hashtable contenente coppie (nometente, attributi)
	 */
	public Dictionary getOnlineUsers() throws RemoteException;
	/**
	 * Restituisce un dizionario contenente un elenco di propriet� che
	 * descrivono l'account dell'utente.
	 * @see Account.
	 * @return Un'istanza di Dictionary contenente una serie di coppie 
	 * ('nome propriet�', valore) che rappresentano l'account.
	 */
	public Dictionary getAccountData() throws RemoteException;
    public void setAccountData(Dictionary data) throws RemoteException;
    public void say(String text) throws RemoteException;
	public boolean addTournament(String name, int type, String gameName, String description) throws RemoteException;
	public Dictionary getTournamentList() throws RemoteException;
	public Dictionary getTournamentData(String name) throws RemoteException;
	public boolean subscribe(String name) throws RemoteException;
	public boolean start(String tournamentName) throws RemoteException;
}
