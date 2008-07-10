package android.client;

interface ServiceInterface {

	boolean register(String uname, String pwd, String mobile, String home, String work, String mail, String im);
 	boolean login(String uname, String pwd);
	List<String> getUsers();
	List<String> getFriends();
	boolean updatePosition(double x_position, double y_position);	
	boolean addFriend(String friendName);
	List<String> pendingFriends();
	void acceptFriend(String friendName);
	void denyFriend( String friendName);
	List<String> getUserDetails(String friend);
	boolean connect(String server);
	boolean changepersonal(String username,String oldpwd, String newPwd, String mobile,String home,String work,String mail,String im);
	List<String> getPersonal();
	boolean setpreferred(String username,String mode);
	String getpreferred(String username);
	
	
	void stop();
	boolean isRunning();
	boolean insertContact(String friend);
	boolean modifyContact(String friend);
	boolean setMobile(String contact);
	boolean setWork(String contact);
	boolean setHome(String contact);
	boolean setIm(String contact);
	boolean setMail(String contact);
	void notifyPendings();
	void setNormalStatus();
}