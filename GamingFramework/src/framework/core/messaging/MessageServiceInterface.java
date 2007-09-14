package framework.core.messaging;

import java.rmi.Remote;
import java.rmi.RemoteException;
/**
 * Interfaccia Remota Message Service
 * 
 * @author Vincenzo Frascino
 *
 */
public interface MessageServiceInterface extends Remote{

	public void append(String s) throws RemoteException;
	
	public String get() throws RemoteException;
	
}
