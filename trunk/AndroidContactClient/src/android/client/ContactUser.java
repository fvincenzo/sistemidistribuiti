package android.client;

import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

public class ContactUser implements UserInterface {

	private String username;
	private String position;
	private String mobile;
	private String home;
	private String work;
	private String mail;
	private String IM;
	
	public ContactUser(String parseMe){
		StringTokenizer tok = new StringTokenizer(parseMe, "$");
		this.username = tok.nextToken();
		this.position = tok.nextToken();
		this.mobile = tok.nextToken();
		this.work = tok.nextToken();
		this.home = tok.nextToken();
		this.mail = tok.nextToken();
		this.IM = tok.nextToken();
		
	}
	
	public ContactUser(String usename, String position, String mobile,
			String home, String work, String mail, String im) {
		super();
		this.username = usename;
		this.position = position;
		this.mobile = mobile;
		this.home = home;
		this.work = work;
		this.mail = mail;
		this.IM = im;
	}
	
	public List<String> toList(){
		List<String> ret = new LinkedList<String>();
		ret.add(username);
		ret.add(mobile);
		ret.add(home);
		ret.add(work);
		ret.add(mail);
		ret.add(IM);
		ret.add(position);
		return ret;
	}
	/* (non-Javadoc)
	 * @see android.client.UserInterface1#getUsename()
	 */
	public String getUsename() {
		return username;
	}
	/* (non-Javadoc)
	 * @see android.client.UserInterface1#getPosition()
	 */
	public String getPosition() {
		return position;
	}
	/* (non-Javadoc)
	 * @see android.client.UserInterface1#getMobile()
	 */
	public String getMobile() {
		return mobile;
	}
	/* (non-Javadoc)
	 * @see android.client.UserInterface1#getHome()
	 */
	public String getHome() {
		return home;
	}
	/* (non-Javadoc)
	 * @see android.client.UserInterface1#getWork()
	 */
	public String getWork() {
		return work;
	}
	/* (non-Javadoc)
	 * @see android.client.UserInterface1#getMail()
	 */
	public String getMail() {
		return mail;
	}
	/* (non-Javadoc)
	 * @see android.client.UserInterface1#getIM()
	 */
	public String getIM() {
		return IM;
	}
}
