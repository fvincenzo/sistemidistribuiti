package framework.core.usermanager;
import java.util.*;
import java.rmi.server.*;
import java.rmi.*;

import framework.core.GameServer;
import framework.core.fs.FileNode;
import framework.core.fs.HashNode;
import framework.core.fs.Node;
import framework.core.fs.UFile;
import framework.core.fs.UserNode;
import framework.core.logic.Game;
import framework.core.logic.Tournament;
import framework.core.messaging.ChatMessage;
import framework.core.messaging.ServerMessage;

/**
 * Implementa l'interfaccia di comunicazione con Arena, lato server.
 * Sia l'interfaccia lato client (UserClient) che quella lato server (UserServer)
 * implementano l'interfaccia UserInterface che, oltre a rappresentare il contratto
 * dei servizi offerti da UserServer a UserClient, rappresenta anche l'interfaccia 
 * RMI utilizzata per effettuare la comunicazione vera e propria tra utente e server
 * Arena.
 * @author Vincenzo Frascino
 */
public class User extends UnicastRemoteObject implements UserServer {
	private Account acc;
	private UserClient client;
	/**
	 * Costruttore della classe UserServer
	 * @param acc Account associato al client.
	 */
	public User(UserClient client, Account acc) throws RemoteException {
		// RI: acc != null, client != unll
		this.acc = acc;
		this.client = client;
	}
	/**
	 * @return L'account associato al client.
	 */	
	public Account getAccount(){
		return acc;
	}
	/**
	 * @return Un'istanza dell'interfaccia UserClient che rappresenta il
	 * client dell'utente.
	 */
	public UserClient getUserClient() {
		return client;
	}
	/**
	 * Espelle l'utente dal server.
	 */
	public void kick() {
		removeUser();
		try {
			client.kick();
		} catch(Exception e) {
			
		}
	}
        public void send(ServerMessage msg) {
		try {
        		client.send(msg);
		} catch(Exception e) {
			
		}
        }
	/**
	 * Rimuove l'utente dal server.
	 */
	private void removeUser() {
		GameServer arena = GameServer.getInstance();
		Node root = arena.getRoot();
		Node u = root.find("users/" + acc.getName());
		u.remove();
	}
	//-------------------------------------------------------------------------
	//	IMPLEMENTAZIONE USERSERVER
	//-------------------------------------------------------------------------
	/**
	 * Effettua il logout dell'utente.
	 */
	public void logout() {
		GameServer arena = GameServer.getInstance();
		removeUser();
		arena.log("Utente disconnesso: " + acc.getName());
        arena.broadcastMessage(new ServerMessage("logout", acc.getName()));
	}
	/**
	 * Restituisce una dizionario contenente un elenco di utenti ed una serie
	 * di attributi (Online, Admin) associato ad ogni utente.
	 * Il metodo ha comportamenti diversi se a chiamarlo � un'amministratore
	 * o un utente comune.
	 * @return Una hashtable contenente coppie (nometente, attributi)
	 */
	public Dictionary getOnlineUsers() {
		GameServer arena = GameServer.getInstance();
		Node root = arena.getRoot();
		Hashtable res = new Hashtable();
		Node users = root.find("/users");
		Enumeration enu = users.children();
		while(enu.hasMoreElements()) {
			String attribs = "";
                        UserNode u = (UserNode)enu.nextElement();
			Account acc = (Account)u.getUser().getAccount();
			if(acc.getAdmin()) attribs = "admin";
                        res.put(u.getName(), attribs);
		}
		return res;
	}
	/**
	 * Restituisce un dizionario contenente un elenco di propriet� che
	 * descrivono l'account dell'utente.
	 * @see Account.
	 * @return Un'istanza di Dictionary contenente una serie di coppie 
	 * ('nome propriet�', valore) che rappresentano l'account.
	 */
	public Dictionary getAccountData() {
		// NB, non servono controlli su acc perch� RI-> acc != null.
		Dictionary d = acc.getObjectData();
		d.put("Name", acc.getName());
		return d; 
	}
        public void setAccountData(Dictionary data) {
            acc.setObjectData(data);
            acc.save();
        }
	/**
	 * Crea un nuovo torneo.
	 * @param name Nome del torneo
	 * @param gameName Nome del gioco associato al torneo.
	 * @return Un'istanza di Tournament in caso di successo, null se un
	 * torneo con lo stesso nome esiste gi� o non esistono giochi con
	 * il nome specificato.
	 */
	public boolean addTournament(String name, int type, String gameName, String description) {
		Tournament t = null;
                boolean res = false;
		Node games = GameServer.getInstance().getRoot().find("/games");
		Node tournaments = GameServer.getInstance().getRoot().find("/tournaments");
		Game game = (Game)games.findChild(gameName);
		if(game != null) {
			if(tournaments.findChild(name) == null) {
				t = new Tournament(name, acc, type, game);
				t.setDescription(description);
				tournaments.addChild(t);
				t.save();
                                res = true;
			}
		}
		return res;
	}
	public Dictionary getTournamentList() {
		GameServer arena = GameServer.getInstance();
		Node root = arena.getRoot();
		Hashtable res = new Hashtable();
		Node games = root.find("/games");
		Node tournaments = root.find("/tournaments");
		Enumeration enu = games.children();
		while(enu.hasMoreElements()) {
			Vector tss = new Vector();
			Game g = (Game)enu.nextElement();
			Enumeration ts = tournaments.children();
			while(ts.hasMoreElements()) {
			    Tournament t = (Tournament)ts.nextElement();
			    if(t.getGame() == g) {
				tss.add(t.getName());
			    }
			}
			res.put(g.getName(), tss);
		}
		return res;
	}
	/**
	 * Restituisce informazioni riguardanti il torneo con il nome specificato.
	 * @param name Il nome del torneo di cui si vogliono ottenere i dati.
	 */
	public Dictionary getTournamentData(String name) {
	    Tournament t = (Tournament)GameServer.getInstance().getRoot().find("/tournaments/" + name);
	    if(t != null) {
		return t.getObjectData();
	    }
	    return null;
	 }
	 /**
         * Invia un messaggio di chat a tutti gli utenti connessi.
         */
        public void say(String text) throws RemoteException {
            GameServer.getInstance().broadcastMessage(new ChatMessage(this, text));
        }
        
