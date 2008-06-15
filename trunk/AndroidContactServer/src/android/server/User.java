package android.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.*;

public class User {

	private String uname;
	private String pwd;
	private String mobile;
	private String work;
	private String home;
	private String mail;
	private String im;
	private String geo;
	private boolean connected;
	
	//Questa Stringa può assumere i valori HOME WORK MAIL IM o MOBILE, di default per ogni utente è settata ad HOME
	private String PreferredMode;
	
	private Vector<String> Friends;
	private Vector<String> Pendings;
	
	public User(String username,String password,String mobile,String home,String work,String mail,String im,String position) {
		
		this.uname = username;
		this.pwd = password;
		this.home = home;
		this.mobile = mobile;
		this.work = work;
		this.mail = mail;
		this.im = im;
		this.geo = position;
	
		this.PreferredMode = "HOME";
		
		Friends = new Vector<String>();
		Pendings = new Vector<String>();
		
		connected = false;
		
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
	
	public User(String username,String password,String mobile,String home,String work,String mail,String im) {
		
		this.uname = username;
		this.pwd = password;
		this.home = home;
		this.mobile = mobile;
		this.work = work;
		this.mail = mail;
		this.im = im;
		this.geo = "0";
	
		this.PreferredMode = "HOME";
		
		Friends = new Vector<String>();
		Pendings = new Vector<String>();
		
		connected = false;
		
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
	
	public String getUser() {
		
		return uname;
	
	}
	
	public String getPwd() {
		
		return pwd;
		
	}
	
	public void setGeo(String geo) {
		
		this.geo = geo;
		
	}
	
	public String getGeo() {
		
		return geo;
		
	}
	
	public void addFriend(String f) {
		
		Friends.add(f);
		
	}
	
	public Vector<String> listFriends() {
		
		return Friends;
		
	}
	
	public void addPendings(String pen) {
		
		Pendings.add(pen);
		
	}
	
	public void removePenginds(String pen) {
		
		Pendings.remove(pen);
		
	}
	
	public Vector<String> listPendings() {
		
		return Pendings;
		
	}
	
	public boolean getConnected() {
	
		return connected;
		
	}
	
	public String getUserInfo() {
		
		String user = this.uname+"$"+this.geo+"$"+this.mobile+"$"+this.work+"$"+this.home+"$"+this.mail+"$"+this.im;
		return user;
		
	}
	
	public void setConnected() {
		if(connected==true) {
			connected = false;
		} else {
			connected = true;
		}
	}

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
		return PreferredMode;
	}

	public void setPreferredMode(String preferredMode) {
		PreferredMode = preferredMode;
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
