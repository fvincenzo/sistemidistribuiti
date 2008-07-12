package android.pc.client;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class PcClientAPI {
	
	private Socket kkSocket = null;
    private PrintWriter out = null;
    private BufferedReader in = null;
    private String Username;
	private String Password;
    private int connected = 0; 
	
	
	public PcClientAPI() {
		
	}
	
	public void connect() {
		
		try {
			BufferedReader i = new BufferedReader(new FileReader("conf/config.txt"));
			Username = i.readLine();
			Password = i.readLine();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		try {
            kkSocket = new Socket("localhost", 4444);
            out = new PrintWriter(kkSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(kkSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: darkstar.");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: darkstar.");
            System.exit(1);
        }
		
        connected = 1;
        
		//Lancio dei comandi
		out.println("FORCELOGIN");
		out.println(Username);
		out.println(Password);
		out.println("END");
		
	}
	
	public boolean mail() {
		
		if(connected == 1) {
			out.println("SETPREFERRED");
			out.println(Username);
			out.println("MAIL");
			out.println("END");
			return true;
		} else {
			return false;
		}
	}
	
	public boolean im() {
		
		if(connected == 1) {
			out.println("SETPREFERRED");
			out.println(Username);
			out.println("IM");
			out.println("END");
			return true;
		} else {
			return false;
		}
	}
	
	public void disconnect(){
	
		connected = 0;
		out.println("QUIT");
		out.println("END");
	}

}
