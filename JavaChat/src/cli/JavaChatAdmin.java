/**
 * javaChat Project
 */
package src.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.*;
import src.core.*;

/**
 * @author   Prof. Frascino
 */
public class JavaChatAdmin {

	JavaChatAdminService admin = new JavaChatAdminService();
	BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	String str= new String();
	String str2= new String();
	private static boolean start = true;
	
	public static void main(String[] args) {
		
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String str= new String();
		System.out.println();
		System.out.println("Java Chat Admin Service... ");
		
		//----------------------------------------------
	
		System.out.printf("\n");
		
		while(start == true) {
			
		
			System.out.printf("javaChat>");
		
			try { 
		
				str = in.readLine(); 
		
			} catch (IOException e) { 
				
				System.out.println ("Si � verificato un errore: " + e);
				System.exit(-1); 
			
			}
	
			try {
				 JavaChatAdmin v = new JavaChatAdmin();
		         Class c = Class.forName("cli.javaChatAdmin");
		         Object r = c.getMethod(str).invoke(v);
		        }
		      catch (Throwable e) {
		         System.err.println(e);
		    }
		
		}
		
	
		//----------------------------------------------
		
		//System.out.println("Java Chat Admin Service Ends.");

	}
	
	public void help() {
		
		System.out.printf("javaChat Admin Menu.\n");
		System.out.printf("\n");
		System.out.printf("help      - this menu.\n");
		System.out.printf("userlist  - display user's list.\n");
		System.out.printf("topiclist - display topic's list.\n");
		System.out.printf("adduser   - create a new user.\n");
		System.out.printf("addutot   - add a user to a topic.\n");
		System.out.printf("addtopic  - add a new topic.\n");
		System.out.printf("exit      - exit from admin menu.\n");
		
	}
	/*
	public void userlist() {
		
			Hashtable users = admin.returnUsr();
		 	Enumeration e  = users.elements();
		 	int i = 0;
		 	
		 	while(e.hasMoreElements()) {
		 		
		 		//li stampa alla storta
		 		
		 		System.out.printf("%d - %s\n",i,((ChatUser)e.nextElement()).getUser());
		 		i++;
		 
		 	}		
		
	}
	
	public void topiclist() {
		
		Hashtable users = admin.returnTopics();
	 	Enumeration e  = users.elements();
	 	int i = 0;
	 	
	 	while(e.hasMoreElements()) {
	 		
	 		//li stampa alla storta
	 		
	 		System.out.printf("%d - %s\n",i,(e.nextElement()).toString());
	 		i++;
	 
	 	}		
	
	}
	
	public void adduser() throws Exception{
		
		System.out.printf("username:");
		str=in.readLine();
		System.out.printf("password:");
		str2=in.readLine();
		admin.createUser(str, str2);
		
	}
	
	//Add user to topic

    public void addutot() throws Exception{
		
		System.out.printf("username:");
		str=in.readLine();
		System.out.printf("topic:");
		str2=in.readLine();
		admin.addUserToTopic(str, str2);
		
	}
	
    
	public void addtopic() throws Exception{
		
		System.out.printf("topic:");
		str2=in.readLine();
		admin.createChat(str2);
		
	}*/

	public void exit() throws Exception{
		
		admin.disconnect();
		start = false;
		
	}
	
}
