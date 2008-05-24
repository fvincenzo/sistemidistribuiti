package namingservice.core;

import java.net.InetAddress;
import java.rmi.Naming;
import java.util.Vector;

public class NSClient {

	private RemoteServer r;
	
	public NSClient() {
		
	}
	
	public RemoteServer connect(String nome, String Host) {
		
		String url = "//"+Host+"/"+nome;
		
		r = null;
		
		try {
			
			r = (RemoteServer) Naming.lookup(url);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return r;
		
	}
	
	public String register(String nome,String host) {
		
		String s = null;
		
		try {
			s = r.add(nome,host,nome);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return s;
		
	}
	
	public Vector<String> list() {
		
		Vector<String> v = null;
		
		try {
			v = r.getlist();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return v;
		
	}
	
}
