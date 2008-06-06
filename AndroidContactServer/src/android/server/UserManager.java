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
		String s = " ";
		String p;
		String g;
		
		try
		{
		    // Open an input stream
			FileInputStream fin = new FileInputStream ("users.lst");

		    // Read a line of text
		    DataInputStream in = new DataInputStream(fin);
		    
		    s = in.readLine();
	    	p = in.readLine();
	    	g = in.readLine();
		    
		    while(s != null) {
		    	
		    	String f;
		    	//System.out.println(s+p+g);
		    	//Carico l'utente
		    	User u = new User(s,p,g);
		    	//Carico la lista degli amici
		    	FileInputStream frdin = new FileInputStream ("users/"+u.getUser()+".frd");
		    	DataInputStream frin = new DataInputStream(frdin);
		    	
		    	//System.out.println("1");
		    	
		    	while((f = frin.readLine()) != null)
		    		u.addFriend(f);
		    	
		    	//Carico le richieste pendendi
		    	FileInputStream pndin = new FileInputStream ("users/"+u.getUser()+".pnd");
		    	DataInputStream pnin = new DataInputStream(pndin);
		    	
		    	//System.out.println("1");
		    	
		    	while((f = pnin.readLine()) != null)
		    		u.addPendings(f);
		    	
		    	//System.out.println("2");
		    	
		    	//Aggiungo l'utente
		    	v.add(u);
		    	
		    	s = in.readLine();
		    	p = in.readLine();
		    	g = in.readLine();
		    	
		    } 
		    	
		    
		    // Close our input stream
		    fin.close();		
		    
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
				
				User s = i.next();
				
				out.println(s.getUser());
				out.println(s.getPwd());
				out.println(s.getGeo());
				
				//Aggiorno la lista degli amici
				FileOutputStream frdout = new FileOutputStream ("users/"+s.getUser()+".frd");
				PrintStream frout = new PrintStream(frdout);
				
				Vector<String> u = s.listFriends();
				
				Iterator it = u.iterator();
				
				while(it.hasNext())
					frout.println((String)it.next());
				
				//Aggiorno la lista richieste pendendi
				FileOutputStream pndout = new FileOutputStream ("users/"+s.getUser()+".pnd");
				PrintStream pnout = new PrintStream(pndout);
				
				Vector<String> t = s.listPendings();
				
				Iterator pi = u.iterator();
				
				while(pi.hasNext())
					pnout.println((String)pi.next());
				
				
				
			} while(i.hasNext());
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
}
