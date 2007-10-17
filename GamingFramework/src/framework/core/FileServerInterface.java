package framework.core;

import java.rmi.Remote;
import java.rmi.RemoteException;

import framework.core.fileservice.FileServiceInterface;
/**
 * Interfaccia del File Service
 * 
 * @author Vincenzo Frascino
 *
 */


public interface FileServerInterface extends Remote {

	public FileServiceInterface getInstance() throws RemoteException;
	
}
