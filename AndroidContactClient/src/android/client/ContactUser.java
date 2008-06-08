package android.client;

public class ContactUser implements UserInterface {

	private String usename;
	private String position;
	private String mobile;
	private String home;
	private String work;
	private String mail;
	private String IM;
	
	public ContactUser(String parseMe){
		
	}
	
	public ContactUser(String usename, String position, String mobile,
			String home, String work, String mail, String im) {
		super();
		this.usename = usename;
		this.position = position;
		this.mobile = mobile;
		this.home = home;
		this.work = work;
		this.mail = mail;
		this.IM = im;
	}
	/* (non-Javadoc)
	 * @see android.client.UserInterface1#getUsename()
	 */
	public String getUsename() {
		return usename;
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
