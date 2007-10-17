package framework.console;

import java.io.*;
import framework.core.*;
import framework.core.client.*;
import framework.core.messaging.ChatMessage;
import framework.core.messaging.ServerMessage;

/**
 * @author Vincenzo Frascino
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class TextModeClient implements ClientUI {
	private BufferedReader stdin;
    private Client client;
    /**
     * Genera un Client TextMode
     *
     */
	public TextModeClient() {
		stdin = new BufferedReader(new InputStreamReader(System.in));
        client = new Client(this);
	}
	/**
	 * 
	 * @return il client generato
	 */
    public Client getClient() {
            return client;
    }
    /**
     * @param prompt contiene il messaggio di prompt.
     * @return una stringa contenete il messaggio 
     * di ritorno della console.
     */
	public String onConsoleInput(String prompt) {
		System.out.print(prompt);
		String s = null;
		try {
			s = stdin.readLine();
		} catch(IOException e) {
		}
		return s;
	}
	/**
	 * Overriding del metodo standard di stampa. 
	 * @param prompt contiene il messaggio di prompt.
	 */
	public void onConsolePrint(String text) {
 		System.out.print(text);
 	}
	/**
	 * Azione svolta in risposta all'invio di un log.
	 * @param text contiene i messaggi di log del server.
	 */
	public void onLogMessage(String text) {
 		System.out.print(text);
 	}
	/**
	 * Azione svolta in risposta all'invio di un kick.
	 */
	public void onKick() {
		onLogMessage("Sei stato espulso dal server.\n");
	}
	/**
	 * Azione svolta in risposta all'invio di un Messaggio del Server.
	 */
    public void onServerMessage(ServerMessage msg) {
        if(msg.type().equals("login")) {
            onConsolePrint(msg.data() + " si e' connesso.\n");
        } else if(msg.type().equals("logout")) {
            onConsolePrint(msg.data() + " si e' disconnesso.\n");
        } else if(msg.type().equals("chat")) {
            ChatMessage cm = (ChatMessage)msg;
            onConsolePrint(cm.sender() + ">" + cm.data() + "\n");
        }
    }
}
