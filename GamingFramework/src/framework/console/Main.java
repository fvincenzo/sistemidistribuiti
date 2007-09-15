package framework.console;

import framework.core.*;
import framework.core.client.*;
import framework.core.fs.Shell;
//import arena.games.random.*;
/**
 * 
 * @author Vincenzo Frascino
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
/**
 * Arena console: Versione testuale del client arena.
 */
public class Main {
	private static Shell shell = null;
	private static TextModeClient client = null;
	/**
	 * Entry point dell'applicazione.
	 */
	public static void main(String[] args) {
		// Crea il client utilizzato per accedere ai servizi di Arena
		client = new TextModeClient();
		// Tiriamocela un pò...
		printHeader();
		boolean exit = false;
		do {
			String status = client.getClient().getUsername() + "@" + client.getClient().getHostname();
			if(shell != null) status = status + ":" + shell.getSelectedNode().fullname() + "/"; 
			String s = client.onConsoleInput(status + ">");
			if(s.equals("exit")) {
				if(shell != null) {
					GameServer.getInstance().fini();
				}
				exit = true;
			} else if(s.equals("run server")) {
				runInternalServer();
			} else if(s.equals("?")) {
				printHelp();
			} else {
				if(!client.getClient().eval(s)) {
					if(shell != null) {
						String res = shell.eval(s);
						if(res != null) {
							System.out.println(res);
						} 
					} else System.out.println("Comando sconosciuto: " + s);
				} 
			}
		} while(!exit);
		System.exit(0);
	}
	/**
	 * Avvia una sessione interna del server Arena e connette ad
	 * esso il client.
	 */
	public static void runInternalServer() {
		// Effettua la connessione ad un server interno per
		// effettuare prove locali senza RMI.
		GameServer as = GameServer.getInstance();
		// Installa il gestore di log sul server.
		as.attachLogHandler(client.getClient());
		// Crea un gioco 'fittizio' utilizzato per simulare la
		// creazione dei tornei.
		//Game g = new RandomGame();
		//Game f = new Random2Game();
		//as.getRoot().find("/games").addChild(g, "random");
		//as.getRoot().find("/games").addChild(f, "random2");
		GameConfiguration cfg = new GameConfiguration();
		cfg.port = 1099;
		cfg.workspace = "/home/gabrielknight/workspace/GamingFramework";
		cfg.dbUrl = "localhost:3306/arena";
		// Meglio non usare l'utente root ma un utente fatto apposta per 
		// il database. Ricordati di garntirgli tutti i privilegi sul db arena
        cfg.dbUser = "provauser";
		cfg.dbPassword = "prova";
		if(as.init(cfg)) {
			shell = new Shell();
			shell.setSelectedNode(as.getRoot());
		} else {
			// L'avvio del server � fallito, rimetti tutto come prima.
			//g.remove(); 
		}
	}
	/**
	 * Contiene l'header della shell.
	 *
	 */
	public static void printHeader() {
		System.out.print("\n\n..::  ARENA ::..\n" +
				         "..::  Progetto di laboratorio di Ingegneria del Software\n" +
						 "..::  Politecnico di Milano 2004\n" +
						 "Arena Console\n" + 
						 "Per ottenere un elenco base dei comandi della console digitare ?\n");
	}
	/**
	 * Contiene l'output del comando help.
	 *
	 */
	public static void printHelp() {
		System.out.print("Arena Console :: Comandi principali\n" + 
						 " help\n" +
						 "  Visualizza un elenco completo dei comandi del client.\n" + 
						 " run [servizio]\n" +
						 "  Avvia un servizio supportato dalla console. I servizi disponibili sono:\n" +
						 "   - server\n" +
						 "      Crea inizializza ed avvia un'istanza del server di Arena.\n" +
						 " connect [host]\n" +
						 "  Tenta la connessione al server Arena identificato da [host].\n" +
						 "  Host pu� essere un indirizzo IP, un nome di host o la parola chiave 'internal'" +
						 "  se si desidera effettuare la connessione al server Arena interno avviato\n" + 
						 "  attraverso il comando 'run server'\n" + 
						 " exit\n" +
						 "  Chiude la console\n"); 
	}
}