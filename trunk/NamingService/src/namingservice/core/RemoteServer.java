package namingservice.core;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;

import namingservice.core.node.Node;

/**
 * Interfaccia RemoteServer è un'interfaccia remota che si occupa di esportare i metodi utilizzati dal servizio di naming
 * 
 * @author Nicolas Tagliani e Vincenzo Frascino
 *
 */
public interface RemoteServer extends Remote {
	
	public String sum(String a, String b) throws RemoteException;
	public String add(String a, String ip, String Info) throws RemoteException;
	public Vector<String> getlist() throws RemoteException;
	public String request(String Name) throws RemoteException;
	public String exec(String s) throws RemoteException;
	public Node getReference() throws RemoteException;
	public boolean synch(String ServerName, Node root)throws RemoteException;
	public String Info() throws RemoteException;
	public String find(String Name) throws RemoteException;
	public void updateNode(String ServerName, Node root)throws RemoteException;
	
}
