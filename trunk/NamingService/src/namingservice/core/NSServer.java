package namingservice.core;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import namingservice.core.node.*;

public class NSServer {

	private Server s;
	private static Registry r;

	public NSServer(String nome, Node father) {
		try{
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
//				if (nome.equals("root")==true)
				s = new Server(nome, InetAddress.getLocalHost().getHostAddress(),nome,father);
//				else {
//				s = new Server(nome, InetAddress.getLocalHost().getHostAddress(),nome,father);
//				}

				r.rebind(nome, s);
				System.out.println("Started Server:"+InetAddress.getLocalHost().getHostAddress()+" "+nome);


			}

			catch (RemoteException exx){
				System.out.println("Errore durante la creazione del server!\n");
			}
		}catch(UnknownHostException e){
			System.out.println("Impossibile identificare l'hostname");
		}
	}

}
