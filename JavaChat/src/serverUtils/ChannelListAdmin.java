/*
 * Created on Jan 6, 2007
 *
 */
package serverUtils;

import java.rmi.RemoteException;

public interface ChannelListAdmin extends ChannelList{
    
    public boolean addChannel(String channel) throws RemoteException;

    public boolean removeChannel(String channel) throws RemoteException;

    public boolean existsName(String s) throws RemoteException;
}
