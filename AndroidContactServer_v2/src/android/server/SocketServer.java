package android.server;

import java.net.*;
import java.util.Vector;
import java.io.*;

public class SocketServer extends Thread{

//	public static UserManager um;
//	public static Vector<User> users;
	private  String result;

//	private boolean connected;
	private Socket clientSocket;
	private User u;
	private UserManager um = UserManager.getHinstance();
	private boolean run = true;
	private boolean logged;
	private String username;

	public SocketServer(Socket s) {
		System.out.println("Android Contact Server ver 0.15 final");

		clientSocket = s;
//		um = new UserManager();
//		users = um.restore(); 


	}

	public void run(){


		try {
			Vector<String> command = new Vector<String>();
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//			String s = null;

			//Server version
			out.println("Android Contact Server ver 0.15 final");
			//Server Welcome Message
			out.println("Welcome in Darkstar Contact Server.");
			//Server Status
			out.println("Status: up and running.");
			int size = 0;
			while(run) {

				command.add(in.readLine());

				while(!command.lastElement().equals("END")) {
					size ++;
					command.add(in.readLine());
				}


				System.out.println("Command: "+command);

				if(command.firstElement().equals("REGISTER") && command.size() > 7) {

					result = register(command.get(1),command.get(2),command.get(3),command.get(4),command.get(5),command.get(6),command.get(7));
					out.println(result);


				}
				else 
					if(command.firstElement().equals("LOGIN") && command.size() > 2) {

						result = login(command.get(1),command.get(2));
						out.println(result);
					}else 
						if(command.firstElement().equals("FORCELOGIN") && command.size() > 2) {

							result = forcelogin(command.get(1),command.get(2));
							out.println(result);
						}
						else
							if(command.firstElement().equals("CHANGEPERSONAL") && command.size() > 8) {

								result = changePersonal(command.get(1),command.get(2),command.get(3),command.get(4),command.get(5),command.get(6), command.get(7),command.get(8));
								out.println(result);
							}
							else
								if(command.firstElement().equals("GETPERSONAL") && command.size() > 1) {

									result = getPersonal(command.get(1));
									out.println(result);
								}
								else
									if(command.firstElement().equals("GETUSERS") && command.size() > 0) {

//										System.out.println("GETUSERS");
										result = getusers();
										out.println(result);
									}
									else
										if(command.firstElement().equals("GETFRIENDS") && command.size() > 1) {

//											System.out.println("GETFRIENDS");
//											String u = in.readLine();
											result = getfriends(command.get(1));
											out.println(result);
										}
										else
											if(command.firstElement().equals("UPDATEPOSITION") && command.size() > 2) {

//												System.out.println("UPDATEPOSITION");
//												String u = in.readLine();
//												String p = in.readLine();
												result = updateposition(command.get(1),command.get(2));
												out.println(result);
											}
											else
												if(command.firstElement().equals("ADDFRIEND") && command.size() > 2) {
//													System.out.println("ADDFRIEND");
//													String u = in.readLine();
//													String p = in.readLine();
													result = addfriend(command.get(1),command.get(2));
													out.println(result);
												}
												else
													if(command.firstElement().equals("PENDINGFRIENDS") && command.size() > 1) {
//														System.out.println("PENDINGFRIENDS");
//														String p = in.readLine();
														result = pendingfriends(command.get(1));
														out.println(result);
													}
													else
														if(command.firstElement().equals("ACCEPTFRIEND") && command.size() > 2) {
//															System.out.println("ACCEPTFRIEND");
//															String u = in.readLine();
//															String p = in.readLine();
															result = acceptfriend(command.get(1),command.get(2));
															out.println(result);
														}
														else
															if(command.firstElement().equals("DENYFRIEND") && command.size() > 2) {
//																System.out.println("DENYFRIEND");
//																String u = in.readLine();
//																String p = in.readLine();
																result = denyfriend(command.get(1),command.get(2));
																out.println(result);
															}
															else
																if(command.firstElement().equals("GETUSERDATA") && command.size() > 2) {
//																	System.out.println("GETUSERDATA");
//																	String u = in.readLine();
//																	String f = in.readLine();
																	result = getuserdata(command.get(1),command.get(2));
																	out.println(result);
																}
																else
																	if(command.firstElement().equals("CHECKPOSITION") && command.size() > 2) {
//																		System.out.println("CHECKPOSITION");
//																		String u = in.readLine();
																		result = getposition(command.get(1),command.get(2));
																		out.println(result);
																	}
																	else
																		if(command.firstElement().equals("SETPREFERRED") && command.size() > 2) {
//																			System.out.println("SETPREFERRED");
//																			String u = in.readLine();
//																			String pos = in.readLine();
																			result = setpreferred(command.get(1),command.get(2));
																			out.println(result);
																		}
																		else
																			if(command.firstElement().equals("GETPREFERRED") && command.size() > 2) {
//																				System.out.println("GETPREFERRED");
//																				String u = in.readLine();
																				result = getpreferred(command.get(1),command.get(2));
																				out.println(result);
																			} 
																			else
																				if (command.firstElement().equals("QUIT")){
																					System.out.println("Closing thread: user "+username+" sent QUIT command.");
																					quit();
																				}
				System.out.println("Returning: "+result);
				for ( ; size > 0; size--){
					command.remove(0);
				}
				command.remove(0);


//				out.println(result);
//				System.out.println("Ritorno al client: "+result);
//				result = "";

			}

			/*
            out.close();
            in.close();
            clientSocket.close();
            serverSocket.close();
			 */
		} catch (Exception e) {
			// TODO: handle exception
		}


	}
	public void quit(){
		try {
			run = false;
			clientSocket.close();
		} catch (IOException e) {

		}
	}

