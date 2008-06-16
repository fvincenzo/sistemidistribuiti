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
		System.out.println("Android Contact Server ver 0.14 final");
		
		clientSocket = s;
		um = new UserManager();
    	users = um.restore(); 
    	this.start();
		
	}
	
	public void run(){
    	
    	
    	try {
    		PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String s = null;
            
            //Server version
            out.println("Android Contact Server ver 0.14 final");
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
            			String username = in.readLine();
            			String password = in.readLine();
            			String mobile = in.readLine();
            			String home = in.readLine();
            			String work = in.readLine();
            			String mail = in.readLine();
            			String im = in.readLine();
            			
            			boolean res = register(username,password,mobile,home,work,mail,im);
            			
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
            		
            		if(s.equals("CHANGEPERSONAL")) {
            			
            			System.out.println("CHANGEPERSONAL");
            			String username = in.readLine();
            			String mobile = in.readLine();
            			String home = in.readLine();
            			String work = in.readLine();
            			String mail = in.readLine();
            			String im = in.readLine();
            			
            			result = changepersonal(username,mobile,home,work,mail,im);
            		
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
            		
            		if(s.equals("ADDFRIEND")) {
            			System.out.println("ADDFRIEND");
            			String u = in.readLine();
            			String p = in.readLine();
            			result = addfriend(u,p);
            		}
            		
            		if(s.equals("PENDINGFRIENDS")) {
            			System.out.println("PENDINGFRIENDS");
            			String p = in.readLine();
            			result = pendingfriends(p);
            		}
            		
            		if(s.equals("ACCEPTFRIEND")) {
            			System.out.println("ACCEPTFRIEND");
            			String u = in.readLine();
            			String p = in.readLine();
            			result = acceptfriend(u,p);
            		}
            		
            		if(s.equals("DENYFRIEND")) {
            			System.out.println("DENYFRIEND");
            			String u = in.readLine();
            			String p = in.readLine();
            			result = denyfriend(u,p);
            		}
            		
            		if(s.equals("GETUSERDATA")) {
            			System.out.println("GETUSERDATA");
            			String u = in.readLine();
            			String f = in.readLine();
            			result = getuserdata(u,f);
            		}
            		
            		if(s.equals("CHECKPOSITION")) {
            			System.out.println("CHECKPOSITION");
            			String u = in.readLine();
            			result = getposition(u);
            		}
            		
            		if(s.equals("SETPREFERRED")) {
            			System.out.println("SETPREFERRED");
            			String u = in.readLine();
            			String pos = in.readLine();
            			result = setpreferred(u, pos);
            		}
            		
            		if(s.equals("GETPREFERRED")) {
            			System.out.println("GETPREFERRED");
            			String u = in.readLine();
            			result = getpreferred(u);
            		}
            		
            	}
            	
            	out.println(result);
            	result = "";
            	
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
	
	public static boolean register(String username,String password,String mobile,String home,String work,String mail,String im) {
		
		//Aggiungo l'utente 
		users.add(new User(username,password,mobile,home,work,mail,im));
		//Salvo la lista
		um.save(users);
		//Loggo l'utente appena registrato
		return login(username,password);
		
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
	
	public static String changepersonal(String username,String mobile,String home,String work,String mail,String im) {
		
		Iterator<User> i = users.iterator();
		
		String result = "ERROR.";
		
		do {
			
			User u = (User)i.next();
			
			if((u.getUser().equals(username)==true) && (u.getConnected()==true)) {
				u.setMobile(mobile);
				u.setHome(home);
				u.setIm(im);
				u.setMail(mail);
				u.setWork(work);
				result = "0K.";
				break;
			}
			
		} while(i.hasNext());
		
		um.save(users);
		
		return result;
		
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
				
				result = "Connect Before. ERROR.";
				
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
				
				result = "Position Updated. OK.";
				
				break;
				
			} else if ((u.getUser().equals(uname)==true) && (u.getConnected() == false)) {
				
				result = "Connect Before. ERROR.";
				
			}
			
		} while(i.hasNext());
		
		um.save(users);
		
		return result;
		
	}

	//Alla richiesta di aggiunta di un amico questo viene aggiunto nella lista degli pending
	public static String addfriend(String uname,String friend) {
		
		Iterator<User> i = users.iterator();
		String result = "";
		
		do {
			
			User u = (User)i.next();
			
			if((u.getUser().equals(friend)==true)) {
				
				u.addPendings(uname);
				
				break;
				
			} 
			
		} while(i.hasNext());
		
		um.savefriends(friend,users);
		
		return result;
		
	}
	
	public static String pendingfriends(String uname) {
		
		Iterator<User> i = users.iterator();
		String result = "";
		
		do {
			
			User u = (User)i.next();
			
			if((u.getUser().equals(uname)==true) && (u.getConnected() == true)) {
				
				Iterator<String> it = u.listPendings().iterator();
				
				while(it.hasNext()) {
					
					result += it.next()+"$";
					
				}
				
				break;
				
			} else if ((u.getUser().equals(uname)==true) && (u.getConnected() == false)) {
				
				result = "Connect Before. ERROR.";
				
			}
			
		} while(i.hasNext());
		
		return result;
		
	}

	public static String acceptfriend(String uname,String friend) {
		
		Iterator<User> i = users.iterator();
		String result = "Request Denied. ERROR.";
		
		do {
			
			User u = (User)i.next();
			
			if((u.getUser().equals(uname)==true) && (u.getConnected() == true)) {
				
				u.addFriend(friend);
				u.removePenginds(friend);
				
				break;
				
			} else if ((u.getUser().equals(uname)==true) && (u.getConnected() == false)) {
				
				result = "Connect Before. ERROR.";
				
			}
			
		} while(i.hasNext());
		
		i = users.iterator();
		do {
			
			User u = (User)i.next();
			
			if((u.getUser().equals(friend)==true)) {
				
				u.addFriend(uname);
				u.removePenginds(uname);
				
				result = "OK.";
				
				break;
				
			} 
			
		} while(i.hasNext());
		
		//Salvo tutto
		um.savefriends(friend,users);
		
		return result;
		
	}
	
	public static String denyfriend(String uname,String friend) {
		
		Iterator<User> i = users.iterator();
		String result = "Request Denied. ERROR.";
		
		do {
			
			User u = (User)i.next();
			
			if((u.getUser().equals(uname)==true)) {
				
				u.removePenginds(friend);
				
				result = "OK.";
				
				break;
				
			} 
			
		} while(i.hasNext());
		
		um.savefriends(friend,users);
		
		return result;
		
	}
	
	public static String getuserdata(String uname, String friend) {
		
		Iterator<User> i = users.iterator();
		String result = "";
		//TODO: non sarebbe giusto controllare anche se friend ha nella sua lista di amici uname?
		if(getfriends(uname).contains(friend)) {
			
			do {
				
				User u = (User)i.next();
				
				if((u.getUser().equals(friend)==true)) {
					
					result = u.getUserInfo();
					
					break;
					
				} 
				
			} while(i.hasNext());
			
		} else {
			
			result = friend + "is not in your friends list. ERROR."; 
			
		}
		
		return result;
		
	}
	
	public static String getposition(String uname) {
		
		Iterator<User> i = users.iterator();
		String result = "";
		
		do {
			
			User u = (User)i.next();
			
			if((u.getUser().equals(uname)==true)) {
				
				result = u.getGeo();
				
				break;
				
			} 
			
		} while(i.hasNext());
		
		return result;
		
	}
	
	public static String setpreferred(String uname, String pos) {
		
		Iterator<User> i = users.iterator();
		String result = "ERROR Unexisting User.";
		
		do {
			
			User u = (User)i.next();
			
			if((u.getUser().equals(uname)==true)) {
				
				result = "OK Mode Changed.";
					
				u.setPreferredMode(pos);
				
				break;
				
			} 
			
		} while(i.hasNext());
		
		return result;
		
	}	
	
	public static String getpreferred(String uname) {
		
		Iterator<User> i = users.iterator();
		String result = "ERROR Unexisting User.";
		
		do {
			
			User u = (User)i.next();
			
			if((u.getUser().equals(uname)==true)) {
				
				result = u.getPreferredMode();
				
				break;
				
			} 
			
		} while(i.hasNext());
		
		return result;
		
	}	
	
}	