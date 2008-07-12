package namingservice.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.Iterator;
import java.util.Vector;
import namingservice.core.NSClient;
import namingservice.core.NSServer;
import namingservice.core.RemoteServer;

/**
 * Classe Admin Serve a fare il bootstrap della rete
 * 
 * @author Nicolas Tagliani e Vincenzo Frascino
 *
 */
public class Adminshell {

	
	@SuppressWarnings("unused")
	private static NSServer ns;
	private static NSClient nsc;
	private static RemoteServer rs;
	
	/**
	 * Metodo Main
	 * 
	 * @param args serve a passare i parametri da shell(inutilizzato al momento)
	 */
	public static void main(String[] args) {
		
		int conn = 0;
		
		InputStreamReader reader = new InputStreamReader (System.in);
		BufferedReader buf_in = new BufferedReader (reader);
		String str;
		
		try {
			
			do {
				
				System.out.print(">");
		        str = buf_in.readLine ();
		        
		        if (str.compareTo("start")==0) {
		        	
		        	ns = new NSServer("root",null);
		        	continue;
		        	
		        }
		        
		        if (str.compareTo("connect")==0) {
		        	
		        	try {
		        		System.out.print("Server Name:");
			        	nsc = new NSClient();
			        	rs = nsc.connect(buf_in.readLine(),"localhost:1099");
			        	if(rs != null) conn = 1;
					} catch (Exception e) {
						System.out.println("Host Unknown");
					}
		        	
		        	continue;
		        }
		        
		        if (str.compareTo("connectip")==0) {
		        	
		        	try {
		        		System.out.print("Server Name:");
		        		String hn = buf_in.readLine();
		        		System.out.print("Server IP:");
		        		String hip = buf_in.readLine();
			        	nsc = new NSClient();
			        	rs = nsc.connect(hn,hip+":1099");
			        	if(rs != null) conn = 1;
					} catch (Exception e) {
						System.out.println("Host Unknown");
					}
		        	
		        	continue;
		        }
		        
		        if (str.compareTo("find")==0) {
		        	
		        	try {
		        		System.out.print("Server Name:");
			        	System.out.println(rs.find(buf_in.readLine()));
					} catch (Exception e) {
						System.out.println("Host Unknown");
					}
		        	
		        	continue;
		        }
		        
		        if (str.compareTo("register")==0) {
		        	
		        	if (conn == 1) {
		        		
		        		System.out.print("Server Name:");
		        		ns = new NSServer(nsc.register(buf_in.readLine(),InetAddress.getLocalHost().getHostAddress()),rs.getReference());
		       
		        	} else {
		        		
		        		System.out.println("Connect Before");
		        		
		        	}
		        	continue;
		        	
		        }
		        
		        if (str.compareTo("list")==0) {
		        	
		        	if (conn == 1) {
		        		
		        		Vector<String> ls = nsc.list();
		        		Iterator<String> it = ls.iterator();
		        		
		        		while(it.hasNext()) {
		        			
		        			System.out.println(it.next());
		        			
		        		}
		        		continue;
		        		
		        	} else {
		        		
		        		System.out.println("Connect Before");
		        		continue;
		        	}
		        	
		        }
		        
		        if(str.compareTo("info")==0) {
		        	if (conn == 1) {
		        		
		        		System.out.println(rs.Info());
		        	
		        	} else {
		        		
		        		System.out.println("Connect Before");
		        		continue;
		        	}
		 
		        }
		        
		        else {
		        	
		        	if(str.isEmpty()) str = "empty";
		        	System.out.println(rs.exec(str));
		        	
		        }

			}while (!str.toLowerCase ().equals ("exit")  );
	    }	
	    catch  (IOException e) {
	        System.out.println ("IO exception = " + e );
	    }
		
	}
	
}