	/* (non-Javadoc)
	 * @see android.server.ServerInterface#register(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public  String register(String username,String password,String mobile,String home,String work,String mail,String im) {

		if (!logged){
			u = new User(username,password,mobile,home,work,mail,im, "@0.0,0.0", "MOBILE");
			if (um.addUser(u)){
//				um.commit();
				return login(username,password);
			}
		}
		return "Register ERROR.";
	}

	/* (non-Javadoc)
	 * @see android.server.ServerInterface#login(java.lang.String, java.lang.String)
	 */
	public  String login(String uname, String pwd) {
		User u;
		if (!logged){
			if ((u =um.getUser(uname)) != null){
				if (u.getPwd().equals(pwd)){
					logged = true;
					if(! um.unameConnected(uname)) {
						um.setConnected(uname, this);
						username = uname;
						return "Login OK.";
					}
				}
			}
		}
		return "Login ERROR.";
		/*	Iterator<User> i = users.iterator();

		do {

			User u = (User)i.next();

			if((u.getUser().equals(uname)==true) && (u.getPwd().equals(pwd))==true) {
				u.setConnected();
				return true;
			}

		} while(i.hasNext());

		return false;
		 */
	}

	public  String forcelogin(String uname, String pwd) {
		User u;
		if (!logged){
			if ((u =um.getUser(uname)) != null){
				if (u.getPwd().equals(pwd)){
					logged = true;
					if(! um.unameConnected(uname)) {
						um.setConnected(uname, this);
					} else {
						um.removeUnameConnected(uname);
						um.setConnected(uname, this);
					}
					username = uname;
					return "Login OK.";
				}
			}
		}
		return "Login ERROR.";
		/*	Iterator<User> i = users.iterator();

		do {

			User u = (User)i.next();

			if((u.getUser().equals(uname)==true) && (u.getPwd().equals(pwd))==true) {
				u.setConnected();
				return true;
			}

		} while(i.hasNext());

		return false;
		 */
	}

	/* (non-Javadoc)
	 * @see android.server.ServerInterface#changepersonal(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	/*public  String changepersonal(String username,String mobile,String home,String work,String mail,String im) {
		User u = null;
		String result = "Update ERROR.";
		if (logged && username.equals(this.username) &&(u = um.getUser(username)) != null){
			u.setMobile(mobile);
			u.setHome(home);
			u.setIm(im);
			u.setMail(mail);
			u.setWork(work);
			um.commit();
			result = "Update OK.";
		}
		return result;


	}*/

	/* (non-Javadoc)
	 * @see android.server.ServerInterface#getusers()
	 */
	public  String getusers() {
		String ret = "";
		if (logged){
			for (String s : um.getUsers()){
				ret += s+"$";
			}
		}
		return ret;
	}

	/* (non-Javadoc)
	 * @see android.server.ServerInterface#getfriends(java.lang.String)
	 */
	public  String getfriends(String uname) {
		String ret = "";
		if (logged && uname.equals(username)){
			User u = um.getUser(uname);
			if (u!= null){
				for(String s : u.getFriends()){
					ret += s+"$";
				}
			}
		}
		return ret;
	}

