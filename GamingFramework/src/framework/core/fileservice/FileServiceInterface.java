package framework.core.fileservice;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;

/**
 * Servizio usato dal server per scambiare informazioni su file coi client
 *  
 * @author Vincenzo Frascino
 *
 */
public interface FileServiceInterface extends Remote {

	public void sendFile(String username, String IP, String filename, String filehash) throws RemoteException;
	
}
