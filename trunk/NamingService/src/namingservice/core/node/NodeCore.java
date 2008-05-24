package namingservice.core.node;

import java.io.Serializable;

public class NodeCore implements Serializable{

	private String HostID;
	private String HostIP;
	private static final long serialVersionUID = 1L;
	
	public NodeCore(String ID, String IP) {
		
		this.HostID = ID;
		this.HostIP = IP;
		
	}
	
	public String getID() {
		
		return this.HostID;
		
	}
	
	public String getIP() {
		
		return this.HostIP;
		
	}
	
}
