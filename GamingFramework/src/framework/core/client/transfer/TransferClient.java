package framework.core.client.transfer;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

import framework.core.client.ClientConfig;

public class TransferClient {

	private Socket s;
	private DataInputStream is;
	private PrintStream os;
	private ClientConfig cfg;
	
	public TransferClient() {
	
			this.s = null;
			this.is = null;
			this.os = null;
			try {
				cfg = ClientConfig.getClientConfig();
			} catch (IOException e) {
				//handle exception
				System.out.println(e.getMessage());
			}
			
	}
	
	public void reaquestFile(String IP, String Dfile) {
		
		try {
			s = new Socket(IP,Integer.parseInt(cfg.port));
			is = new DataInputStream(s.getInputStream());
			os = new PrintStream(new BufferedOutputStream(s.getOutputStream()));
		} catch (IOException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
		
		System.out.println("Socket creata:" + s);
		
		
		//controllo argomenti
		if(Dfile.isEmpty()){
			os.println("File Name non corretto");
			os.flush();
			os.close();
			try {
				is.close();
				s.close();
			} catch (IOException e) {
				//handle exception
				System.err.println(e.getMessage());
			}
			System.exit(1);
		}
		
		//invio il nome del File
		System.out.println("Richiesta " + Dfile);
		os.println(Dfile);
		os.flush();
		
		//stampo risposta del Server
		System.out.println("Attesa Risposta...");
		String line = null;
		try {
			while((line = is.readLine()) != null) {
				//per il momento lo stampo
				//System.out.println("Messaggio: " + line);
				//adesso ci siamo lo salvo anche
				FileOutputStream dload = new FileOutputStream(cfg.received+"/"+Dfile);
				PrintStream dlos = new PrintStream(dload, false);
				dlos.println(line);
			}
			is.close();
			os.close();
			s.close();
		} catch (IOException e) {
			//handle exception
			System.out.println(e.getMessage());
		}
		
	}
	
	
}
