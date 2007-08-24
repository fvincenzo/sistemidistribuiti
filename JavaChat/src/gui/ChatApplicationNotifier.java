/*
 * Created on Jan 8, 2007
 *
 */
package src.gui;

public interface ChatApplicationNotifier {
    public void textReceived(String text);
    public void userJoin(String userName);
    public void userPart(String userName);

}
