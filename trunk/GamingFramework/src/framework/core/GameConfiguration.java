package framework.core;

/**
 * Contiene le opzioni di configurazione del server di Arena
 */
/**
 * 
 * @author Vincenzo Frascino
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GameConfiguration {
	public int port;			// La porta sulla quale il server rester� 
								// in attesa di connessioni
	
	public String workspace;    //Workspace ref
	
	public String dbUrl;		// host:port/dbname del database contenente i dati
								// del server.
	
	public String dbUser;		// Username perl'accesso al database.
	
	public String dbPassword;	// Password per l'accesso al database.
}
