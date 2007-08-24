/**
 * JavaChat Project
 */
package core;

/**
 * @author Vincenzo Frascino
 *
 */
public interface UserInterface {
	
	public abstract boolean setAdmin(String User,boolean admin);
	public abstract boolean isAdmin();
	public abstract String getUser();
	public abstract String getPwd();
	public abstract void setUser(String Username,String Password);

}
