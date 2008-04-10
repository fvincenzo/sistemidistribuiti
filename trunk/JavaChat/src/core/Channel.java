/**
 * JavaChat Project
 */
package core;

import gui.ChatApplicationNotifier;

import javax.jms.*;
import javax.naming.*;
//import java.io.*;
import java.rmi.Naming;
import java.util.HashSet;
import serverUtils.ChannelList;
import utils.*;

/**
 * @author   Vincenzo Frascino
 * @author   Nicolas Tagliani
 */

public class Channel implements ChannelInterface {

    //Connection Factory
    private ConnectionFactory conFactory;
    //Publisher and his session
    private MessageProducer   pub;
    private Session           pubSession;
    //Subscriber and his session
    private MessageConsumer   sub;
    private Session           subSession;
    //Connection
    private Connection        connection;
    //Other Data
    private String            userName;
    //Number of Connections
    private static int        connections = 0;
    //List of all the users connected to this channel
    private HashSet<String>   userList = new HashSet<String>();
    //Flag to control login phase
    private boolean login = true;
    //Flag to control the change nick phase
    private boolean change = false;
    //Flag to control the change nick phase
//    private boolean hello = false;
    
    private boolean changeUsername = false;
    private int name = 0;
    //Vector Clock
//  private Map<String, Integer> vectorClock = new Hashtable<String, Integer>();

    private ChatApplicationNotifier notifier = null;
    /* (non-Javadoc)
     * @see core.connectionManagerInterface#connect(java.lang.String)
     */
    private Channel(String topicName, String username) throws Exception {

	InitialContext jndi = null;

	try {
	    jndi = GetContext.context(); //new InitialContext();
	} catch( Exception e) {
	    System.out.println( e.toString() );
	    System.exit(2);
	}

	// Ricerca di una connection Factory
	conFactory = (ConnectionFactory)jndi.lookup("JavaChat");

	// Creazione di una connessione alla Factory
	connection = conFactory.createConnection();

	// Creazione di 2 connectionObject
	pubSession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	subSession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

	// Look up di un topic
	Topic chatTopic = (Topic)jndi.lookup(topicName);

	// Creazione del publisher e del subscriber
	pub = pubSession.createProducer(chatTopic);
	sub = subSession.createConsumer(chatTopic);

	// Attacco un message listener
	sub.setMessageListener(this);

	// Inizializzazione della connessione
	userName = username;
	changeUsername = false;
	connection.start( );

	// Invio un messaggio predefinito per segnalare la mia presenza nella stanza agli altri utenti
	TextMessage message = pubSession.createTextMessage( );
	message.setText("/login "+userName);
	message.setStringProperty("User", userName);
	this.login = true;

	pub.send(message);
//	vectorClock.put(username, 0);

    }
    public  void setTextReceiver(ChatApplicationNotifier recv){
	this.notifier = recv;
    }
    /* (non-Javadoc)
     * @see core.connectionManagerInterface#connect(java.lang.String)
     */
    public static Channel connect(String topicName, String username) throws Exception{

	connections++;  //non capisco a cosa serve questo contatore
	return new Channel(topicName,username);

    }

    /* (non-Javadoc)
     * @see core.connectionManagerInterface#getList()
     */

    public static String[] getList(String server) {
	try {
	    ChannelList c = (ChannelList)Naming.lookup("//"+server+"/ChannelList");

	    return c.getAllChannels();

	}catch (Exception e){
	    e.printStackTrace();
	    return null;
	}
	// TODO Implementare la lista delle chat

    }

    public int getStatus() {
	if (changeUsername) return -1;
	return 0;
    }
    
    public String[] getUsersList(){
	String[] a ={};
	return this.userList.toArray(a);        
    }

    /* (non-Javadoc)
     * @see core.connectionManagerInterface#disconnect()
     */
    public void disconnect() throws JMSException{
	TextMessage message = pubSession.createTextMessage( );
	message.setText("/bye "+userName);
	message.setStringProperty("User", userName);
	pub.send(message);
	connection.close( );
	pubSession.close();
	subSession.close();
    }

