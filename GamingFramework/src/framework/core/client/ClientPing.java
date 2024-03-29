/**
 * 
 */
package framework.core.client;

import java.rmi.Naming;
import java.rmi.RemoteException;

import framework.core.*;
import framework.core.messaging.*;

/**
 * Implementazione lato Client del Ping-Pong Protocol usa il Servizio di Messaging
 * 
 * @author Vincenzo Frascino
 *
 */
public class ClientPing implements Runnable{
	
	private ClientConfig conf;
	
	private String host;
		
	private PingPongServerInterface p;
	
	private String casa;
	
	public ClientPing() {
		try {

			conf = ClientConfig.getClientConfig();
			host = conf.host+":"+conf.rmiport;
			p = (PingPongServerInterface) Naming.lookup("//"+host+"/PingPongServer");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void run() {
	
		try {
			
			PingPongServiceInterface m = (PingPongServiceInterface)p.getInstance();
			
			while(true) {
			
					m.append(conf.username+".PING");
					//System.out.println("Client Message:PING\n");
					try {
						Thread.sleep(15000);
					} catch (Exception e) {
						// TODO: handle exception
					}
			}
			
		} catch (RemoteException e) {
			// TODO: handle exception
		}
	
	}
	
}
