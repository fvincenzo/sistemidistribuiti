package framework.core.client;

import java.rmi.*;
import java.util.*;
import java.lang.reflect.*;
import java.rmi.server.*;

import framework.core.client.transfer.TransferClient;
import framework.core.client.transfer.TransferServer;
import framework.core.fs.ParsedCmd;
import framework.core.fs.Shell;
import framework.core.logic.LogHandler;
import framework.core.logic.Tournament;
import framework.core.messaging.ServerMessage;
import framework.core.usermanager.LoginServer;
import framework.core.usermanager.UserClient;
import framework.core.usermanager.UserServer;
/**
 * @author Vincenzo Frascino
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class Client implements LogHandler {
	private ClientCore core;
	private ClientConsole console;
    private ClientUI    ui;
	/**
	* Stringa identificativa per client non connesso.
	*/
	public static final String CONNECTION_OFFLINE = "offline";
	/**
	 * Costruttore della classe ClientInterface
	 */
	public Client(ClientUI ui) {
		try {
                    core = new ClientCore();
                    console = new ClientConsole();
                    core.client = this;
                    console.client = this;
                    this.ui = ui;
		} catch(Exception e) {
			
		}
	}
        /**
         * Restituisce l'interfaccia ClientUI che rappresenta la UI associata al client.
         * @return Un'istanza di un oggetto che implementa ClientUI.
         */
        public ClientUI getUI() {
            return ui;
        }
	/**
	 * Esegue un comando supportato dalla console.
	 * @param cmd Il comando da eseguire.
	 * @return true se un comando con il nome specificato � stato
	 * trovato ed eseguito correttamente, false altrimenti.
	 */
	public boolean eval(String cmd) {
		return console.eval(cmd);
	}
	/**
	 * Registra un nuovo utente sul server identificato dall'host
	 * passato come parametro.
	 * @param host L'host del server sul quale gira il server
	 * @param username Il nome utente del nuovo account
	 * @param password La passwod del nuovo acocunt.
	 */
	public boolean register(String host, String username, String password) {
		boolean res = false;
		if(core.server == null) {
			try {
				LoginServer ls = (LoginServer)Naming.lookup("//" + host + "/GameServer");
				core.server = ls.register(core, username, password);
				if(core.server != null) {
					core.host = host;
					res = true;
				} 
			} catch(Exception e) {
				//TODO: Fai qualcosa..
			}
		}
		return res;
	} 
	/**
	 * Effettua il login su un server arena. 
	 * @param L'indirizzo server nella forma hostname:port
	 * al quale connettersi.
	 * @param user Il nome dell'utente che st� effettuando il login.
	 * @param password La password dell'utente.
	 * @return true se il login � terminato correttamente, false se in nome
	 * utente o la password sono errati, oppure se nessun server ha risposto
	 * dall'host specificato.
	 */
	public boolean login(String host, String username, String password) {
		boolean res = false;
		if(core.server == null) {
			try {
				LoginServer ls = (LoginServer)Naming.lookup("//" + host + "/GameServer");
				core.server = ls.login(core, username, password);
				if(core.server != null) {
					core.host = host;
					//Genero la Configurazione del Client
					ClientConfig conf = ClientConfig.getClientConfig();
					conf.username = username;
					//Avvio del Client dei Messaggi
					new Thread(new ClientMessaging()).start();
					//Avvio del Client Ping-Pong
					new Thread(new ClientPing()).start();
					//Lancio il server di FileSharing
                    new Thread(new ClientFile()).start();
					res = true;
				} 
			} catch(Exception e) {
				//TODO: Fai qualcosa..
			}
		}
		return res;
	}
	/**
	 * Effettua il logout del client.
	 */
	public void logout() {
		if(core.server != null) {
			try {
				core.server.logout();
				core.server = null;
			} catch(Exception e) {
				System.out.println("UserClient.logout: " + e.toString());
			}
		}
	}
	/**
	 * Restituisce lo stato di autenticazione del client.
	 * @return true se il client � connesso ed autenticato su  
	 * un server Arena, false altrimenti.
	 */
	public boolean isLogged() {
		if(core.server != null) return true;
		return false;
	}
	/**
	 * Restituisce il nome dell'utente autenticato. 
	 * @return Una stringa contenente il nome dell'utente.
	 * @see connect, login
	 */
	public String getUsername() {
		String res = "anonymous";
		if(isLogged()) {
			try {
				res = (String)core.server.getAccountData().get("Name");
			} catch(Exception e) {
				System.out.println("UserClient.getUserName(): " + e.toString());
			}
		}
		return res;
	}
	/**
	 * Restituisce il nome dell'host al quale � connesso
	 * il client.
	 */
	public String getHostname() {
		return core.host;
	}
	/**
	 * Restituisce un indicatore dei privilegi concessi all'utente
	 * @return true se l'utente possiede i privilegi di amministratore,
	 * false altrimenti.
	 * @throws IllegalStateException se il client non � connesso o non si �
	 * ancora autenticato
	 * @see connect, login
	 */
	public boolean isAdmin() throws IllegalStateException {
		if(isLogged()) {
			try {
				Boolean admin = (Boolean)core.server.getAccountData().get("Admin");
				return admin.booleanValue();
			} catch(Exception e) {
				System.out.println("UserClient.isAdmin(): " + e.toString());
				return false;
			}
		} else throw new IllegalStateException();
	}
	/**
	 * Restituisce un dizionario contenente un elenco di propriet� che
	 * descrivono l'account dell'utente.
	 * @see Account.
	 * @return Un'istanza di Dictionary contenente una serie di coppie 
	 * ('nome propriet�', valore) che rappresentano l'account o null se il
	 * client non si � ancora autenticato.
	 */
	public Dictionary getAccountData() {
		if(core.server != null) {
			try {
				return core.server.getAccountData();
			} catch(Exception e) {
				System.out.println("UserClient.getAccountData(): " + e.toString());
			}
		}
		return null;
	}
	/**
	 * Metodo utile a settare i dati relativi ad un account.
	 * @param data contiene i dati relativi ad un account
	 */
    public void setAccountData(Dictionary data) {
		if(core.server != null) {
			try {
				core.server.setAccountData(data);
			} catch(Exception e) {
				System.out.println("UserClient.setAccountData(): " + e.toString());
			}
		}
        }
	/**
	 * Restituisce una dizionario contenente un elenco di utenti ed una serie
	 * di attributi (Online, Admin) associato ad ogni utente.
	 * Il metodo ha comportamenti diversi se a chiamarlo � un'amministratore
	 * o un utente comune.
	 * @return Una hashtable contenente coppie (nometente, attributi)
	 */
	public Dictionary getOnlineUsers() {
		try {
			return core.server.getOnlineUsers();
		} catch(Exception e) {
			return null;
		}
	}
	/**
	 * Metodo utile ad inviare un messaggio di testo in chat.
	 * @param text contiene il testo iviato.
	 */
    public void say(String text) {
		try {
			core.server.say(text);
		} catch(Exception e) {
		}
        }
    /**
     * Metodo find per cercare file remoti
     */
    public void find(String text) {
    	//System.out.println("find invocato");
    	try {
			Dictionary hash = core.server.find(text);
			Enumeration elements = hash.elements();
			while(elements.hasMoreElements()) {
				System.out.println((String)elements.nextElement());
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
    	
    }
    /**
     * Metodo download per scaricare file remoti
     */
    public void download(String text) {
    	try {
			Dictionary files = core.server.find(text);
			Enumeration IPs = files.keys();
			TransferClient tclient = new TransferClient();
			tclient.reaquestFile((String)IPs.nextElement(), text);
		} catch (Exception e) {
			// TODO: handle exception
		}
    }
    /**
     * Metodo list per ottenere la lista dei file
     */
    public void list(String text) {
    	try {
			Dictionary filelist = core.server.listoffile(text);
			Enumeration enf = filelist.elements();
			while (enf.hasMoreElements()) {
				System.out.println(enf.nextElement());
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
    }
    /**
     * Metodo che serve ad ottenere la lista di Ip
     */
    public void myip() {
    	Vector<String> v = ClientIpList.ipList();
    	Enumeration enip = v.elements();
    	while(enip.hasMoreElements()) {
    		System.out.println(enip.nextElement());
    	}
    }
	/**
	 * Crea un nuovo torneo.
	 * @param name il nome del torneo.
	 * @param gameName il nome di un gioco associato al torneo. 
	 * @return un'istanza della classe Tournament in caso di successo,
	 * null altrimenti.
	 */
	public boolean addTournament(String name, int type, String gameName, String description) {
		if(core.server != null) {
			try {
				return core.server.addTournament(name, type, gameName, description);
			} catch(Exception e) {
				console.println(e.toString());
			}
		}
		return false;
	}
	/**
	 * Restituisce la lista dei tornei attivi associando al nome di ogni
	 * torneo il gioco utilizzato.
	 * @return
	 */
	public Dictionary getTournamentList() {
		if(core.server != null) {
			try {
				return core.server.getTournamentList();
			} catch(Exception e) {
				
			}
		}
		return null;
        }
 	/**
	 * Restituisce un dizionario contenente un elenco di propriet� che
	 * descrivono l'account dell'utente.
	 * @see Account.
	 * @return Un'istanza di Dictionary contenente una serie di coppie 
	 * ('nome propriet�', valore) che rappresentano l'account o null se il
	 * client non si � ancora autenticato.
	 */
	public Dictionary getTournamentData(String name) {
		if(core.server != null) {
			try {
				return core.server.getTournamentData(name);
			} catch(Exception e) {
			}
		}
		return null;
	}
	/**
	 * Metodo necessario per sottoscrivere un account.
	 * @param name contiene i dati dell'account da generare.
	 * @return true se l'account viene registrato con successo.
	 */
	public boolean subscribe(String name) {
		if(core.server != null) {
			try {
				return core.server.subscribe(name);
			} catch(Exception e) {
			}
		}
		return false;
	}
	/**
	 * Metodo utile ad avviare un torneo.
	 * @param tournamentName contiene il nome del torneo.
	 * @return true se iltorneo viene avviato con successo.
	 */
	public boolean start(String tournamentName) {
		if(core.server != null) {
			try {
				return core.server.start(tournamentName);
			} catch(Exception e) {
			}
		}
		return false;
	}
        //-------------------------------------------------------------------------
        //	IMPLEMENTAZIONE LOGHANDLER
        //-------------------------------------------------------------------------
        public void onLogMessage(String msg) {
            ui.onConsolePrint(msg);
        }
//--------------------------------------------------------------------------------------
// CLASSI INTERNE
//--------------------------------------------------------------------------------------
	private class ClientCore extends UnicastRemoteObject implements UserClient {
		public UserServer server;
		public String host;
		public Client client;
		/**
		 * Costruttore della classe UserClient.
		 */
		public ClientCore() throws RemoteException {
		}
		/**
		 * Restituisce un dizionario di propriet� associate al torneo
		 * specificato.
		 * @param name Il nome del torneo del quale si vogliono conoscere
		 * le propriet�
		 * @return Un dizionario contenente le propriet� del torneo in caso di successo,
		 * null se un torneo con il nome specificato non � stato trovato.
		 */
		public Dictionary getTournamentInfo(String name) {
			Hashtable res = null;
			Tournament t = null;//(Tournament)serverRoot.find("/tournaments/"+name);
			if(t != null) {
				res = new Hashtable();
				res.put("Nome", t.getName());
				res.put("Gioco", t.getGame().getName());
				String gameType = "";
				switch(t.getType()) {
					case 0:
						gameType = "Giocatore singolo";
						break;
					case 1:
						gameType = "Torneo all'italiana";
						break;
					case 2:
						gameType = "Eliminazione diretta";
						break;
				}
				res.put("Tipo di gioco", gameType);
				res.put("Gioco", t.getGame().getName());
				res.put("Descrizione", t.getDescription());
			}
			return res;
		}
		//-------------------------------------------------------------------------
		//	IMPLEMENTAZIONE USERCLIENT
		//-------------------------------------------------------------------------
		/**
		 * Espelle il client.
		 */
		public void kick() {
			server = null;
			host = Client.CONNECTION_OFFLINE;
			// Notifica l'espulsione al client.
			client.getUI().onKick();
		}
                public void send(ServerMessage msg) {
                    client.getUI().onServerMessage(msg);
                }
	}
//--------------------------------------------------------------------------------------
	private class ClientConsole {
		public Client client;
		private Hashtable cmds;
		/**
		 * Costruttore della classe UserConsole.
		 * @param cli Istanza della classe UserClient sulla
		 * quale invocare comandi.
		 * @throws NullPointerException Se cli == null.
		 */
		public ClientConsole() {
			// Inizializza la tabella dei comandi.
			initCmds();
		}
		/**
		 * Inizializza la lista dei comandi.
		 * I comandi sono metodi della classe ClientConsole che
		 * hanno come primi tre caratteri del nome la sequenza 'cmq', accettano
		 * un ParsedCmd come parametro e hanno 'void' come tipo di ritorno. I nomi
		 * dei comandi vengono associati ai metodi corrispondenti all'interno di una
		 * hashtable per velocizzare l'esecuzione del metodo eval. 
		 */
		private void initCmds() {
			cmds = new Hashtable();
			Method[] m = this.getClass().getMethods();
			int i;
			for(i=0;i<m.length;i++) {
				// TODO: aggiungi controllo signature del metodo.
				if(m[i].getName().startsWith("cmd")) {
					cmds.put(m[i].getName().substring(3).toLowerCase(), m[i]);
				}
			}
		}
		private void printDictionary(Dictionary d) {
		    Enumeration keys = d.keys();
		    while(keys.hasMoreElements()) {
			    String p = (String)keys.nextElement();
			    println(p + ": " + d.get(p));
		    }
		}
		/**
		 * Esegue un comando supportato dalla console.
		 * @param cmd Il comando da eseguire.
		 * @return true se un comando con il nome specificato � stato
		 * trovato ed eseguito correttamente, false altrimenti.
		 * @throws NullPointerException se cmd == null;
		 */
		public boolean eval(String cmd) throws NullPointerException {
			boolean res = false;
			if(cmd != null) {
				// Utilizza il parser di comandi della shell per 
				// analizzare e scomporre il comando passato come parametro.
				ParsedCmd pCmd = Shell.parseCmd(cmd);
				Method m = (Method)cmds.get(pCmd.Name);
				if(m != null) {
					Object[] args = new Object[1];
					args[0] = pCmd;
					try {
						//System.out.println("command:"+pCmd.toString());
						m.invoke(this, args);
						res = true;
					} catch(Exception e) {
						// Il comando e' ROOOOOOOOTTO!
						println("Il comando '" + cmd + "' ha generato l'eccezione " + e.toString());
						res = false;
					}
				}
			} else throw new NullPointerException();
			return res;
		}
		/**
		 * Richiede un'interazione con l'utente.
		 * @param prompt Il messaggio visualizzato all'utente.
		 * @return Una stringa contenente la risposta dell'utente.
		 */
		public String input(String prompt) {
			return client.getUI().onConsoleInput(prompt);
		}
		/**
		 * Invia una stringa di testo all'utente.
		 * @param text - la stringa da inviare all'utente.
		 */
		public void print(String text) {
			client.getUI().onConsolePrint(text);
		}
		/**
		 * Helper method, invia una stringa di testo terminata da
		 * ritorno a capo all'utente.
		 * @param text - la stringa da inviare all'utente.
		 */
		public void println(String text) {
			print(text + "\n");
		}
		//-------------------------------------------------------------------------
		// IMPLEMENTAZIONE COMANDI
		//-------------------------------------------------------------------------
		/**
		 * Elenca tutti i comandi supportati dalla console.
		 * @param cmd
		 */
		public void cmdHelp(ParsedCmd cmd) {
			println("- Arena Client Console -\nComandi supportati:");
			Enumeration keys = cmds.keys();
			while(keys.hasMoreElements()) {
				println("  " + keys.nextElement());
			}
		}
		/**
		 * Tenta la connessione all'host specificato.
		 */
		public void cmdLogin(ParsedCmd cmd) {
			if(client.login(cmd.Params[0], cmd.Params[1], cmd.Params[2])) {
				println("ok");
			} else println("Connessione fallita.");
		}
		/**
		 * Effettua il logout di un utente.
		 */
		public void cmdLogout(ParsedCmd cmd) {
			if(client.isLogged()) {
				client.logout();
				println("ok.");
			} else println("Client non autenticato.");
		}
		/**
		 * Restituisce la descrizione del torneo specificato.
		 */
		public void cmdTournament(ParsedCmd cmd) {
			if(client.isLogged()) {
			    if(cmd.Params[0].equals("add")) {
				String name = input("Nome:");
				int type = Integer.parseInt(input("Tipo [0-2]:"));
				String gameName = input("Nome del gioco:");
				String description = input("Descrizione:");
				if(client.addTournament(name, type, gameName, description)) {
				    println("ok.");
				} else {
				    println("Creazione fallita.");
				}
			    } else if(cmd.Params[0].equals("info")) {
				printDictionary(client.getTournamentData(cmd.Params[1]));
			    } else if(cmd.Params[0].equals("subscribe")) {
				if(client.subscribe(cmd.Params[1])) {
				    println("ok.");
				} else {
				    println("Iscrizione fallita.");
				}
			    } else if(cmd.Params[0].equals("start")) {
				if(client.start(cmd.Params[1])) {
				    println("ok.");
				} else {
				    println("Apertura torneo fallita.");
				}
			    }
			}
		}
		/**
		 * Registra un nuovo utente.
		 */
		public void cmdRegister(ParsedCmd cmd) {
			if(client.register(cmd.Params[0], cmd.Params[1], cmd.Params[2])) {
				println("ok.");
			} else {
				println("Registrazione fallita: Server non disponibile o account esistente.");
			}
		}
		/**
		 * Elenca gli utenti onnessi al server
		 */
		public void cmdUsers(ParsedCmd cmd) {
			if(client.isLogged()) {
				Dictionary users = client.getOnlineUsers();
				Enumeration names = users.keys();
				Enumeration attribs = users.elements();
				while(names.hasMoreElements()) {
					println((String)names.nextElement() + " " +
									   (String)attribs.nextElement());
				}
			}
		}
		/**
		 * Restituisce informazioni circa il proprio account.
		 */
		public void cmdMyAccount(ParsedCmd cmd){
			if(client.isLogged()){
				Dictionary d;
				d = client.getAccountData();
				Enumeration keys = d.keys();
				while(keys.hasMoreElements()) {
					String p = (String)keys.nextElement();
					println(p + ": " + d.get(p));
				}
			}
		}
        
		public void cmdSay(ParsedCmd cmd) {
            if(client.isLogged()) {
                client.say(cmd.Params[0]);
            }
        }
		
		public void cmdFind(ParsedCmd cmd) {
			if(client.isLogged()){
				//System.out.println("find invocato");
				client.find(cmd.Params[0]);
			}
		}
               
		public void cmdDownload(ParsedCmd cmd) {
			if(client.isLogged()){
				//System.out.println("download invocato");
				client.download(cmd.Params[0]);
			}
		}
		
		public void cmdList(ParsedCmd cmd) {
			if(client.isLogged()){
				//System.out.println("list invocato");
				client.list(cmd.Params[0]);
			}
		}
		
		public void cmdMyip(ParsedCmd cmd) {
				//System.out.println("find invocato");
				client.myip();
		}
              
	}
}
