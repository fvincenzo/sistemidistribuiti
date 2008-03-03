package framework.core.client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

public class ClientConfig {
	
	private static ClientConfig cfg;
	
	public String host;
	
	public String port;
	
	public String username;
	
	public String rmiport;
	
	public String received;
	
	public String sharing[] = { new String() };
	
	private ClientConfig() throws IOException{
//		DataInputStream config = new DataInputStream(new FileInputStream("client.config"));
		BufferedReader config = new BufferedReader(new FileReader("cliend.config"));
		host = config.readLine();
		//System.out.println(host);
		port = config.readLine();
		//System.out.println(port);
		rmiport = config.readLine();
		//System.out.println(rmiport);
		received = config.readLine();
		sharing[0] = config.readLine() ;
		//System.out.println(sharing[0]);
	}
	
	public static ClientConfig getClientConfig() throws IOException{
		
		if(cfg == null) {
			cfg = new ClientConfig();
			return cfg;
		} else {
			return cfg;
		}
		
	}

}
