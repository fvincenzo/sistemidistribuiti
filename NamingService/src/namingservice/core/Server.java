package namingservice.core;

import java.net.InetAddress;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.Iterator;
import java.util.Vector;

import namingservice.core.node.Node;
import namingservice.core.node.NodeManager;

public class Server extends UnicastRemoteObject implements RemoteServer {
	
	private Node n;
	private NodeManager nm;
	
	public Server(String n, String ip, String Info,Node father) throws RemoteException {
		
		this.n = new Node("/", n, ip, father, Info);
		nm = new NodeManager(this.n);
		
	}
	
	public Node getReference() throws RemoteException {
		
		return n;
		
	}
	
	public String Info() throws RemoteException {
		
		return n.getHostIP();
		
	}
	
	public String find(String Name) throws RemoteException {
		
		if(n.findChild(Name)==true) {
			
			return n.getChild(Name).getHostIP()+" "+n.getChild(Name).getHostID();
			
		} else {
			
			String url = "//"+n.getFather().getHostIP()+":1099/"+n.getFather().getHostID();
			
			try {
				
				RemoteServer r = (RemoteServer) Naming.lookup(url);
				return r.find(Name);
				
			} catch (Exception e) {
				return null;
			}
			
		}
		
	}
	
	public String add(String a, String ip, String Info) throws RemoteException {
		
		n.addChild(a, a, ip, n, Info);
		System.out.println("Registered:"+n.getHostID()+"."+a);
		
		//Mi connetto a mio padre per aggiornarlo
		if (n.getHostID().equals("root") == false){
			Node father = n.getFather();
			
			//		System.out.println(n.getFather());
			//		System.out.println("Server Info:"+father.getHostID()+" "+father.getHostIP());
			
			System.out.println(((Node)father).getHostID());
			
			String url = "//"+father.getHostIP()+":1099/"+father.getHostID();
			
			try {
				
				RemoteServer r = (RemoteServer) Naming.lookup(url);
				r.synch(n.getHostID(), n);
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		}
		
		return n.getHostID()+"."+a;
		
	}
	
	public Vector<String> getlist() throws RemoteException {
		
		return n.listChild();
		
	}
	
	public String exec(String s) throws RemoteException {
		
		return nm.exec(s);
		
	}
	
	public boolean synch(String ServerName, Node root)throws RemoteException {
		
		//Aggiorno me stesso
		updateNode(ServerName,root);
		
		//Aggiorno gli altri
		String url;
		Node father = n.getFather();
		
		System.out.println("Father Name:"+father.getHostID());
		
		if(father.getHostID().equals("root")==true) {
			
			url = "//"+father.getHostIP()+":1099/"+father.getHostID();
			
			try {
				
				RemoteServer r = (RemoteServer) Naming.lookup(url);
				r.updateNode(ServerName, father);
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		} else {
			
			url = "//"+father.getHostIP()+":1099/"+father.getHostID();
			
			try {
				
				RemoteServer r = (RemoteServer) Naming.lookup(url);
				r.updateNode(ServerName, father);
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			while(true) {
				
				father = father.getFather();
				
				url = "//"+father.getHostIP()+":1099/"+father.getHostID();
				
				try {
					
					RemoteServer r = (RemoteServer) Naming.lookup(url);
					r.updateNode(ServerName, father);
					
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				if(father.getHostID().equals("root")==true) {
					
					break;
					
				} else {
					
					continue;
					
				}
				
			}
			
		}
		
		
		
		return true;
		
	}
	
	public void updateNode(String ServerName, Node root)throws RemoteException {
		
		String url = ServerName;
		
		while(true)
			if(url.contains(".")==true){
				url=url.replace(url.substring(0, url.indexOf(".")+1), "");
			} else {
				System.out.println(url);
				break;
			}
		
		Node m = n.getChild(url);
		
		m.updateNode(root); 
		
	}
	
	public String request(String Name) throws RemoteException {
		
		Vector<String> pars = new Vector<String>(); 
		
		String hname = " ";
		String haddr = " ";
		
		int j = 0;
		int i = Name.length();
		
		int pos = 0;
		
		while(j<i){
			
			if(Name.charAt(j) == '.') pos=j;
			
			j++;
		}
		
		hname = Name.substring(0,pos);
		
		if(hname == "root") {
			haddr = n.getHostIP();
		} else {
		
			String line = hname;
			
			line = line.replace(line.substring(0, line.indexOf(".")+1), "");
			
			while(!line.equals("")) {
				pars.add(line.substring(0, line.indexOf(".")));
				line = line.replace(line.substring(0, line.indexOf(" ")+1), "");
			}
				
			Iterator it = pars.iterator();
			Node ns = n;
			
			
			while(it.hasNext()){
				
				ns=ns.getChild((String) it.next());
				
			}
			
			haddr = ns.getHostIP();
		
		}
		
		return hname+" "+haddr+":1099";	
	}
	
	/*public String sum(String a, String b) throws RemoteException {
		
		int c = Integer.parseInt(a);
		int d = Integer.parseInt(b);
		int somma = c+d;
		
		return String.valueOf(somma);
		
	}*/

}
