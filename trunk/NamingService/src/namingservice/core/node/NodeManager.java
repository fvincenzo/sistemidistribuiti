package namingservice.core.node;

import java.util.Iterator;
import java.util.Vector;

/**
 * Classe NodeManager costituisce il sistema di gestione del database interno di un Host rappresenta un nodo della rete
 * 
 * @author Nicolas Tagliani e Vincenzo Frascino
 *
 */
public class NodeManager {
	
	private NodeMap curPos;
	
	private NodeMap n;
	
	/**
	 * Costruttore NodeManager 
	 * @param n nodo da gestire
	 */
	public NodeManager(NodeMap n) {
		
		this.n = n;
		this.curPos = n;
		
	}
		
	public void update(NodeMap n){
		this.n = n;
		this.curPos = n;
	}
	/**
	 * Metodo exec serve ad invocare i comandi sul database di un host
	 * @param command comando da invocare
	 * @return risultato invocazione
	 */
	public String exec(String command) {
		
		String com = " ";
		String pars = " ";
		String result = "";
		
		//System.out.println(command);
		//comandi singola opzione
		if(command.equals("ls")==true) {
			
			Vector<String> list = curPos.listChild();
			Iterator<String> i = list.iterator();
			
			if(list.isEmpty()) {
				result = " ";
			} else {
			
				while(i.hasNext()) {
					result += ((String) i.next()) + " ";
				}
				
			}
			
		} 
		
		//comandi con 2 opzioni
		else {
			
			if(command.indexOf(" ")>0){
				
				com = command.substring(0, command.indexOf(" "));
				pars = command.substring(command.indexOf(" ")+1);
				
				if(com.equals("cd")) {
					
					if(pars.equals("..")) {
						
						if(!n.getHostID().equals(curPos.getHostID()))
							curPos = curPos.getFather();
//						else
//							n = n;
						
					} else {
						
						curPos = curPos.getChild(pars);
						
					}
					
				} else {
					
					result = "command:"+com+" pars:"+pars+" Unknown exec command";
					
				}
				
			} else {
				
				result = "command:"+com+" pars:"+pars+" Unknown exec command";
				
			}
			
		}
		
		return result;
		
	}
	
}
