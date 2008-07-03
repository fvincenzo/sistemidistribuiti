package android.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UserManager {

	private Map<String ,User> users = new HashMap<String, User>();

	private PrintWriter out_users;

	private static UserManager u = null;

	private UserManager(){	

		FileReader usersFis = null;
		BufferedReader usersIn = null;
		try
		{
			usersFis = new FileReader(new File("users.slist"));
			usersIn = new BufferedReader(usersFis);
			String newUser = usersIn.readLine();
			while (newUser != null){
			User u = new User(newUser, usersIn.readLine(),usersIn.readLine(),usersIn.readLine(),usersIn.readLine(),usersIn.readLine(),usersIn.readLine(),usersIn.readLine(), usersIn.readLine());
				users.put(newUser, u);
				newUser = usersIn.readLine();
			}
			usersFis.close();
			usersIn.close();
		}
		catch(IOException ex)
		{
			System.out.println("No userfile found. Creating a new one...");
//			ex.printStackTrace();

		}
		
	/*	FileOutputStream file = null;
		out_users = null;
		try
		{
			file = new FileOutputStream("users.slist");
			out_users = new PrintWriter(file, true);
			out_users.close();
			file.close();

		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
*/
		for(User u : users.values()){
			u.load();
		}

	}

	public static UserManager getHinstance(){
		if (u == null) u = new UserManager();
		return u;

	}

	public boolean addUser(User u){
		if (!users.containsKey(u.getUser())){
			users.put(u.getUser(),u);
			commit();
			return true;
		}
//			if (users.put(u.getUser(), u)){
//				return commit();
//			}
		return false;
	}


	public User getUser(String uname){
		return users.get(uname);
	}

	public Set<String> getUsers() {
		return users.keySet();
	}

	public boolean commit(){
		
		try{
			PrintWriter out_users = new PrintWriter(new FileOutputStream("users.slist"), true);
		
//			out_users.reset();
			for (User u : users.values()){
				out_users.print(u.saveMe());
				out_users.flush();
				}
			out_users.close();
			}catch (IOException e){
				e.printStackTrace();
				return false;
			}
			return true;

		
	}

	/*
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


	}*/

/*	@Override
	public String acceptfriend(String uname, String friend) {
		User u = getUser(uname);
		u.removePenginds(friend);
		u.addFriend(friend);
		User f = getUser(friend);
		f.addFriend(friend);
		return null;
	}

	@Override
	public String addfriend(String uname, String friend) {
		User u = getUser(uname);
		u.addPendings(friend);
		return "OK";
	}

	@Override
	public String changepersonal(String username, String mobile, String home,
			String work, String mail, String im) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String denyfriend(String uname, String friend) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getfriends(String uname) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getposition(String uname) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getpreferred(String uname) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getuserdata(String uname, String friend) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override

	@Override
	public boolean login(String uname, String pwd) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String pendingfriends(String uname) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean register(String username, String password, String mobile,
			String home, String work, String mail, String im) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String setpreferred(String uname, String pos) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String updateposition(String uname, String position) {
		// TODO Auto-generated method stub
		return null;
	}
*/
}
