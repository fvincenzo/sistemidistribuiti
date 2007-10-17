package framework.core;

import java.rmi.Remote;
import java.rmi.RemoteException;

import framework.core.messaging.MessageServiceInterface;

/**
 * Interfaccia del Message Service
 * 
 * @author Vincenzo Frascino
 *
 */
public interface MessageServerInterface extends Remote{

	public MessageServiceInterface getInstance() throws RemoteException;
	
}