    /* (non-Javadoc)
     * @see core.connectionManagerInterface#login(java.lang.String)
     */
    /*
	public ChannelInterface login(String channel) {
		// TODO decidere se serve...
		return null;
	}

	public int connectionNumber() {

		return this.connections;

	}
     */
    /* (non-Javadoc)
     * @see core.channelManagerInterface#sendText(java.lang.String)
     */
    public synchronized void sendText(String Text) throws JMSException{

	if (!Text.trim().equals("")){
//	    ObjectMessage message1 = pubSession.createObjectMessage();
	    TextMessage message = pubSession.createTextMessage( );
//	    message1.setStringProperty("Text", Text);
//	    message1.setStringProperty("User", userName);
	    message.setText(Text.replaceAll("/", "//"));
	    message.setStringProperty("User", userName);
	    /*  
        vectorClock.put(userName, vectorClock.get(userName)+1);
        message1.setObjectProperty("VectorClock", vectorClock);

        String users = "";
        String clocks = "";
        int i = 0;
        for (String s : vectorClock.keySet()){
        	users += (s+";");
        	clocks += vectorClock.get(s)+";";
            i++;
        }
        message.setStringProperty("vecUsers", users);
        message.setStringProperty("vecValues", clocks);
        pub.send(message1);
	     */   
	    pub.send(message);
	}
    }

    /* (non-Javadoc)
     * @see core.channelManagerInterface#recvText(java.lang.String)
     */
    public synchronized void onMessage(Message message) {
	//TODO Ricevere l'objectmessage e sistemare
	try {
//	    ObjectMessage objectMessage = (ObjectMessage) message;

	    TextMessage textMessage = (TextMessage) message;
	    String text = textMessage.getText( );
//	    String text1 = objectMessage.getStringProperty("Text");

	    if (text.startsWith("/login") ){
		//salto fino allo prima lettera del nome
		String newUser = text.substring(7);

		if (newUser.equals(userName) ){  //O sono io o √® qualcuno con il mio nick
		    if (login) {
			/*
			 * login true se il messaggio l'ho inviato io 
			 * mi inserisco nella lista degli utenti
			 */
			userList.add(userName);
			notifier.userJoin(userName);
			login = false;
		    }
		    else {
			/*
			 * non ho inviato io il messaggio di login
			 * un'altro utente cerca di entrare col mio nick gli notifico che c'ero prima io
			 */
			change = true;
			TextMessage m = pubSession.createTextMessage( );
			m.setText("/change "+userName);
			m.setStringProperty("User", userName);
			pub.send(m);
		    }
		}
		else {
		    /*
		     * L'utente per me potrebbe stare nella chat perchè non ha il mio nick
		     * gli rispondo con un hello
		     */
//		    hello = true;
		    TextMessage m = pubSession.createTextMessage( );
			m.setText("/hello "+userName);
			m.setStringProperty("User", userName);
			pub.send(m);
		   
		    userList.add(newUser);
		    notifier.userJoin(newUser); 
		}

	    } else if (text.startsWith("/bye")) {
		String newUser = text.substring(5);
		userList.remove(newUser);
		if (notifier!=null) notifier.userPart(newUser);

	    } else if(text.startsWith("/hello")) {

		String newUser = text.substring(7);
		userList.add(newUser);
		if (notifier!=null) notifier.userJoin(newUser);
	    }

	    else if (text.startsWith("/change") && (text.substring(8)).equals(userName) ) {
		if (change) //L'ho mandato io questo messaggio
		    change = false;
		else { //non l'ho mandato io
//		    System.out.println("Username gia' in uso!!");
		    userList.remove(userName);
		    this.login = true;
		    name++;
		    this.userName = userName + name;
		    TextMessage m = pubSession.createTextMessage( );
			m.setText("/login "+userName);
			m.setStringProperty("User", userName);
			pub.send(m);
		   
		    
		    
		}
	    } 
	    /* else {
                vectorClock.clear();
//                String users = textMessage.getStringProperty("vecUsers");
//                String clocks = textMessage.getStringProperty("vecValues");
                StringTokenizer strokUsers = new StringTokenizer(textMessage.getStringProperty("vecUsers"),";");
                StringTokenizer strokValues = new StringTokenizer(textMessage.getStringProperty("vecValues"),";");
                //TODO sistemare la ricezione del vectorClock senno' strokUsers.hasMoreTokens()
               while(strokUsers.hasMoreElements()){
                   vectorClock.put(strokUsers.nextToken(), Integer.parseInt(strokValues.nextToken()) ); 
               }

              if (notifier == null) System.out.println(textMessage.getStringProperty("User") + ": " + text);
	     */
	    else{ 
		if (text.startsWith("//"))
		    notifier.textReceived(textMessage.getStringProperty("User"), text.replaceAll("//", "/"));
		else
		    if (!text.startsWith("/")){
			notifier.textReceived(textMessage.getStringProperty("User"), text.replaceAll("//", "/"));
			
		    }
	    }

	} catch (JMSException e) {
	    e.printStackTrace( );
	}

    }
    public void clear(){
	
    }

}
