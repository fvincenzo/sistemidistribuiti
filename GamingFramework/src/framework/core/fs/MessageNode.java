package framework.core.fs;

import java.util.Random;

public class MessageNode extends Node {
	
	public Message m;
	
	public MessageNode(String message) {
		
		Random a = new Random();
		int name = a.nextInt();
		String mname = new String(Integer.toString(name));
		setName("m"+mname);
		
		m = new Message(message);
		
		
	}
	
	
	public String getInfo() {
		
		return m.getMessage();
		
	}
	
	
}
