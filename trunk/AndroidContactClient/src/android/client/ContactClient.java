package android.client;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;

public class ContactClient implements ContactClientInterface {
	private PrintWriter out;
	private BufferedReader in;
	private Socket socket;
	private String username = "";
	private static ContactClient me = null;

	private ContactClient(){}

	public static ContactClient getHistance(){
		if (me == null){
			me = new ContactClient();
		}
		return me;
		
	}
	@Override
	public void acceptFriend( String friendName) {
		if (username != ""){
			out.println("DENYFRIEND");
			out.println(username);
			out.println(friendName);
			out.println("END");
		}	
	}



	@Override
	public boolean addFriend(String friendName) {
		if (username != ""){
			out.println("ADDFRIEND");
			out.println(username);
			out.println(friendName);
			out.println("END");
			String ret;
			try {
				ret = in.readLine();
				//TODO fare il parsing della stringa di ritorno

				return false;
			} catch (IOException e) {
				return false;
			}
		}
		return false;
	}

	@Override
	public void denyFriend(String friendName) {
		if (username != ""){
			out.println("DENYFRIEND");
			out.println(username);
			out.println(friendName);
			out.println("END");
		}	
	}

	@Override
	public Vector<String> getFriends() {
		if (username != ""){
			out.println("GETFRIENDS");
			out.println(username);
			out.println("END");
			String ret;
			try {
				ret = in.readLine();
				//TODO fare il parsing della stringa di ritorno

				return null;
			} catch (IOException e) {
				return null;
			}
		}
		return new Vector<String>();
	}

	@Override
	public UserInterface getUserDetails(String friend) {
		if (username != ""){
			//TODO controllare se il comando si chiama così davvero
			out.println("GETUSERSDETAILS");
			out.println(username);
			out.println(friend);
			out.println("END");
			String ret;
			try {
				ret = in.readLine();
				//TODO fare il parsing della stringa di ritorno

				return null;
			} catch (IOException e) {
				return null;
			}
		}
		return null;
	}

	@Override
	public Vector<String> getUsers() {
		if (username != ""){
			out.println("GETUSERS");
			out.println("END");
			String ret;
			try {
				ret = in.readLine();
				//TODO fare il parsing della stringa di ritorno

				return null;
			} catch (IOException e) {
				return null;
			}
		}
		return new Vector<String>();
	}

	@Override
	public boolean login(String uname, String pwd) {
		out.println("LOGIN");
		out.println(uname);
		out.println(pwd);
		out.println("END");
		String ret;
		try {
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
	public Vector<String> pendingFriends() {
		if (username != ""){
			out.println("PENDINGFRIENDS");
			out.println(username);
			out.println("END");
			String ret;
			try {
				ret = in.readLine();
				//TODO fare il parsing della stringa di ritorno

				return null;
			} catch (IOException e) {
				return null;
			}
		}
		return new Vector<String>();
	}

	@Override
	public boolean register(String uname, String pwd, String geo) {
		out.println("REGISTER");
		out.println(uname);
		out.println(pwd);
		//TODO controlare se effettivamente bisogna mettere il cancelletto dopo geo
		out.println(geo);
		out.println("END");
		String ret;
		try {
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


	public boolean register(String uname, String pwd, float x_position, float y_position) {
		out.println("REGISTER");
		out.println(uname);
		out.println(pwd);
		//TODO controlare se effettivamente bisogna mettere il cancelletto dopo geo
		out.println("geo:"+x_position+","+y_position);
		out.println("END");
		String ret;
		try {
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
	public boolean updatePosition( float x_position, float y_position) {
		if (username != ""){
			out.println("UPDATEPOSITION");
			out.println(username);
			out.println("geo:"+x_position+","+y_position);
			//TODO controlare se effettivamente bisogna mettere il cancelletto dopo geo
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
