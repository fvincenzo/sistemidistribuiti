package framework.core.logic;

import java.util.Iterator;
import java.util.Vector;

import framework.core.GameServer;
import framework.core.fs.Node;
import framework.core.messaging.MessageService;
import framework.core.messaging.PingPongService;
import framework.core.messaging.ServerMessage;
import framework.core.usermanager.UserStatus;

/**
 * Implementazione lato Server del Ping-Pong Protocol usa il Servizio di Messaging
 * 
 * @author Vincenzo Frascino
 *
 */
public class ServerPong implements Runnable{

	private PingPongService p;
	private MessageService q;
	private Vector<String> users;
	private Vector<UserStatus> connections;
	private GameServer arena;
	
	public ServerPong(PingPongService p) {
		
		this.p = p;
		users = new Vector();
		connections = new Vector();
		arena = GameServer.getInstance();
		try {
			q = MessageService.getInstance();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
		
	public void run() {
		
		int a = 0;
		int i = 0;
		String ping = null;
		
		while(true) {
			
			/**
			 * La variabile i rappresenta il tempo di attesa in ms
			 * Fa il polling degli utenti ogni 45 secondi
			 */
			while(i<3000) {
				
				try {
					Thread.sleep(15);
					ping = p.get();	
				} catch (Exception e) {
					// TODO: handle exception
				}
				if(ping != null) {
					//System.out.println(ping+i+"\n");
					a =  ping.indexOf(".");
					if(!users.contains(ping.substring(0, a))) {
						users.add(ping.substring(0, a));
						connections.add(new UserStatus(ping.substring(0, a), "true"));
					} else {
						int cont = 0;
						for( Iterator myx = connections.iterator(); myx.hasNext(); myx.next() ) {
							UserStatus user = connections.elementAt(cont).returnUserStatus();
							if(user.getUser().equals(ping.substring(0, a))) {
								user.setStatus("true");
								//System.out.println("element:"+ping.substring(0, a)+" status:"+user.getStatus(ping.substring(0, a))+"\n");
							}
							cont++;
						}
					}
	            try {
	            	q.append(ping.substring(0,a)+".PONG");
				} catch (Exception e) {
					// TODO: handle exception
				}  
				}
				i++;
				
			}
			
			/**
			 * Verifico le condizioni di uscita dal ciclo
			 */
			try{
				if (!users.isEmpty()) {
					int num = 0;
					
					for( Iterator my = users.iterator(); my.hasNext(); my.next() ) {
						
						if(connections.elementAt(num).getStatus(users.elementAt(num)).equals("true")) {
							connections.elementAt(num).setStatus("false");
							//System.out.println("element:"+users.elementAt(num)+" status:"+connections.elementAt(num).getStatus(users.elementAt(num))+"\n");
						} else {
							
							/**
							 * Se si arriva qui il client è off-line 
							 */
							//Rimuove l'utente dalla lista
							removeUser(users.elementAt(num));
							arena.log("Utente disconnesso: " + users.elementAt(num));
					        arena.broadcastMessage(new ServerMessage("logout", users.elementAt(num)));
					        users.removeElementAt(num);
						
						}
						
						num++;
						
					}
					
				}
			}catch (Exception e) {
				//C'è un'eccezione se si sconnette l'ultimo utente ma nn fa nulla
			}
			
			i = 0;
			
		}
		
	}
	
	/**
	 * Rimuove l'utente dal server.
	 */
	private void removeUser(String username) {
		GameServer arena = GameServer.getInstance();
		Node root = arena.getRoot();
		Node u = root.find("users/" + username);
		u.remove();
	}
	
}
