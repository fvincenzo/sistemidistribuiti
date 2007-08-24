/*
 * Created on Jan 6, 2007
 *
 */
package src.serverUtils;

import java.rmi.RemoteException;

public interface ChannelListAdmin extends ChannelList{
    
    public boolean addChannel(String channel) throws RemoteException;

    public boolean removeChannel(String channel) throws RemoteException;

}
