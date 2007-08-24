/**
 * 
 */
package src.core;

import java.util.Hashtable;

/**
 * @author Vincenzo Frascino
 *
 */
public interface UserManagerInterface {

	public abstract boolean addUser(String usr,String Pwd);
	public abstract void modifyUser(String oldusr,String usr,String pwd);
	public abstract String returnUserPwd(String usr);
	public abstract boolean isAdmin(String usr);
	public abstract boolean setAdmin(String usr,boolean admin);
	public abstract Hashtable returnUserList();
	
}
