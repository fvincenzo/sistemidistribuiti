package android.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.StringTokenizer;
import java.util.Vector;

public class ContactClient implements ContactClientInterface {
	
	
	private PrintWriter out;
	private BufferedReader in;
	private Socket socket;
	private String username = "";
	private static ContactClient me = null;

	public static ContactClient getHistance(){
		if (me == null){
			me = new ContactClient();
		}
		return me;

	}

	private ContactClient(){
		
	}

	@Override
	public boolean register(String uname, String pwd, String mobile,
			String home, String work, String mail, String im, double x_position,
			double y_position) {
		try {
			in.readLine();
			in.readLine();
			in.readLine();
			out.println("REGISTER");
			out.println(uname);
			out.println(pwd);
			if (mobile == null) mobile = " ";
			out.println(mobile);
			if (home == null) home = " ";
			out.println(home);
			if (work == null) work = " ";
			out.println(work);
			if (mail == null) mail = " ";
			out.println(mail);
			if (im == null) im = " ";
			out.println(im);
			out.println("geo:"+x_position+","+y_position);
			out.println("END");
			String ret;
			ret = in.readLine();
			if (ret.contains("OK")){
				this.username = uname;
				return true;
			}
			return false;
		} catch (IOException e) {
			return false;
		}
	}

	@Override
	public boolean login(String uname, String pwd) {
		try {
			in.readLine();
			in.readLine();
			in.readLine();
			out.println("LOGIN");
			out.println(uname);
			out.println(pwd);
			out.println("END");
			String ret;
			ret = in.readLine();
			if (ret.contains("OK")){
				this.username = uname;
				return true;
			}
			return false;
		} catch (IOException e) {
			return false;
		}
	}

	@Override
	public boolean addFriend(String friendName) {
		try{
			if (username != ""){
				out.println("ADDFRIEND");
				out.println(username);
				out.println(friendName);
				out.println("END");
				return true;
			}
			return false;
		}
		catch (Exception e){
			return false;
		}
	}

	@Override
	public void denyFriend(String friendName) {
		try {
			if (username != ""){
				out.println("DENYFRIEND");
				out.println(username);
				out.println(friendName);
				out.println("END");
			}	
		} catch(Exception e){

		}
	}

	@Override
	public void acceptFriend( String friendName) {
		try{
			if (username != ""){
				out.println("ACCEPTFRIEND");
				out.println(username);
				out.println(friendName);
				out.println("END");
			}	
		}catch (Exception e){

		}
	}

	@Override
	public Vector<String> getFriends() {
		Vector<String> retV = new Vector<String>();
		if (username != ""){
			out.println("GETFRIENDS");
			out.println(username);
			out.println("END");
			String ret;
			try {
				ret = in.readLine();
				StringTokenizer tok =new StringTokenizer(ret, "$");
				while (tok.hasMoreTokens()) {
					retV.add(tok.nextToken());
				}
				return retV;
			} catch (IOException e) {
				return retV;
			}
		}
		return retV;
	}

	@Override
	public UserInterface getUserDetails(String friend) {
		try {
			if (username != ""){
				out.println("GETUSERDATA");
				out.println(friend);
				out.println("END");
				String ret;
				ret = in.readLine();
				return new ContactUser(ret);
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}


	@Override
	public Vector<String> getUsers() {
		Vector<String> retV = new Vector<String>();
		if (username != ""){
			out.println("GETUSERS");
			out.println("END");
			String ret;
			try {
				ret = in.readLine();
				StringTokenizer tok =new StringTokenizer(ret, "$");
				while (tok.hasMoreTokens()) {
					retV.add(tok.nextToken());
				}
				return retV;
			} catch (IOException e) {
				return retV;
			}
		}
		return retV;
	}

	@Override
	public Vector<String> pendingFriends() {
		Vector<String> retV = new Vector<String>();
		if (username != ""){
			out.println("PENDINGFRIENDS");
			out.println(username);
			out.println("END");
			String ret;
			try {
				ret = in.readLine();
				StringTokenizer tok =new StringTokenizer(ret, "$");
				while (tok.hasMoreTokens()) {
					retV.add(tok.nextToken());
				}
				return retV;
			} catch (IOException e) {
				return retV;
			}
		}
		return retV;
	}

	@Override
	public boolean updatePosition( double x_position, double y_position) {
		if (username != ""){
			out.println("UPDATEPOSITION");
			out.println(username);
			out.println("geo:"+x_position+","+y_position);
			out.println("END");
			String ret;
			try {
				ret = in.readLine();
				if (ret.contains("OK")){
					return true;
				}
				return false;
			} catch (IOException e) {
				return false;
			}
		}
		return false;
	}

	public void connect(String server) throws UnknownHostException, IOException{
		socket = new Socket(server, 4444);
		out = new PrintWriter(socket.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		
	}
}
