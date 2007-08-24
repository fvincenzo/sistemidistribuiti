/**
 * JavaChat Project
 */
package core;

/**
 * @author Vincenzo Frascino
 *
 */
public class user implements userInterface{

	String Username = new String();
	String Password = new String();
	boolean admin = false;
	
	/*
	 *public user() {
	 *	
	 *}
	*/
	
	public user(String User,String Pwd) {
		
		this.Username = User;
		this.Password = Pwd;
		
	}
	
	public boolean setAdmin(String User,boolean admin) {
		
		if(this.Username == User) {
			
			this.admin = admin;
			return true;
			
		} else {
			
			return false;
			
		}
	
	}
	
	public boolean isAdmin() {
		
		return this.admin;
		
	}

	
	public String getUser() {
	
		return this.Username;
		
	}
	
	public String getPwd() {
		
		return this.Password;
		
	}
	
	public void setUser(String Username,String Password) {
		
		this.Username = Username;
		this.Password = Password;
		
	}
	
}
