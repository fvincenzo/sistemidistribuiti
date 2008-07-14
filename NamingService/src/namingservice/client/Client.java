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
import namingservice.core.Server;

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
			Server s = null;
			InputStreamReader reader = new InputStreamReader (System.in);
			BufferedReader in = new BufferedReader (reader);
			BufferedReader i = new BufferedReader(new FileReader("config.txt"));
			String host = i.readLine();
			String hname = host.substring(0,host.indexOf(" "));
			String haddr = host.substring(host.indexOf(" ")+1);
			String myname = null;
			
			NSClient nsc = null;
			RemoteServer rs = null;
			int conn = 0;
			
			String str;
				
				do {
					
					System.out.print(">");
			        str = in.readLine ();
			        
			        if (str.compareTo("start")==0) {
			        	
			        	ns = new NSServer("root",null);
			        	
			        }
			        
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
			        		myname = in.readLine();
			        		ns = new NSServer(nsc.register(myname,InetAddress.getLocalHost().getHostAddress()),rs.getReference());
			        		s = ns.getReference();
			        		
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
			        
			        //TODO: Aggiungere askremove utile ad un nodo a rimuovere se stesso da una rete
			        
			        if(str.compareTo("askremove")==0) {
			        	if (conn == 1) {
			        		
			        		System.out.println(rs.askremove(myname));
			        		
			        	} else {
			        		
			        		System.out.println("Connect Before");
			        		continue;
			        	}
			        }
			        
			        if(str.compareTo("remove")==0) {
			        	if (conn == 1) {
			        		
			        		System.out.println("Node Name:");
			        		String a = in.readLine();
			        		System.out.println(s.remove(a));
			        		
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
			        		System.out.println("La somma e':"+a);
			        	
			        	} else {
			        		
			        		System.out.println("Connect Before");
			        		continue;
			        	}
			 
			        }
			        
			        else {
			        	
			        	if(str.isEmpty()) str = "empty";
			        	System.out.println(s.exec(str));
			        	
			        }

				}while (!str.toLowerCase ().equals ("exit")  );
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

}
