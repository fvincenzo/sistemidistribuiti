package framework.core.fs;

public class UFile {
	
	private String Owner;
	
	private String Name;
	
	private String Hash;
	
	private String IP;
	
	public UFile(String Owner,String IP,String Name,String Hash) {
		this.Owner = Owner;
		this.Name = Name;
		this.Hash = Hash;
		this.IP=IP;
	}
	

	public String getName() {
		return Name;
	}
	
	public String getOwner() {
		return Owner;
	}
	
	public String getHash() {
		return Hash;
	}
	
	public String getIP() {
		return IP;
	}
}
