package namingservice.core.node;

import java.util.Iterator;
import java.util.Vector;

public class NodeManager {
	
	private Node n;
	
	public NodeManager(Node n) {
		
		this.n = n;
		
	}
		
	
	public String exec(String command) {
		
		String com = " ";
		String pars = " ";
		String result = "";
		
		//System.out.println(command);
		//comandi singola opzione
		if(command.equals("ls")==true) {
			
			Vector<String> list = n.listChild();
			Iterator<String> i = list.iterator();
			
			while(i.hasNext()) {
				result += ((String) i.next()) + " ";
			}
			
		} 
		
		//comandi con 2 opzioni
		else {
			
			if(command.indexOf(" ")>0){
				
				com = command.substring(0, command.indexOf(" "));
				pars = command.substring(command.indexOf(" ")+1);
				
				if(com.equals("cd")) {
					
					if(pars.equals("..")) {
						
						if(n.getName().equals("/")==false)
							n = n.getFather();
						else
							n = n;
						
					} else {
						
						n = n.getChild(pars);
						
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