	public boolean subscribe(String name) {
		Node tournaments = GameServer.getInstance().getRoot().find("/tournaments");
		Tournament t = (Tournament)tournaments.findChild(name);
		if(t != null) {
		    return t.subscribe(acc);
		}
		return false;
	}
	
	public boolean start(String tournamentName) {
		Node tournaments = GameServer.getInstance().getRoot().find("/tournaments");
		Tournament t = (Tournament)tournaments.findChild(tournamentName);
		if(t != null && t.getOwner() == acc) {
		    t.start();
		    return true;
		}
		return false;
	}
	
	public Dictionary find(String Name) throws RemoteException{
		
		Hashtable users = new Hashtable();
		String hashcode = new String();
		GameServer game = GameServer.getInstance();
		FileNode  file = (FileNode)game.getRoot().find("/server/fileserver/"+Name);
		//System.out.println(file.getUFile().getOwner());
		//users.put(String.valueOf(1), file.getUFile().getOwner());
		hashcode = file.getUFile().getHash();
		
		Enumeration en = game.getRoot().find("/users").children();
		
		while (en.hasMoreElements()) {
			//System.out.println("Sono qui!!"+hashcode);
			String username = (String)((UserNode)en.nextElement()).getUser().getAccount().getName();
			//System.out.println(username);
			Node hash = (Node) game.getRoot().find("/users/"+username+"/hashregistry");
			Enumeration enh = hash.children();
			while(enh.hasMoreElements()) {
				UFile file1 = ((HashNode)enh.nextElement()).getUFile();
				String hash1 = file1.getHash();
				//System.out.println("hash.equal :"+hash1.equals(hashcode));
				if(hash1.equals(hashcode)) {
					//System.out.println(file1.getOwner());
					users.put(file1.getIP(), file1.getOwner());
					break;
				}
			}
		}
		
		return users;
		
	}
	
	public Dictionary listoffile(String users) throws RemoteException{
		
		GameServer game = GameServer.getInstance();
		Hashtable result = new Hashtable();
		
		if(users.equals("all")) {
			Node fileserver = game.getRoot().find("/server/fileserver");
			Enumeration enf = fileserver.children();
			while(enf.hasMoreElements()) {
				FileNode f = (FileNode)enf.nextElement();
				result.put(f.getUFile().getHash(), f.getUFile().getName());
			}
		} else
		if (users.equals(null)) {
			result = null;
		} else {
			Node fileserver = game.getRoot().find("/users/"+users+"/files");
			Enumeration enf = fileserver.children();
			while(enf.hasMoreElements()) {
				FileNode f = (FileNode)enf.nextElement();
				result.put(f.getUFile().getHash(), f.getUFile().getName());
			}
		}
		
		return result;
		
	}
	
}

