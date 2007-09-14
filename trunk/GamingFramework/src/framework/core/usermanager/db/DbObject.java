package framework.core.usermanager.db;
import java.util.*;

import framework.core.fs.Node;
import framework.core.fs.ParsedCmd;
/**
 * Interfaccia per oggetti sincronizzabili con un database.
 * @author Vincenzo Frascino
 */
public abstract class DbObject extends Node {
	/**
	 * Carica le informazioni del database nell'oggetto locale.
	 */
	public void load() {
		DbServer db = DbServer.getInstance(); 
		Dictionary data = (Dictionary)db.getTableData(parent().getName()).get(getName());
		setObjectData(data);
	}
	/**
	 * Copia l'oggetto locale nel database.
	 */
	public void save() {
		DbServer db = DbServer.getInstance(); 
		Dictionary data = getObjectData();
		db.setRow(parent().getName(), getName(), data);
	}
	public abstract void setObjectData(Dictionary data);
	public abstract Dictionary getObjectData();
	/**
	 * Override del metodo Dispatch della classe node
	 * @param cmd Il comando da eseguire sul nodo.
	 */
	public String Dispatch(ParsedCmd cmd) {
		String res = null;
		if(cmd.Name.equals(".save")) {
			save();
			res = "ok";
		} else if(cmd.Name.equals(".cat")) {
			Dictionary data = getObjectData();
			Enumeration keys = data.keys();
			res = getClass().getName() + " " + getName() + " is:\n";
			while(keys.hasMoreElements()) {
				String name = (String)keys.nextElement();
				res = res + name + ": " + data.get(name) + "\n";
			}
		} else res = super.Dispatch(cmd);
		return res;
	}
}
