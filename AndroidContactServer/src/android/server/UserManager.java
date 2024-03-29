package android.server;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.Vector;

public class UserManager {

	public Vector<User> restore() {
		
		Vector<User> v = new Vector<User>();
		String username= " ";
		String password;
		String mobile;
		String home;
		String work;
		String mail;
		String im;
		String position;
		
		try
		{
		    // Open an input stream
			FileInputStream fin = new FileInputStream ("users.lst");

		    // Read a line of text
		    DataInputStream in = new DataInputStream(fin);
		    
		    //Carico la lista degli amici
		    FileInputStream frdin = null;
	    	DataInputStream frin = null;
	    	
	    	//Carico le richieste pendendi
	    	FileInputStream pndin = null;
	    	DataInputStream pnin = null;
	    	
		    username = in.readLine();
			password = in.readLine();
			mobile = in.readLine();
			home = in.readLine();
			work = in.readLine();
			mail = in.readLine();
			im = in.readLine();
			position = in.readLine();
		    
		    while(username != null) {
		    	
		    	String f;
		    	//System.out.println(s+p+g);
		    	//Carico l'utente
		    	User u = new User(username,password,mobile,home,work,mail,im,position);
		    	
		    	//Carico la lista degli amici
		    	frdin = new FileInputStream ("users/"+u.getUser()+".frd");
		    	frin = new DataInputStream(frdin);
		    	
		    	//System.out.println("1");
		    	
		    	while((f = frin.readLine()) != null)
		    		u.addFriend(f);
		    	
		    	//Carico le richieste pendendi
		    	pndin = new FileInputStream ("users/"+u.getUser()+".pnd");
		    	pnin = new DataInputStream(pndin);
		    	
		    	//System.out.println("1");
		    	
		    	while((f = pnin.readLine()) != null)
		    		u.addPendings(f);
		    	
		    	//System.out.println("2");
		    	
		    	//Aggiungo l'utente
		    	v.add(u);
		    	
		    	username = in.readLine();
				password = in.readLine();
				mobile = in.readLine();
				home = in.readLine();
				work = in.readLine();
				mail = in.readLine();
				im = in.readLine();
				position = in.readLine();
		    	
		    } 
		    	
		    
		    // Close our input stream
		    fin.close();		
		    pnin.close();
		    pndin.close();
		    frin.close();
		    frdin.close();
		    
		    return v;
		}
		// Catches any error conditions
		catch (IOException e)
		{
			System.err.println ("Unable to read from file");
			System.exit(-1);
		}
		
		return v;
		
	}
 	
	
	public void save(Vector<User> v) {
	
		Iterator<User> i = v.iterator();

		try {
		
			FileOutputStream fout = new FileOutputStream ("users.lst");
			PrintStream out = new PrintStream(fout);
			
			do {
				
				User s = (User)i.next();
				
				System.out.println("User:"+s.getUser());
				
				out.println(s.getUser());
				out.println(s.getPwd());
				out.println(s.getMobile());
				out.println(s.getHome());
				out.println(s.getWork());
				out.println(s.getMail());
				out.println(s.getIm());
				out.println(s.getGeo());
				
				
			} while(i.hasNext());
			
			out.close();
			fout.close();
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	public void savefriends(String username,Vector<User> v) {
		
		System.out.println("Save Friends");
		
		User s = null;
		User c = null;
		
		Iterator i = v.iterator();
		
		while(i.hasNext())
			if((c = (User) i.next()).getUser().equals(username))
				s = c;
		
		System.out.println(s.getUser());
		
		try {
			//Aggiorno la lista degli amici
			FileOutputStream frdout = new FileOutputStream ("users/"+s.getUser()+".frd");
			PrintStream frout = new PrintStream(frdout);
//			System.out.println("Sono qui");
			Vector<String> u = s.listFriends();
			
			if(!u.isEmpty()) {
				
				Iterator<String> it = u.iterator();
				System.out.println((String)it.next());
//				System.out.println("Sono qui");
				
				while(it.hasNext())
					frout.println((String)it.next());
				
			}
			
//			System.out.println("Sono qui");
			
			//Aggiorno la lista richieste pendendi
			FileOutputStream pndout = new FileOutputStream ("users/"+s.getUser()+".pnd");
			PrintStream pnout = new PrintStream(pndout);
			
			Vector<String> t = s.listPendings();
			
			if(!t.isEmpty()) {
				
				Iterator<String> pi = t.iterator();
			
				while(pi.hasNext())
					pnout.println((String)pi.next());
			
			}
			
			frout.close();
			frdout.close();
			pnout.close();
			pndout.close();
			
		} catch (Exception e) {
			System.out.println("UserManager.savefriends()"+e.toString());
		}
		
		
	}
	
}
