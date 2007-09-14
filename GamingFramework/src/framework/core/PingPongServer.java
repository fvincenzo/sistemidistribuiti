package framework.core;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import framework.core.logic.ServerPong;
import framework.core.messaging.*;

public class PingPongServer extends UnicastRemoteObject implements PingPongServerInterface{
	
	public static final long serialVersionUID = 0;

	private PingPongService ms; 
	static private PingPongServer mms;
	
	private PingPongServer() throws RemoteException {
		ms = PingPongService.getInstance();
		new Thread(new ServerPong(ms)).start();
	}
	
	public static PingPongServer init() {
		
		if (mms == null) {
			try {
				mms = new PingPongServer();
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
	public PingPongServiceInterface getInstance() throws RemoteException {
		// TODO Auto-generated method stub
		return ms;
	}
	
}