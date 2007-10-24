/*
 * Created on Jan 8, 2007
 *
 */
package gui;

public interface ChatApplicationNotifier {
    
    public abstract void textReceived(String text);
    public abstract void textReceived(String username, String text);
    public abstract void userJoin(String userName);
    public abstract void userPart(String userName);

}
