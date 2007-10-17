/*
 * Created on 3-giu-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package framework.core.usermanager;
import java.rmi.*;

import framework.core.messaging.ServerMessage;
/**
 * @author Vincenzo Frascino
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public interface UserClient extends Remote {
	/**
	 * Espelle l'utente dal server.
	 */
	public void kick() throws RemoteException;
    public void send(ServerMessage msg) throws RemoteException;
}
