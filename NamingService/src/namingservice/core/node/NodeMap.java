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
public class NodeMap implements Serializable{
	
	private Hashtable<String,NodeMap> children;
	private Vector<String> names;
 	private NodeMap father;
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
 	public NodeMap(String name, String host, String ip, NodeMap father, String Info) {
 		
 		super();
 		
 		this.father = father;
 		this.children = new Hashtable<String,NodeMap>();
 		this.names = new Vector<String>();
 		this.name = name;
 		this.nodeInfo = Info;
 		this.nc = new NodeCore(host,ip);
 		
// 		System.out.println("Node name:"+host);
 		
 	}
 	
 	//Funzioni di utilita' del singolo nodo: setter, getter, ecc
 	
 	public String getName() {
 		
 		return this.name;
 		
 	}
 	
 	public void setName(String n) {
 		
 		this.name = n;
 		
 	} 
 	
 	public NodeMap getFather() {
 		
 		return this.father;
 		
 	}
 	
 	public NodeMap getRoot() {
 		
 		NodeMap n = this;
 		
 		while(n.getName() != "/") {
 			n = n.getFather();
 		}
 		
 		return n;
 		
 	}
 	
 	public NodeMap getSNode(String s) {
 		
 		NodeMap n = this;
 		
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
 	
 	public void addChild(String name, String  host, String ip, NodeMap father, String Info) {
 		
 		names.add(name);
 		children.put(name, new NodeMap(name,host,ip,father,Info));
 		
 	}
 	
 	public NodeMap getChild(String name) {
 		
 		return ((NodeMap)children.get(name));
 		
 	}
 	
 	public boolean findChild(String name) {
 		
 		if(getChild(name) == null) {
 			
 			return false;
 			
 		} else {
 			
 			return true;
 		}
 		
 	}
 	
 	public String recursiveFindChild(String name) {
 		
 		if(findChild(name))
				return getChild(name).getHostIP()+" "+getChild(name).getHostID();
 		else
 		for(NodeMap node : children.values()) {
 			String ret = node.recursiveFindChild(name);
 					if (!ret.equals("Unknown Host")){
 						return ret;
 					}
 			
 		}
 		
 		return "Unknown Host";
 		
 	}
 	
 	public String removeChild(String name) {
 		
 		names.remove(name);
 		children.remove(name);
 		return "OK";
 		
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
 	
 	public Hashtable<String,NodeMap> getChildren(){
 		
 		return this.children;
 		
 	}
 	
 	public Vector<String> getNames() {
 		
 		return this.names;
 		
 	}
  	
 	public void updateNode(NodeMap n) {
 		
 		this.children = n.getChildren();
 		this.names = n.getNames();
 		
 	}
 	
}
