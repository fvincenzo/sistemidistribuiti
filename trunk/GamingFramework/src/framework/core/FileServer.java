package framework.core;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import framework.core.fileservice.FileService;
import framework.core.fileservice.FileServiceInterface;
import framework.core.messaging.MessageService;

public class FileServer extends UnicastRemoteObject implements FileServerInterface {

	public static final long serialVersionUID = 0;

	private FileService fs; 
	static private FileServer ffs;
	
	private FileServer() throws RemoteException {
		fs = FileService.getInstance();
	}
	
	public static FileServer init() {
		
		if (ffs == null) {
			try {
				ffs = new FileServer();
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println(e.toString()+" Siamo qui!\n");
			}
		} else {
			//Do Nothing
		}
		
		return ffs;
		
	}
	
	public FileServiceInterface getInstance() throws RemoteException {
		// TODO Auto-generated method stub
		return fs;
	}

}
