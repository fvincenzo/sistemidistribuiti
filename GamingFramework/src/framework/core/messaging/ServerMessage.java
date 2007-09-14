/*
 * ServerMessage.java
 *
 * Created on 7 giugno 2004, 18.23
 */

package framework.core.messaging;
import java.io.*;
/**
 *
 * @author  Vincenzo Frascino
 */
public class ServerMessage implements Serializable {
    private String type;
    private Object data;
    /** Creates a new instance of ServerMessage */
    public ServerMessage(String type, Object data) {
        this.type = type;
        this.data = data;
    }
    /**
     * Ritorna il tipo di messaggio del server.
     * @return una stringa col tipo di messaggio del sever.
     */
    public String type() {
        return type;
    }
    /**
     * Ritorna il messaggio del server.
     * @return una stringa col messaggio del sever.
     */
    public Object data() {
        return data;
    }
}
