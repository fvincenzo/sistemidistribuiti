package android.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class User  {

	private String uname;
	private String pwd;
	private String mobile;
	private String work;
	private String home;
	private String mail;
	private String im;
	private String geo;
//	private boolean connected;

	//Questa Stringa può assumere i valori HOME WORK MAIL IM o MOBILE, di default per ogni utente è settata ad HOME
	private String preferredMode;

//	private transient Vector<String> Friends;
//	private transient Vector<String> Pendings;

	private Set<String> friends = new HashSet<String>();
	private Set<String> pendings = new HashSet<String>();
//	private PrintWriter pendings_out;
//	private PrintWriter friends_out;
	private Object pendingsLock = new Object();
	private Object friendsLock = new Object();

	public User(String username,String password,String mobile,String home,String work,String mail,String im,String lastPosition, String preferred) {

		this.uname = username;
		this.pwd = password;
		this.home = home;
		this.mobile = mobile;
		this.work = work;
		this.mail = mail;
		this.im = im;
		this.geo = lastPosition;

		this.preferredMode = preferred;

//		Friends = new Vector<String>();
//		Pendings = new Vector<String>();

//		connected = false;


//		initWrite();

		/*
	File f = new File("users/"+getUser()+".frd");
	//System.out.println("users/"+getUser()+".frd exists:"+f.exists());

	if(!f.exists()) {
		//Creo il file degli amici
		try {
			FileOutputStream frdout = new FileOutputStream ("users/"+getUser()+".frd");
			PrintStream frout = new PrintStream(frdout);
			frout.close();
			frdout.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	File g = new File("users/"+getUser()+".pnd");
	//System.out.println("users/"+getUser()+".frd exists:"+f.exists());

	if(!g.exists()) {
		//Creo il file degli amici
		try {
			FileOutputStream frdout = new FileOutputStream ("users/"+getUser()+".pnd");
			PrintStream frout = new PrintStream(frdout);
			frout.close();
			frdout.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}*/


	}
	/*
	public User(String username,String password,String mobile,String home,String work,String mail,String im) {

		this.uname = username;
		this.pwd = password;
		this.home = home;
		this.mobile = mobile;
		this.work = work;
		this.mail = mail;
		this.im = im;
		this.geo = "@0.00,0.00";

		this.PreferredMode = "HOME";

//		Friends = new Vector<String>();
//		Pendings = new Vector<String>();

		connected = false;


		initWrite();


		/*
		File f = new File("users/"+getUser()+".frd");
		//System.out.println("users/"+getUser()+".frd exists:"+f.exists());

		if(!f.exists()) {
			//Creo il file degli amici
			try {
				FileOutputStream frdout = new FileOutputStream ("users/"+getUser()+".frd");
				PrintStream frout = new PrintStream(frdout);
				frout.close();
				frdout.close();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		File g = new File("users/"+getUser()+".pnd");
		//System.out.println("users/"+getUser()+".frd exists:"+f.exists());

		if(!g.exists()) {
			//Creo il file degli amici
			try {
				FileOutputStream frdout = new FileOutputStream ("users/"+getUser()+".pnd");
				PrintStream frout = new PrintStream(frdout);
				frout.close();
				frdout.close();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

	}
	 */
	/*
	private void initWrite(){

		FileOutputStream pendings_file = null;
		pendings_out = null;
		try
		{
			pendings_file = new FileOutputStream("users/"+getUser()+".spnd");
			pendings_out = new PrintWriter(pendings_file, true);
//			pendings_out.writeObject(pendings);

		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}



		FileOutputStream friends_file = null;
		friends_out = null;
		try
		{
			friends_file = new FileOutputStream("users/"+getUser()+".sfrn");
			friends_out = new PrintWriter(friends_file, true);

		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}
	 */
	public void load() {



		try
		{
			FileReader friendsFis = null;
			BufferedReader friendsIn = null;
			friendsFis = new FileReader("users/"+getUser()+".sfrn");
			friendsIn = new BufferedReader(friendsFis);
			String friend = friendsIn.readLine();
			while (friend != null){
				friends.add(friend);
			}

			friendsIn.close();
			friendsFis.close();
		} catch (IOException e) {
		}
		try {
			FileReader pendingsFis = null;
			BufferedReader pendingsIn = null;

			pendingsFis = new FileReader("users/"+getUser()+".spnd");
			pendingsIn = new BufferedReader(pendingsFis);
			String pending = pendingsIn.readLine();
			while (pending != null){
				pendings.add(pending);
			}
			pendingsIn.close();
			pendingsFis.close();

//			initWrite();
//			return true;
		}
		catch (IOException e){
//			return false;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof User) {
			if ( ((User)obj).uname.equals(uname))
				return true;

		}
		return false;
	}

	public void addFriend(String f) {
		synchronized (friendsLock) {

//			try {
			friends.add(f);

			try {
				PrintWriter friends_out = new PrintWriter(new FileOutputStream("users/"+getUser()+".sfrn"), true);

				friends_out.append(f+"\n");
				friends_out.flush();
				friends_out.close();
			}
			catch (IOException e){
			}

		}
//		friends_out.reset();
//		friends_out.append(f+"\n");
//		friends_out.flush();
//		Friends.add(f);


	}
	public Set<String> getFriends(){
		return friends;
	}

	public Set<String> getPendings(){
		return pendings;
	}

//	public Vector<String> listFriends() {

//	return Friends;

//	}

	public void addPendings(String pen) {
		synchronized (pendingsLock) {

			pendings.add(pen);
//			friends_out.reset();
			try {
				PrintWriter pendings_out = new PrintWriter(new FileOutputStream("users/"+getUser()+".spnd"), true);

				pendings_out.append(pen+"\n");
				pendings_out.flush();
				pendings_out.close();
			}
			catch (IOException e){

			}
		}
	}

	public void removePenginds(String pen) {
		synchronized (pendingsLock) {

			try {
				pendings.remove(pen);
				File pendings = new File("users/"+getUser()+".spnd");
				pendings.delete();
//				pendings_out.reset();
//				pendings_out.writeObject(friends);
				PrintWriter pendings_out = new PrintWriter(new FileOutputStream("users/"+getUser()+".spnd"), true);
				for (String s: this.pendings){
					pendings_out.println(s);
				}
				pendings_out.flush();
				pendings_out.close();
//				Pendings.remove(pen);
			} catch (IOException e){
				e.printStackTrace();
			}
		}
	}

//	public Vector<String> listPendings() {

//	return Pendings;

	@Override
	public String toString() {

		String user = this.uname+"$"+this.geo+"$"+this.mobile+"$"+this.work+"$"+this.home+"$"+this.mail+"$"+this.im;
		return user;

	}

	public String saveMe(){
		return		 	uname+"\n"+
		pwd+"\n"+
		mobile+"\n"+
		work+"\n"+
		home+"\n"+
		mail+"\n"+
		im+"\n"+
		geo+"\n"+
		preferredMode+"\n";


	}


	//		public Vector<String> listPendings() {

	//		return Pendings;

	//		}
	/*
				public boolean getConnected() {

					return connected;

				}
	 */
	public String getUserInfo() {

		String user = this.uname+"$"+this.geo+"$"+this.mobile+"$"+this.work+"$"+this.home+"$"+this.mail+"$"+this.im;
		return user;

	}

	public String getUser() {

		return uname;

	}

	public String getPwd() {

		return pwd;

	}

	public String getGeo() {

		return geo;

	}

	public String getPersonal(){
		return uname+"$"+mobile+"$"+work+"$"+home+"$"+mail+"$"+im;
	}



	/*	
		public void setConnected() {
			if(connected==true) {
				connected = false;
			} else {
				connected = true;
			}
		}
	 */
	public String getMobile() {
		return mobile;
	}

	public String getWork() {
		return work;
	}

	public String getHome() {
		return home;
	}

	public String getMail() {
		return mail;
	}

	public String getIm() {
		return im;
	}

	public String getPreferredMode() {
		return preferredMode;
	}

	public void setPreferredMode(String preferredMode) {
		this.preferredMode = preferredMode;
	}
	public void setGeo(String geo) {

		this.geo = geo;

	}

	public void setPwd(String pwd){
		this.pwd = pwd;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public void setWork(String work) {
		this.work = work;
	}

	public void setHome(String home) {
		this.home = home;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public void setIm(String im) {
		this.im = im;
	}
}
