package framework.core;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import framework.core.messaging.*;

public class MessageServer extends UnicastRemoteObject implements MessageServerInterface{
	
	public static final long serialVersionUID = 0;

	private MessageService ms; 
	static private MessageServer mms;
	
	private MessageServer() throws RemoteException {
		ms = MessageService.getInstance();
	}
	
	public static MessageServer init() {
		
		if (mms == null) {
			try {
				mms = new MessageServer();
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println(e.toString()+" Siamo qui!\n");
			}
		} else {
			//Do Nothing
		}
		
		return mms;
		
	}
	/**
	 * Implementazione della MessageServerInterface
	 */
	public MessageServiceInterface getInstance() throws RemoteException {
		// TODO Auto-generated method stub
		return ms;
	}
	
}
