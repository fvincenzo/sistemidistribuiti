package namingservice.core;

import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.Iterator;
import java.util.Vector;

import namingservice.core.node.NodeMap;
import namingservice.core.node.NodeManager;

/**
 * Classe Server rappresenta il servizio di naming vero e proprio estende la classe UnicastRemoteObject per poter utilizzare RMI ed implementa l'interfaccia remota RemoteException
 * 
 * @author Nicolas Tagliani e Vincenzo Frascino
 *
 */
public class Server extends UnicastRemoteObject implements RemoteServer {
	
	private NodeMap n;
	private NodeManager nm;
	private static final long serialVersionUID = 7526471155622776147L;
	
	
	/**
	 * Costruttore Server si occupa di inizializzare i paramtri del servizio
	 * 
	 * @param n il nome del server
	 * @param ip l'ip del server
	 * @param Info le informazioni sul server
	 * @param father riferimento al nodo padre
	 * @throws RemoteException dovuta ad eventuali fallimenti o errori del server RMI in fase di registrazione del servizio
	 */
	public Server(String n, String ip, String Info,NodeMap father) throws RemoteException {
		
		this.n = new NodeMap("/", n, ip, father, Info);
		nm = new NodeManager(this.n);
		
	}
	
	/**
	 * Metodo getReference consente di ottenere un riferimento al nodo
	 */
	public NodeMap getReference() throws RemoteException {
		
		return n;
		
	}
	
	/**
	 * Metodo Info consente di ottenere le informazioni sul nodo
	 */
	public String Info() throws RemoteException {
		
		return n.getHostIP();
		
	}
	
	/**
	 * Metodo lfind consente la ricerca di un nome sul database locale se la richiesta non ha esito positivo viene propagata al padre fino ad arrivare a root. Se il nodo root non riesce a dare una risposta viene tornato il messaggio host non trovato
	 *
	 * @param Name indica il nome dell'host da cercare
	 * @return una stringa contenente l'ip
	 */
	public String lfind(String Name) {
		
		if(n.findChild(Name)==true) {
			
			return n.getChild(Name).getHostIP()+" "+n.getChild(Name).getHostID();
			
		} else {
			
			return "Unknown host";
			
		}
		
	}
	
	/**
	 * Metodo find consente la ricerca di un nome sul database locale se la richiesta non ha esito positivo viene propagata al padre fino ad arrivare a root. Se il nodo root non riesce a dare una risposta viene tornato il messaggio host non trovato
	 *
	 * @param Name indica il nome dell'host da cercare
	 * @return una stringa contenente l'ip
	 */
	public String find(String Name) throws RemoteException {
		
		System.out.println("Server.find():"+n.findChild(Name));
		
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
	
	/**
	 * Metodo add consente di registrare un host sul proprio naming service
	 * 
	 * @param a nome dell'host da regitrare
	 * @param ip ip dell'host da regitrare
	 * @param Info informazioni dell'host da registrare
	 * 
	 * @return una stringa contenente il nome completo dell'host
	 */
	public String add(String a, String ip, String Info) throws RemoteException {
		
		n.addChild(a, n.getHostID()+"."+a, ip, n, Info);
		System.out.println("Registered:"+n.getHostID()+"."+a);
		
		//Mi connetto a mio padre per aggiornarlo
		if (n.getHostID().equals("root") == false){
			NodeMap father = n.getFather();
			
			//		System.out.println(n.getFather());
			//		System.out.println("Server Info:"+father.getHostID()+" "+father.getHostIP());
			
			System.out.println(((NodeMap)father).getHostID());
			
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
	
	/**
	 * Metodo getlist consente di ottenere una lista dehli host registrati sul proprio servizio
	 *
	 * @return un vettore contenete i nomi degli host
	 */
	public Vector<String> getlist() throws RemoteException {
		
		return n.listChild();
		
	}
	
	public String exec(String s){
		
		return nm.exec(s);
		
	}
	
	/**
	 * Metodo synch viene utilizzato quando una modifica viene fatta ad un livello x della rete per propagarla ricorsivamente fino alla root
	 *
	 * @param ServerName nome del server che ha ricevuto modifica
	 * @param root database del nodo che ha ricevuto la modifica
	 * 
	 * @return risultato della propagazione della modifica true se e' andata a buon fine
	 */
	public boolean synch(String ServerName, NodeMap root)throws RemoteException {
		
		//Aggiorno gli altri
		String url;
		NodeMap father = n.getFather();
		
		updateNode(ServerName,root);
		
		if(father != null){
			
			url = "//"+father.getHostIP()+":1099/"+father.getHostID();
			
			try {
				
				RemoteServer r = (RemoteServer) Naming.lookup(url);
				r.synch(n.getHostID(), n);
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		}
		
		
		return true;
		
	}
	
	/**
	 * Metodo updateNode serve a modificare il database di un nodo
	 * 
	 * @param ServerName nome del server che ha ricevuto modifica
	 * @param root database del nodo che ha ricevuto la modifica
	 */
	public void updateNode(String ServerName, NodeMap root)throws RemoteException {
		
		String url = ServerName;
		
		while(true)
			if(url.contains(".")==true){
				url=url.replace(url.substring(0, url.indexOf(".")+1), "");
			} else {
				System.out.println(url);
				break;
			}
		
		NodeMap m = n.getChild(url);
		
		m.updateNode(root); 
		
	}
	
	/**
	 * Metodo request fornisce nomehost ed indirizzo di un determinato host
	 * 
	 * @param Name nome dell'host
	 */
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
				
			Iterator<String> it = pars.iterator();
			NodeMap ns = n;
			
			
			while(it.hasNext()){
				
				ns=ns.getChild((String) it.next());
				
			}
			
			haddr = ns.getHostIP();
		
		}
		
		return hname+" "+haddr+":1099";	
	}
	
	public String remove(String Name) {
		
		String result = n.removeChild(Name);
		
		String url = "//"+n.getFather().getHostIP()+":1099/"+n.getFather().getHostID();
		
		try {
			
			RemoteServer r = (RemoteServer) Naming.lookup(url);
			r.synch(n.getHostID(), n);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return result;
		
	}
	
	public String askremove(String Name) throws RemoteException {
		
		String result = n.removeChild(Name);
		
		String url = "//"+n.getFather().getHostIP()+":1099/"+n.getFather().getHostID();
		
		try {
			
			RemoteServer r = (RemoteServer) Naming.lookup(url);
			r.synch(n.getHostID(), n);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return result;
		
	}
	
	/**
	 * Metodo sum servizio generico messo a disposione dall'host come esempio di utilizzo
	 * 
	 * @param a primo parametro
	 * @param b secondo parametro
	 * @return la somma
	 * @throws RemoteException eccezione sollevata da RMI
	 */
	public String sum(String a, String b) throws RemoteException {
		
		System.out.println("Server.sum():"+a+" "+b);
		int c = Integer.parseInt(a);
		int d = Integer.parseInt(b);
		int somma = c+d;
		
		return String.valueOf(somma);
		
	}

}
