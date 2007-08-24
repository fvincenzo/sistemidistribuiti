/*
 * Created on 24/mag/07
 * 
 */
package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ServerImpl extends UnicastRemoteObject implements ServerInterface {

    public ServerImpl() throws RemoteException{
        super();
    }
    
    public String prova() throws RemoteException {
        return "Funziona... e che implementazione degna di nota...";
    }

}
