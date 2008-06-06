package android.server;

import java.net.*;
import java.util.Iterator;
import java.util.Vector;
import java.io.*;

public class SocketServer extends Thread{
    
	public static UserManager um;
	public static Vector<User> users;
	public static String result;
	
	private static Socket clientSocket;
	
	public SocketServer(Socket s) {
		
		clientSocket = s;
		um = new UserManager();
    	users = um.restore(); 
    	this.start();
		
	}
	
	public void run(){
    	
    	System.out.println("Android Contact Server ver 0.1");
    	
    	try {
    		PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String s = null;
            
            //Server version
            out.println("Android Contact Server ver 0.1");
            //Server Welcome Message
            out.println("Welcome in Darkstar Contact Server.");
            //Server Status
            out.println("Status: up and running.");
            
            while(true) {
            	
            	s = "casa";
            	
            	while(s.equals("END")==false) {
            		
            		
            		s = in.readLine();
            		
            		System.out.println("Command: "+s);
            		
            		if(s.equals("REGISTER")) {
            			
            			System.out.println("REGISTER");
            			String u = in.readLine();
            			String p = in.readLine();
            			String g = in.readLine();
            			
            			boolean res = register(u,p,g);
            			
            			if (res == true) {
            				result = "Register and Login OK.";
            			} else {
            				result = "Register ERROR.";
            			}
            			
            			
            			
            		}
            		
            		if(s.equals("LOGIN")) {
            			
            			System.out.println("LOGIN");
            			String u = in.readLine();
            			String p = in.readLine();
            			System.out.println("Username:"+u);
            			System.out.println("Password:"+p);
            			boolean res = login(u,p);
            			
            			if (res == true) {
            				result = "Login OK.";
            			} else {
            				result = "Login ERROR.";
            			}
            			
            		}
            		
            		if(s.equals("GETUSERS")) {
            			
            			System.out.println("GETUSERS");
            			result = getusers();
            			
            		}
            		
            		if(s.equals("GETFRIENDS")) {
            			
            			System.out.println("GETFRIENDS");
            			String u = in.readLine();
            			result = getfriends(u);
            			
            		}
            		
            		if(s.equals("UPDATEPOSITION")) {
            			
            			System.out.println("UPDATEPOSITION");
            			String u = in.readLine();
            			String p = in.readLine();
            			result = updateposition(u,p);
            			
            		}
            		
            		
            	}
            	
            	out.println(result);
            	
            }
            /*
            out.close();
            in.close();
            clientSocket.close();
            serverSocket.close();
            */
		} catch (Exception e) {
			// TODO: handle exception
		}

        
    }
	
	public static boolean register(String uname, String pwd, String geo) {
		
		//Aggiungo l'utente 
		users.add(new User(uname,pwd,geo));
		//Salvo la lista
		um.save(users);
		//Loggo l'utente appena registrato
		return login(uname,pwd);
		
	}
	
	public static boolean login(String uname, String pwd) {
		
		Iterator<User> i = users.iterator();
		
		do {
			
			User u = (User)i.next();
			
			if((u.getUser().equals(uname)==true) && (u.getPwd().equals(pwd))==true) {
				u.setConnected();
				return true;
			}
			
		} while(i.hasNext());
		
		return false;
		
	}
	
	public static String getusers() {
		
		String s = "";
		Iterator<User> i = users.iterator();
		
		do {
			
			User u = i.next();
			System.out.println(u.getUser());
			s += u.getUser()+"$";
			
		} while(i.hasNext());
		
		return s;
	}
	
	public static String getfriends(String uname) {
		
		Iterator<User> i = users.iterator();
		String result = "";
		
		do {
			
			User u = (User)i.next();
			
			if((u.getUser().equals(uname)==true) && (u.getConnected() == true)) {
				
				Iterator<String> it = u.listFriends().iterator();
				
				while(it.hasNext()) {
					
					result += it.next()+"$";
					
				}
				
				break;
				
			} else if ((u.getUser().equals(uname)==true) && (u.getConnected() == false)) {
				
				result = "Connect Before.";
				
			}
			
		} while(i.hasNext());
		
		return result;
		
	}
	
	public static String updateposition(String uname, String position){
		
		Iterator<User> i = users.iterator();
		String result = "";
		
		do {
			
			User u = (User)i.next();
			
			if((u.getUser().equals(uname)==true) && (u.getConnected() == true)) {
				
				u.setGeo(position);
				
				break;
				
			} else if ((u.getUser().equals(uname)==true) && (u.getConnected() == false)) {
				
				result = "Connect Before.";
				
			}
			
		} while(i.hasNext());
		
		return result;
		
	}
}