package core;

import java.util.*;

public class UserManager implements UserManagerInterface{
	
	@SuppressWarnings("unchecked")
	private Hashtable utenti = new Hashtable();
	static int i = 0;
	static boolean created = false;
	
	private UserManager() {
	
	}
	
	/**
	 * Design Pattern Costructor usato perch� per
	 * ogni istanza del server 
	 * deve esistere un solo userManager
	*/
	public static UserManager creator() {
		
		if(created == false) {
			
			created = true;
			return new UserManager();
			
		} else {
			
			return null;
			
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public boolean addUser(String usr,String Pwd) {
		
		if(i<256) {
			
			utenti.put(usr,new ChatUser(usr,Pwd));
			i++;
			return true;
			
		} else {
			
			return false;
		}
		
	}
	
	public void modifyUser(String oldusr,String usr,String pwd) {
		
		//To be implemented
		
	}
	
	public String returnUserPwd(String usr) {
		
		return ((ChatUser)utenti.get(usr)).getPwd();
		
	}
	
	public boolean isAdmin(String usr) {
		
		return ((ChatUser)utenti.get(usr)).isAdmin();
		
	}
	
	public boolean setAdmin(String usr,boolean admin) {
		
		return ((ChatUser)utenti.get(usr)).setAdmin(usr, admin);
		
	}
	
	@SuppressWarnings("unchecked")
	public Hashtable returnUserList() {
		
		return utenti;
		
	}
	
	/**
	 * Piccolo Main per dimostrare il funzionamento della classe
	 *
	 *	public static void main(String args[]) throws Exception {
	 *
	 *	userManager ute = userManager.creator();
	 *  //userManager ute1 = userManager.creator();
	 *	
	 *	ute.addUser("vincenzo", "frascino");
	 *	ute.addUser("leonardo", "frascino");
	 *	
	 *	ute.setAdmin("vincenzo", true);
	 *	System.out.printf("%b\n",ute.isAdmin("vincenzo"));
	 *	ute.setAdmin("vincenzo", false);
	 *	System.out.printf("%b\n",ute.isAdmin("vincenzo"));
	 *	
	 *	System.out.printf("%s\n",ute.returnUserPwd("vincenzo"));
	 *	
	 *	Hashtable users = ute.returnUserList();
	 *	Enumeration e  = users.elements();
	 *	int i = 0;
	 *	
	 *	while(e.hasMoreElements()) {
	 *		
	 *		//li stampa alla storta
	 *		
	 *		System.out.printf("%d - %s\n",i,((user)e.nextElement()).getUser());
	 *		i++;
	 *		
	 *	}		
	 *	
	 * }
     */
}
