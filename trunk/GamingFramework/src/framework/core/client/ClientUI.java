/*
 * ClientInterface.java
 *
 * Created on 8 giugno 2004, 9.58
 */

package framework.core.client;

import framework.core.messaging.ServerMessage;

/**
 *
 * @author  Vincenzo Frascino
 */
public interface ClientUI {
    public void onKick();
    public String onConsoleInput(String prompt);
    public void onConsolePrint(String text);
    public void onServerMessage(ServerMessage msg);
}
