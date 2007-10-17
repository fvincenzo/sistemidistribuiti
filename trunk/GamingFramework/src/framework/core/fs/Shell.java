package framework.core.fs;
import java.util.*;

/**
 * Implementa una semplice shell che permette il browsing nell'albero
 * degli oggetti e la manipolazione dei nodi a livello elementare. 
 * @author Vincenzo Frascino
 */
public class Shell {
	private Node selectedNode;
	/**
	 * Costruttore della classe Shell.
	 */
	public Shell() {
		selectedNode = null;
	}
	/**
	 * Restituisce il nodo selezionato per la shell.
	 * @return Il nodo attualmente selezionato (==cwd in un filesystem).
	 */
	public Node getSelectedNode() {
		return selectedNode;
	}
	/**
	 * Imposta il nodo selezionato per la shell.
	 * @param n Istanza del nodo da selezionare.
	 */
	public void	setSelectedNode(Node n) {
			selectedNode = n;
	}
	/**
	 * Invoca un comando di shell, tenendo eventualmente conto del nodo
	 * attualmente selezionato.
	 * @param cmd Un comando da eseguire.
	 * @return Una stringa contenente il risultato dell'operazione o null
	 * se non esiste nessun comando con il nome specificato. 
	 * @throws NullPointerException
	 */
	public String eval(String cmd) throws NullPointerException {
		String res = null;
		if(cmd != null) {
			ParsedCmd parsedCmd = parseCmd(cmd);
			res = evalParsedCmd(parsedCmd);
		} else throw new NullPointerException();
		return res;
	}
	/**
	 * Invoca un comando predigerito, tenendo eventualmente conto del nodo
	 * attualmente selezionato.
	 * @param cmd Un comando predigerito da eseguire.
	 * @return Una stringa contenente il risultato dell'operazione o null
	 * se non esiste nessun comando con il nome specificato. 
	 * @throws NullPointerException
	 */
	public String evalParsedCmd(ParsedCmd cmd) {
		String res = null;
		if(cmd != null) {
			if(selectedNode != null) {
				if(cmd.Name.equals(".ls")) {
					res = ls();
				} else if(cmd.Name.equals(".sel")) {
					if(cmd.Params.length == 1) {
						res = sel(cmd.Params[0]);
					} else res = "sel takes 1 argument(s).";
				} else if(cmd.Name.equals(".new")) {
					if(cmd.Params.length == 2) {
						create(cmd.Params[0], cmd.Params[1]);
					} else res = "new takes 2 argument(s)";
				} else if (cmd.Name.equals(".cat")) {
					if (cmd.Params.length == 1) {
						res = cat(cmd.Params[0]);
					} else res = "cat takes 1 argument(s)";
				} else {
					Object ret = selectedNode.Dispatch(cmd);
					if(ret != null) {
						res = ret.toString(); 
					} else {
						res = "Unknown command " + cmd.Name;
					}
				}
			} else throw new NullPointerException();
		} else throw new NullPointerException();
		return res;
	}
	/**
	 * Elenca i figli del nodo selezionato
	 * @return Un elenco di nomi di nodo separati da spazio.
	 */
	public String ls() {
		String res = "";
		Enumeration e = selectedNode.children();
		while(e.hasMoreElements()) {
			Node n = (Node)e.nextElement();
			res = res + n.getName() + " ";
		}
		return res;
	}
	/**
	 * Seleziona un nodo.
	 * @param name Il nome (assoluto o relativo alla selezione corrente)
	 * 			del nodo da selezionare
	 */
	public String sel(String name) {
		String res;
		Node n = selectedNode.find(name); 
		if(n != null) {
			selectedNode = n;
			res = n.toString();
		} else res = "unknown node " + name;
		return res;
	}
	/**
	 * Crea un nuovo nodo
	 * @param clsname Il nome della classe del nuovo nodo.
	 * @param name Il nome del nuovo nodo.
	 */
	public String create(String clsname, String name) {
		String res;
		try {
			// TODO: controllo validit� classe!
			// (deriva da Node?)
			Class type = Class.forName(clsname);
			Node n = (Node)type.newInstance(); 
			n.setName(name);
			selectedNode.addChild(n);
			res = n.fullname();
		} 
		catch(ClassNotFoundException e) {
			res = "Class not found: " + clsname;
		}
		catch(Exception e) {
			res = "Class instantiantion failed";
		}
		return res;
	}
	/**
	 * Metodo cat
	 * @param nome del nodo su cui si vogliono infornazioni
	 * @return Informazioni
	 */
	public String cat(String name) {
		String res;
		Node n = selectedNode.find(name);
		res = n.getInfo();
		return res;
	}
	/// <summary>
	/// Cook a String into a parsed cmd. 
	/// REQUIRES: cmd != null
	/// </summary>
	public static ParsedCmd parseCmd(String cmd) {
		int i = 0;
		int j = 0;
		String sub;
		ParsedCmd parsedCmd = new ParsedCmd(); 
		List cmdParams = new ArrayList(); 
		boolean quoted = false;
		while(i < cmd.length()) {
			switch(cmd.charAt(i)) {
				case ' ':
					if(!quoted && i != j) {
						sub = cmd.substring(j, i);
						cmdParams.add(sub.trim()); 
						j = i + 1;
					}
					break;
				case '\"':
					quoted = !quoted;
					if(!quoted && i != j) {
						sub = cmd.substring(j, i);
						cmdParams.add(sub); 
					}
					j = i + 1;
					break;
			}
			i++;
		}
		sub = cmd.substring(j, i);
		cmdParams.add(sub.trim()); 
		// First param = cmd name;
		parsedCmd.Name = (String)cmdParams.remove(0);
		if(cmdParams.size() > 0) {
			parsedCmd.Params = new String[cmdParams.size()];
			int k;
			for(k = 0; k < cmdParams.size(); k++) {
				parsedCmd.Params[k] = (String)cmdParams.get(k);
			}
		}
		return parsedCmd;
	}
}
