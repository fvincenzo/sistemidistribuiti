package namingservice.core.node;

import java.util.Hashtable;
import java.util.Vector;
import java.io.Serializable;

/**
 * Classe Node costituisce l'elemento minimo del database interno di un Host rappresenta un nodo della rete
 * implementa l'interfaccia Serializzable per poter essere usata come parametro di una funzione che sfrutta RMI.
 * 
 * @author Nicolas Tagliani e Vincenzo Frascino
 *
 */
public class Node implements Serializable{
	
	private Hashtable<String,Node> children;
	private Vector<String> names;
 	private Node father;
 	private String name;
 	private String nodeInfo;
 	private static final long serialVersionUID = 7526471155622776147L;
 	
 	//NodeCore information
 	private NodeCore nc;
 	
 	/**
 	 * Costruttore Node si occupa di inizializzare il nodo
 	 * 
 	 * @param name nome del nodo
 	 * @param host nome completo del nodo
 	 * @param ip ip del nodo
 	 * @param father riferimento del padre
 	 * @param Info informazioni sul nodo
 	 */
 	public Node(String name, String host, String ip, Node father, String Info) {
 		
 		super();
 		
 		this.father = father;
 		this.children = new Hashtable<String,Node>();
 		this.names = new Vector<String>();
 		this.name = name;
 		this.nodeInfo = Info;
 		this.nc = new NodeCore(host,ip);
 		
// 		System.out.println("Node name:"+host);
 		
 	}
 	
 	//Funzioni di utilità del singolo nodo: setter, getter, ecc
 	
 	public String getName() {
 		
 		return this.name;
 		
 	}
 	
 	public void setName(String n) {
 		
 		this.name = n;
 		
 	} 
 	
 	public Node getFather() {
 		
 		return this.father;
 		
 	}
 	
 	public Node getRoot() {
 		
 		Node n = this;
 		
 		while(n.getName() != "/") {
 			n = n.getFather();
 		}
 		
 		return n;
 		
 	}
 	
 	public Node getSNode(String s) {
 		
 		Node n = this;
 		
 		while(n.getName() != s) {
 			n = n.getFather();
 		}
 		
 		return n;
 		
 	}
 	
 	public String getInfo() {
 		
 		return this.nodeInfo;
 		
 	}
 	
 	public void setInfo(String Info) {
 		
 		this.nodeInfo = Info;
 		
 	}
 	
 	public void addChild(String name, String  host, String ip, Node father, String Info) {
 		
 		names.add(name);
 		children.put(name, new Node(name,host,ip,father,Info));
 		
 	}
 	
 	public Node getChild(String name) {
 		
 		return ((Node)children.get(name));
 		
 	}
 	
 	public boolean findChild(String name) {
 		
 		if(getChild(name) == null) {
 			
 			return false;
 			
 		} else {
 			
 			return true;
 		}
 		
 	}
 	
 	public void removeChild(String name) {
 		
 		names.remove(name);
 		children.remove(name);
 		
 	}
 	
 	public Vector<String> listChild() {
 	
 		return names;
 		
 	}

 	public String getHostID() {
 		
// 		System.out.println("Nome host:"+nc.getID());
 		return nc.getID();
 		
 	}
 	
 	public String getHostIP() {
 	
 		return nc.getIP();
 		
 	}
 	
 	public Hashtable<String,Node> getChildren(){
 		
 		return this.children;
 		
 	}
 	
 	public Vector<String> getNames() {
 		
 		return this.names;
 		
 	}
  	
 	public void updateNode(Node n) {
 		
 		this.children = n.getChildren();
 		this.names = n.getNames();
 		
 	}
 	
}
