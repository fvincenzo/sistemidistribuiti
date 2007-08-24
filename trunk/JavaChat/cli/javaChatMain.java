/**
 * JavaChat Project
 */
package cli;

import core.*;
import java.io.InputStreamReader;
import java.io.BufferedReader ;
import java.io.IOException;
import java.lang.reflect.*;

/**
 * @author Vincenzo Frascino
 *
 */
public class javaChatMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		connectionManager chat = null;
        String     user = null;
	
		System.out.printf("JavaChat the new IRC World!!!!\n");
		System.out.printf("username:");
		
		try {
            user = in.readLine();
        } catch(Exception e) {
            user = "No name";
        }
         
        try {
            
            chat = connectionManager.connect("topicChat", user);
            
            System.out.println("Utente: " + user + " connesso!!!");
            System.out.println("Digita 'ENTER' per inviare una frase...");
            System.out.println("Digita 'exit' o 'quit' per uscire.");
            
            while(true) {
                String s = in.readLine();
                if ( s.equalsIgnoreCase("exit") || s.equalsIgnoreCase("quit") ) {
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



