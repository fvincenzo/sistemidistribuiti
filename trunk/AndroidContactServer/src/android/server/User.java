package android.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.*;

public class User {

	private String uname;
	private String pwd;
	private String geo;
	private boolean connected;
	
	private Vector<String> Friends;
	private Vector<String> Pendings;
	
	public User(String uname,String pwd, String geo) {
		
		this.uname = uname;
		this.pwd = pwd;
		this.geo = geo;
	
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
	
	public Vector<String> listPendings() {
		
		return Pendings;
		
	}
	
	public boolean getConnected() {
	
		return connected;
		
	}
	
	public void setConnected() {
		if(connected==true) {
			connected = false;
		} else {
			connected = true;
		}
	}
}
