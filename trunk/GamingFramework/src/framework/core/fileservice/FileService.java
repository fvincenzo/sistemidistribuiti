package framework.core.fileservice;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

import framework.core.GameServer;
import framework.core.fs.FileNode;
import framework.core.fs.HashNode;
import framework.core.fs.UFile;
import framework.core.messaging.MessageService;

public class FileService extends UnicastRemoteObject implements FileServiceInterface {

	static private FileService p;
	
	private GameServer game;
	
	private FileService() throws RemoteException {
		
		game = GameServer.getInstance();
		
	}
	
	public static FileService getInstance() throws RemoteException {
		
		if(p == null) {
			
			p = new FileService();
			
		} else {
			
			//Do nothing
			
		}
		
		return p;
		
	}
	
	public void sendFile(String username, String IP, String filename, String filehash) throws RemoteException {
		
		//System.out.println("User:"+username);
		//System.out.println("File:"+filename);
		//System.out.println("Hash:"+filehash);
		
		game.getRoot().find("/server/fileserver").addChild(new FileNode(new UFile(username,IP,filename,filehash)));
		game.getRoot().find("/server/home/"+username+"/hashregistry").addChild(new HashNode(new UFile(username,IP,filename,filehash)));
		game.getRoot().find("/users/"+username+"/hashregistry").addChild(new HashNode(new UFile(username,IP,filename,filehash)));
		game.getRoot().find("/users/"+username+"/files").addChild(new FileNode(new UFile(username,IP,filename,filehash)));
		game.getRoot().find("/server/home/"+username+"/files").addChild(new FileNode(new UFile(username,IP,filename,filehash)));
	
	}

}
