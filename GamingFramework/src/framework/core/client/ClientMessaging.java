package framework.core.client;

import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;

import framework.core.*;
import framework.core.messaging.MessageServiceInterface;
import framework.core.messaging.MessageService;

public class ClientMessaging implements Runnable{
	
	private ClientConfig conf;
	
	private String host;
	
	private MessageServerInterface p;
	
	public ClientMessaging() {
		try {
			conf = ClientConfig.getClientConfig();
			host = conf.host+":"+conf.rmiport;
			p = (MessageServerInterface) Naming.lookup("//"+host+"/MessageServer");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void run() {
		

		try {
			
			MessageServiceInterface m = (MessageServiceInterface)p.getInstance();
		
			String casa = "";
			
			while(true) {
			
				casa = m.get();
				
				if(casa != null && !casa.equals("PONG")) {
					System.out.println("Server Message:"+casa);
				}
				
			}
			
		} catch (RemoteException e) {
			// TODO: handle exception
		}
	
	}
	
}

