package namingservice.core.node;

import java.io.Serializable;

/**
 * Classe NodeCore costituisce il core dell'elemento minimo del database interno di un Host rappresenta un nodo della rete
 * implementa l'interfaccia Serializzable per poter essere usata come parametro di una funzione che sfrutta RMI.
 * 
 * @author Nicolas Tagliani e Vincenzo Frascino
 *
 */
public class NodeCore implements Serializable{

	private String HostID;
	private String HostIP;
	private static final long serialVersionUID = 1L;
	
	/**
	 * Cotruttore si occupa di settere i parametri ID ed IP che non possono essere piu' cambiati
	 * 
	 * @param ID identificatore dell'host
	 * @param IP ip dell'host
	 */
	public NodeCore(String ID, String IP) {
		
//		System.out.println("Il nome e':"+ID);
		this.HostID = ID;
		this.HostIP = IP;
		
	}
	
	//Funzioni di utilita'
	
	public String getID() {
		
		return this.HostID;
		
	}
	
	public String getIP() {
		
		return this.HostIP;
		
	}
	
}