	/* (non-Javadoc)
	 * @see android.server.ServerInterface#updateposition(java.lang.String, java.lang.String)
	 */
	public  String updateposition(String uname, String position){

		String ret = "Update ERROR";
		if (logged && uname.equals(username)){
			User u = um.getUser(uname);
			if (u!= null){
				u.setGeo(position);
				um.commit();
				ret = "Update OK.";
			}
		}
		return ret;
	}
	/*		Iterator<User> i = users.iterator();
		String result = "";

		do {

			User u = (User)i.next();

			if((u.getUser().equals(uname)==true) && (u.getConnected() == true)) {

				u.setGeo(position);

				result = "Position Updated. OK.";

				break;

			} else if ((u.getUser().equals(uname)==true) && (u.getConnected() == false)) {

				result = "Connect Before. ERROR.";

			}

		} while(i.hasNext());

		um.save(users);

		return result;

	}*/

	//Alla richiesta di aggiunta di un amico questo viene aggiunto nella lista degli pending
	/* (non-Javadoc)
	 * @see android.server.ServerInterface#addfriend(java.lang.String, java.lang.String)
	 */
	public  String addfriend(String uname,String friend) {

		String ret = "Friend not added. ERROR.";
		if (logged && uname.equals(username)){
			User u = um.getUser(friend);
			if (u!= null){
				u.addPendings(uname);
				um.commit();
				ret = "Friend added. OK.";
			}
		}
		return ret;
	}
	/*	}
		Iterator<User> i = users.iterator();
		String result = "";

		do {

			User u = (User)i.next();

			if((u.getUser().equals(friend)==true)) {

				u.addPendings(uname);
				result = "Friend added to pending list. OK.";
				break;

			} 

		} while(i.hasNext());

		um.savefriends(friend,users);

		return result;

	}*/

	/* (non-Javadoc)
	 * @see android.server.ServerInterface#pendingfriends(java.lang.String)
	 */
	public  String pendingfriends(String uname) {
		String ret = "";
		if (logged && uname.equals(username)){
			User u = um.getUser(uname);
			if (u!= null){
				for(String s : u.getPendings()){
					ret += s+"$";
				}
			}
		}
		return ret;

		/*Iterator<User> i = users.iterator();
		String result = "";

		do {

			User u = (User)i.next();

			if((u.getUser().equals(uname)==true) && (u.getConnected() == true)) {

				Iterator<String> it = u.listPendings().iterator();

				while(it.hasNext()) {

					result += it.next()+"$";

				}

				break;

			} else if ((u.getUser().equals(uname)==true) && (u.getConnected() == false)) {

				result = "Connect Before. ERROR.";

			}

		} while(i.hasNext());

		return result;

		 */
	}
	/* (non-Javadoc)
	 * @see android.server.ServerInterface#acceptfriend(java.lang.String, java.lang.String)
	 */
	public  String acceptfriend(String uname,String friend) {

		String ret = "Friend not accepted. ERROR.";
		if (logged && uname.equals(username)){
			User u = um.getUser(uname);
			if (u!= null){
				u.addFriend(friend);
				u.removePenginds(friend);
			}
			u = um.getUser(friend);
			if (u!= null){
				u.addFriend(uname);
				um.commit();
				ret = "Friend added. OK.";
			}
		}
		return ret;
	}
	/*


		Iterator<User> i = users.iterator();
		String result = "Request Denied. ERROR.";

		do {

			User u = (User)i.next();

			if((u.getUser().equals(uname)==true) && (u.getConnected() == true)) {

				u.addFriend(friend);
				u.removePenginds(friend);

				break;

			} else if ((u.getUser().equals(uname)==true) && (u.getConnected() == false)) {

				result = "Connect Before. ERROR.";

			}

		} while(i.hasNext());

		i = users.iterator();
		do {

			User u = (User)i.next();

			if((u.getUser().equals(friend)==true)) {

				u.addFriend(uname);
				u.removePenginds(uname);

				result = "OK.";

				break;

			} 

		} while(i.hasNext());

		//Salvo tutto
		um.savefriends(friend,users);

		return result;

	}
	 */
	/* (non-Javadoc)
	 * @see android.server.ServerInterface#denyfriend(java.lang.String, java.lang.String)
	 */
	public  String denyfriend(String uname,String friend) {


		String ret = "Friend not accepted. ERROR.";
		if (logged && uname.equals(username)){
			User u = um.getUser(uname);
			if (u!= null){
//				u.addFriend(friend);
				u.removePenginds(friend);
				um.commit();
				ret = "Friend removed from pendings. OK.";
			}
//			u = um.getUser(friend);
//			if (u!= null){
//			u.addFriend(uname);
//			}
		}
		return ret;
	}


