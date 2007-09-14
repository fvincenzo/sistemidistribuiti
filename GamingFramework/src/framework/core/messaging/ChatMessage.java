/*
 * ChatMessage.java
 *
 * Created on 8 giugno 2004, 23.44
 */

package framework.core.messaging;

import java.io.*;

import framework.core.usermanager.User;

/**
 *
 * @author Vincenzo Frascino
 */
public class ChatMessage extends ServerMessage implements Serializable {
    private String sender;
    /** Creates a new instance of ChatMessage */
    public ChatMessage(User sender, String text) {
        super("chat", text);
        this.sender = sender.getAccount().getName();
    }
    /** Invia una Stringa sulla chat **/
    public String sender() {
        return sender;
    }
    
}
