package namingservice.core;

import java.net.InetAddress;
import java.rmi.Naming;
import namingservice.core.node.*;

public class NSServer {

	private Server s;
	
	public NSServer(String nome, Node father) {
		
		try {
			
//			if (nome.equals("root")==true)
				s = new Server(nome, InetAddress.getLocalHost().getHostAddress(),nome,father);
//			else {
//				s = new Server(nome, InetAddress.getLocalHost().getHostAddress(),nome,father);
//			}
				
			Naming.bind(nome, s);
			System.out.println("Started Server:"+InetAddress.getLocalHost().getHostAddress()+" "+nome);
		
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
}
