package framework.core.messaging;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

import framework.core.GameServer;
import framework.core.fs.MessageNode;

/**
 * Servizio usato dal server per scambiare messaggi coi client
 *  
 * @author Vincenzo Frascino
 *
 */

public class MessageService extends UnicastRemoteObject implements MessageServiceInterface{
	
	public static final long serialVersionUID = 0;

	private Vector<String> messageBuffer;
	
	private String firstElem;
	
	private GameServer game;
	
	static private MessageService p = null;
	private String s; 
	
	private MessageService() throws RemoteException{
		
		s = "";
		messageBuffer = new Vector();
		firstElem = null;
		game = GameServer.getInstance();
			
	}
	
	public static MessageService getInstance() throws RemoteException {
		
		if(p == null) {
			
			p = new MessageService();
			
		} else {
			
			//Do nothing
			
		}
		
		return p;
		
	}
	
	public synchronized void append(String s) throws RemoteException {
		
		if(!s.equals("")) {
			int a;
			
			this.s = s;
			a = s.indexOf(".");
			game.getRoot().find("/users/"+s.substring(0,a)+"/messages").addChild(new MessageNode(s.substring(a+1)));
			game.getRoot().find("/server/home/"+s.substring(0,a)+"/messages").addChild(new MessageNode(s.substring(a+1)));
			messageBuffer.add(s.substring(a+1));
			//System.out.println("Valore di s:"+this.s+"\n");
		}
	}
	
	public synchronized String get() throws RemoteException{
		
		if(!messageBuffer.isEmpty()) {
			firstElem = messageBuffer.firstElement();
			messageBuffer.remove(messageBuffer.firstElement());
		} else {
			firstElem = null;
		}
		
		return firstElem;
		
	}

}
