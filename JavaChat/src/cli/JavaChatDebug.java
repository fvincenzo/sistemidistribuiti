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
public class JavaChatDebug {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String str= new String();
	
		System.out.printf("JavaChat Hello World 1!!!!\n");
		
		while(true) {
			
		
			System.out.printf("javaChat>");
		
			try { 
		
				str = in.readLine(); 
		
			} catch (IOException e) { 
				
				System.out.println ("Si � verificato un errore: " + e);
				System.exit(-1); 
			
			}
	
			try {
		         Class c = Class.forName(str);
		         Method m[] = c.getDeclaredMethods();
		         for (int i = 0; i < m.length; i++)
		         System.out.println(m[i].toString());
		      }
		      catch (Throwable e) {
		         System.err.println(e);
		    }
		
		}
		
	}

}
