package framework.core.usermanager;
/**
 * Mantiene lo stato dell'utente
 * 
 * @author Vincenzo Frascino
 *
 */
public class UserStatus {

	private static String username;
	
	private static String connected;
	
	public UserStatus(String username, String connected) {
		
		this.username = username;
		this.connected = connected;
		
	}
	
	public String getStatus(String username) {
		
		return connected;
		
	}
	
	public void setStatus(String connected){
		
		this.connected = connected;
		
	}
	
	public String getUser() {
		return this.username;
	}
	
	public UserStatus returnUserStatus() {
			return this;
	}
	
}
