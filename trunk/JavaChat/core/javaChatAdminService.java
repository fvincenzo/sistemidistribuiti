/**
 * 
 */
package core;

import org.objectweb.joram.client.jms.admin.*;
import org.objectweb.joram.client.jms.*;
import org.objectweb.joram.client.jms.tcp.*;
import java.util.*;


/**
 * @author Vincenzo Frascino
 *
 */
public class javaChatAdminService {
	
	private javax.jms.ConnectionFactory connFactory;
	private javax.naming.Context jndiCtx;
	private int topicValue = 0;
	private userManager ute = userManager.creator();
	private Hashtable toN = new Hashtable();
	private Hashtable nTo = new Hashtable();
	
	public javaChatAdminService(String userName,String password) {

	    try {
	    
	    	AdminModule.connect("localhost", 16010, userName, password,60);
	    
	    } catch(Exception e) {
	    	
	    	System.out.printf("%s", e.toString());
	    	
	    }
       
	    connFactory = TcpConnectionFactory.create("localhost", 16010);
	    
	    try {
	    	
	    	jndiCtx = new javax.naming.InitialContext();
	    	jndiCtx.bind("JavaChat", connFactory);
	    
	    } catch (Exception e) {
	    	
	    	System.out.printf("%s", e.toString());
	    	
	    }
	    
		
	}
	
	public void createUser(String userName,String password) {
		
		ute.addUser(userName, password);
		
	}
	
	public Hashtable returnUsr() {
		
		return ute.returnUserList();
		
	}
	
	public Hashtable returnTopics() {
		
		return nTo;
		
	}
	
	public void createAdmin(String userName,String password) {
		
		//To Be Implemented
		
	}
	
	public void createTopic(String T) throws Exception{
		
		Topic topic = (Topic) Topic.create(this.topicValue);
		toN.put(T, String.valueOf(this.topicValue));
		nTo.put(String.valueOf(this.topicValue), T);
		this.topicValue++;
		
	    User user = User.create("anonymous", "anonymous", 0);

	    topic.setFreeReading();
	    topic.setFreeWriting();
		
	    jndiCtx.bind(T , topic);
	    
	}
	
	public void addUserToTopic(String usr,String T) {
		
		try {
			
			User user = User.create(usr, ute.returnUserPwd(usr),Integer.parseInt((String)toN.get(T)));
		
		} catch (Exception e) {
			
			System.out.printf("%s", e.toString());
			
		}
		
	}
	
	public void disconnect() {
		
		try {
			
			jndiCtx.close();
			AdminModule.disconnect();
		    
		} catch (Exception e) {
			
			System.out.printf("%s", e.toString());
			
		}
		
	}

}
