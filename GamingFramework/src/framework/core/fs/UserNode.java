package framework.core.fs;

import framework.core.usermanager.User;

/**
 * 
 * @author Vincenzo Frascino
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class UserNode extends Node {
	private User user;
	/**
	 * Genera un nodo contenete uno user.
	 * @param u contiene lo user.
	 * @throws NullPointerException
	 */
	public UserNode(User u) throws NullPointerException {
		if(u != null) {
			user = u;
			setName(u.getAccount().getName());
			addChild(new Node("files"));
			addChild(new Node("messages"));
		} else throw new NullPointerException();
	}
	/**
	 * 
	 * @return uno user esistente.
	 */
	public User getUser() {
		return user;
	}
	
	/**
	 * Overrida il Metodo della classe Nodo
	 */
	public String getInfo() {
		
		return "Username:"+user.getAccount().getName()+"\n";
		
	}
	
}