	/*	Iterator<User> i = users.iterator();
		String result = "Request Denied. ERROR.";

		do {

			User u = (User)i.next();

			if((u.getUser().equals(uname)==true)) {

				u.removePenginds(friend);

				result = "OK.";

				break;

			} 

		} while(i.hasNext());

		um.savefriends(friend,users);

		return result;

	}*/

	/* (non-Javadoc)
	 * @see android.server.ServerInterface#getuserdata(java.lang.String, java.lang.String)
	 */
	public  String getuserdata(String uname, String friend) {

		String ret = "";
		if (logged && uname.equals(username)){
			User u = um.getUser(uname);
			User f = um.getUser(friend);
			if (u!= null && f != null){
				if (u.getFriends().contains(friend) && f.getFriends().contains(uname)){
					ret = f.getUserInfo();
				}
			}
		}
		return ret;
	}
	/*	Iterator<User> i = users.iterator();
		String result = "";
		//TODO: non sarebbe giusto controllare anche se friend ha nella sua lista di amici uname?
		if(getfriends(uname).contains(friend)) {

			do {

				User u = (User)i.next();

				if((u.getUser().equals(friend)==true)) {

					result = u.getUserInfo();

					break;

				} 

			} while(i.hasNext());

		} else {

			result = friend + "is not in your friends list. ERROR."; 

		}

		return result;

	}*/

	/* (non-Javadoc)
	 * @see android.server.ServerInterface#getposition(java.lang.String)
	 */
	public  String getposition(String uname, String friend) {
		String ret = "Impossible return position ERROR.";
		if (logged && uname.equals(username)){
			User u = um.getUser(uname);
			User f = um.getUser(friend);
			if (u!= null && f != null){
				if (u.getFriends().contains(friend) && f.getFriends().contains(uname)){
					ret =f.getGeo();
				}
			}
		}
		return ret;
	}
	/*
		Iterator<User> i = users.iterator();
		String result = "";

		do {

			User u = (User)i.next();

			if((u.getUser().equals(uname)==true)) {

				result = u.getGeo();

				break;

			} 

		} while(i.hasNext());

		return result;

	}
	 */
	/* (non-Javadoc)
	 * @see android.server.ServerInterface#setpreferred(java.lang.String, java.lang.String)
	 */
	public  String setpreferred(String uname, String pref) {
		String ret = "Impossible to set prederred mode. ERROR.";
		if (logged && uname.equals(username)){
			User u = um.getUser(uname);
			if (u!= null){
				u.setPreferredMode(pref);
				um.commit();
				ret = "Preferred mode set to: "+pref+" OK.";
			}
		}
		return ret;
	}
	/*
		Iterator<User> i = users.iterator();
		String result = "ERROR Unexisting User.";

		do {

			User u = (User)i.next();

			if((u.getUser().equals(uname)==true)) {

				result = "OK Mode Changed.";

				u.setPreferredMode(pos);

				break;

			} 

		} while(i.hasNext());

		return result;

	}	*/

	/* (non-Javadoc)
	 * @see android.server.ServerInterface#getpreferred(java.lang.String)
	 */
	public  String getpreferred(String uname, String friend) {
		String ret= "Impossible return preferred mode. ERROR";
		if (logged && uname.equals(username)){
			User u = um.getUser(uname);
			User f = um.getUser(friend);
			if (u!= null && f != null){
				if (u.getFriends().contains(friend) && f.getFriends().contains(uname)){
					ret =f.getPreferredMode();
				}
			}
		}
		return ret;
	}

	public String getPersonal(String uname){
		String ret= "Impossible return personal data. ERROR";
		if (logged && uname.equals(username)){
			User u = um.getUser(uname);
			if (u!= null){
				ret = u.getPersonal();
			}
		}

		return ret;
	}

	public String changePersonal(String uname, String oldPwd, String pwd,String  mobile,String work,String home,String mail,String im){
		String ret= "Impossible to change personal data. ERROR";
		if (logged && uname.equals(username)){
			User u = um.getUser(uname);
			if (u!= null){
				if (u.getPwd().equals(oldPwd)){
					u.setPwd(pwd);
					u.setMobile(mobile);
					u.setWork(work);
					u.setHome(home);
					u.setMail(mail);
					u.setIm(im);
					um.commit();
					ret = "OK";
				}
//				u.
			}
		}

		return ret;
	}


	/*
		Iterator<User> i = users.iterator();
		String result = "ERROR Unexisting User.";

		do {

			User u = (User)i.next();

			if((u.getUser().equals(uname)==true)) {

				result = u.getPreferredMode();

				break;

			} 

		} while(i.hasNext());

		return result;

	}	*/

}	