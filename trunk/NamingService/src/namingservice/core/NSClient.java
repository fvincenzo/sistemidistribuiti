package namingservice.core;

import java.rmi.Naming;
import java.util.Vector;

/**
 * Classe NSClient Svolge le principali funzioni di connessione ad un servizio attivo
 * @author  Nicolas Tagliani e Vincenzo Frascino
 */
public class NSClient {

	/**
	 * @uml.property  name="r"
	 * @uml.associationEnd  
	 */
	private RemoteServer r;
	
	/**
	 * NSClient Costruttore di default
	 */
	public NSClient() {
		
	}
	
	/**
	 * Metodo connect serve a connettersi ad un servizio attivo
	 * 
	 * @param nome host a cui ci si connette
	 * @param Host servizio su cui e' registrato
	 * @return un oggetto remoto di tipo RemoteServer su cui poter invocare i principali messi a disposizione dal servizio di Naming
	 */
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
	
	/**
	 * Metodo register serve a registrare il proprio host presso un servizio di naming
	 * 
	 * @param nome indica il nome dell'host da registrare
	 * @param host indica il servizio di naming presso cui ci si registra
	 * @return un messaggio di avvenuta registrazione o di errore
	 */
	public String register(String nome,String host) {
		
		String s = null;
		
		try {
			s = r.add(nome,host,nome);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return s;
		
	}
	
	/**
	 * Metodo list consente di vedere gli host registrati al proprio livello 
	 * (metodo usato in fase di debug onde evitare di chiamare quello presente nell'interfaccia remota)
	 * 
	 * @return un vettore di stringhe contenete i nomi degli host
	 */
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
