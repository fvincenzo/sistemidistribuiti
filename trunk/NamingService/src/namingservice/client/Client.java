package namingservice.client;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.Iterator;
import java.util.Vector;

import namingservice.core.NSClient;
import namingservice.core.NSServer;
import namingservice.core.RemoteServer;

/**
 * Classe Client usata per connettersi al servizio di naming
 * 
 * @author Nicolas Tagliani e Vincenzo Frascino
 *
 */
public class Client {

	/**
	 * Metodo Main
	 * 
	 * @param args serve a passare i parametri da shell(inutilizzato al momento)
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {
			
			NSServer ns;
			InputStreamReader reader = new InputStreamReader (System.in);
			BufferedReader in = new BufferedReader (reader);
			BufferedReader i = new BufferedReader(new FileReader("config.txt"));
			String host = i.readLine();
			String hname = host.substring(0,host.indexOf(" "));
			String haddr = host.substring(host.indexOf(" ")+1);
			
			NSClient nsc = null;
			RemoteServer rs = null;
			int conn = 0;
			
			String str;
				
				do {
					
					System.out.print(">");
			        str = in.readLine ();
			        
			        if (str.compareTo("connect")==0) {
			        	
			        	try {
				        	nsc = new NSClient();
				        	rs = rs = nsc.connect(hname, haddr);
				        	if(rs != null) conn = 1;
						} catch (Exception e) {
							System.out.println("Host Unknown");
						}
			        	
			        	continue;
			        }
			        
			        if (str.compareTo("connectip")==0) {
			        	
			        	try {
			        		System.out.print("Server Name:");
			        		String hn = in.readLine();
			        		System.out.print("Server IP:");
			        		String hip = in.readLine();
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
				        	System.out.println(rs.find(in.readLine()));
						} catch (Exception e) {
							System.out.println("Host Unknown");
						}
			        	
			        	continue;
			        }
			        
			        if (str.compareTo("register")==0) {
			        	
			        	if (conn == 1) {
			        		
			        		System.out.print("Server Name:");
			        		ns = new NSServer(nsc.register(in.readLine(),InetAddress.getLocalHost().getHostAddress()),rs.getReference());
			       
			        	} else {
			        		
			        		System.out.println("Connect Before");
			        		
			        	}
			        	continue;
			        	
			        }
			        
			        if (str.compareTo("list")==0) {
			        	
			        	if (conn == 1) {
			        		
			        		Vector<String> ls = nsc.list();
			        		Iterator it = ls.iterator();
			        		
			        		while(it.hasNext()) {
			        			
			        			System.out.println((String) it.next());
			        			
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
			        
			        if(str.compareTo("sum")==0) {
			        	if (conn == 1) {
			        		
			        		String a = in.readLine();
			        		String b = in.readLine();
			        		a = rs.sum(a, b);
			        		System.out.println("La somma è:"+a);
			        	
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
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

}
