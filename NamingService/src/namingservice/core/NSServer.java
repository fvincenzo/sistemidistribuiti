package namingservice.core;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import namingservice.core.node.*;

/**
 * Classe NSServer inizializza ed avvia il servizio di naming
 * @author  Nicolas Tagliani e Vincenzo Frascino
 */
public class NSServer {

	/**
	 * @uml.property  name="s"
	 * @uml.associationEnd  
	 */
	private Server s;
	private static Registry r;
	
	/**
	 * Costruttore NSServer si occupa di inizializzare il servizio e di avviarlo dando la possibilit� ai client di registrarsi
	 * 
	 * @param nome rappresenta il nome del servizio che si inizializza
	 * @param father reappresenta il nodo padre della rete da cui questo servizio discende nel caso si tratti del nodo root il parametro father=null
	 */
	public NSServer(String nome, NodeMap father) {
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

	public Server getReference() {
		return s;
	}
}
