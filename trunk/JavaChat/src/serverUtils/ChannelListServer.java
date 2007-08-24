/*
 * Created on Jan 5, 2007
 *
 */
package serverUtils;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
import java.util.Set;

public class ChannelListServer extends UnicastRemoteObject implements ChannelListAdmin {

    private Set<String> list = null;
    
    public static void main(String[] args) throws Exception{
        try{
            ChannelListAdmin server = new ChannelListServer();
            Naming.rebind("ChannelList", server);
            System.out.println("Server caricato correttamente");
        }catch (MalformedURLException ex){ 
            System.out.println("URL non valido!");
        }
        catch (RemoteException e){
            System.out.println("Errore durante la creazione del server!\n"+e.getLocalizedMessage());
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
//        new String[1];
        return list.toArray(a);
    }

}
