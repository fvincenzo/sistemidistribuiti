/*
 * Created on 24/mag/07
 * 
 */
package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote{
    public String prova() throws RemoteException;
}