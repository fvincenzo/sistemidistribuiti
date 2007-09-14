package framework.core;

import java.rmi.Remote;
import java.rmi.RemoteException;

import framework.core.messaging.*;

/**
 * Interfaccia del Message Service
 * 
 * @author Vincenzo Frascino
 *
 */
public interface PingPongServerInterface extends Remote{

	public PingPongServiceInterface getInstance() throws RemoteException;
	
}
