/*
 * Created on Jan 5, 2007
 *
 */
package serverUtils;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("serial")
public class ChannelListServer extends UnicastRemoteObject implements ChannelListAdmin {

    private Set<String> list = null;
    private static Registry r;

    public static void main(String[] args) {
	/*
	 * Faccio partire l'rmi registry
	 */
	try{
	    r = LocateRegistry.createRegistry(1099);
	
	}catch (RemoteException e){
	    try {
		r = LocateRegistry.getRegistry(1099);
	    }
	    catch (RemoteException ex){
		System.out.println("Impossibile avviare rmiregistry");
	    }
	}
	/*
	 * Installo il server
	 */
	try {
	    ChannelListAdmin server = new ChannelListServer();
	    r.rebind("ChannelList", server);
	    System.out.println("Server caricato correttamente");
	}

	catch (RemoteException exx){
	    System.out.println("Errore durante la creazione del server!\n");
	}

    }

    public ChannelListServer() throws RemoteException {
	super();
	list = new HashSet<String>();
    }

    /* (non-Javadoc)
     * @see serverUtils.ChannelList#addChannel(java.lang.String)
     */
    public synchronized boolean addChannel(String channel) throws RemoteException {
	return list.add(channel);
    }

    /* (non-Javadoc)
     * @see serverUtils.ChannelList#removeChannel(java.lang.String)
     */
    public synchronized boolean removeChannel(String channel) throws RemoteException {
	return list.remove(channel);
    }

    /* (non-Javadoc)
     * @see serverUtils.ChannelList#getAllChannels()
     */
    public synchronized String[] getAllChannels() throws RemoteException {
	String[] a = {};
//	new String[1];
	return list.toArray(a);
    }
    public synchronized boolean existsName(String n) throws RemoteException {
	return list.contains(n);
    }

}
