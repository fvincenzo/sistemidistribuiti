/**
 * JavaChat Project
 */
package cli;

import core.*;
import java.io.InputStreamReader;
import java.io.BufferedReader ;

/**
 * @author Vincenzo Frascino
 *
 */
public class JavaChatMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		Channel chat = null;
        String     user = null;
	
		System.out.printf("JavaChat the new IRC World!!!!\n");
		System.out.printf("username:");
		
		try {
            user = in.readLine();
        } catch(Exception e) {
            user = "No name";
        }
         
        try {
            
            chat = Channel.connect("topicChat", user);

            System.out.println("Utente: " + user + " connesso!!!");
            System.out.println("Digita 'ENTER' per inviare una frase...");
            System.out.println("Digita '/exit' o '/quit' per uscire.");
            
            while(true) {
                String s = in.readLine();
                if ( s.equalsIgnoreCase("/exit") || s.equalsIgnoreCase("/quit") ) {
                    chat.disconnect(); 
                    System.exit(0);
                } else
                    chat.sendText(s);
            }
            
        } catch(Exception e) {
            System.out.println( e.toString());
        }
    }

		
}



