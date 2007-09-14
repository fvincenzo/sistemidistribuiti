package framework.core.messaging;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

/**
 * Servizio usato dal server per scambiare messaggi coi client
 *  
 * @author Vincenzo Frascino
 *
 */

public class PingPongService extends UnicastRemoteObject implements PingPongServiceInterface{
	
	public static final long serialVersionUID = 0;

	private Vector<String> messageBuffer;
	
	private String firstElem;
	
	static private PingPongService p = null;
	private String s; 
	
	private PingPongService() throws RemoteException{
		
		s = "";
		messageBuffer = new Vector();
		firstElem = null; 
			
	}
	
	public static PingPongService getInstance() throws RemoteException{
		
		if(p == null) {
			
			p = new PingPongService();
			
		} else {
			
			//Do nothing
			
		}
		
		return p;
		
	}
	
	public synchronized void append(String s) throws RemoteException{
		
		this.s = s;
		messageBuffer.add(s);
		//System.out.println("Valore di s:"+messageBuffer.firstElement()+"\n");
		
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
