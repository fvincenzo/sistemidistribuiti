/*
 * Created on Jan 5, 2007
 *
 */
package src.serverUtils;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChannelList extends Remote{

    public String[] getAllChannels() throws RemoteException;

}