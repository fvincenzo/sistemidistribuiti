/**
 * JavaChat Project
 */
package src.core;

import src.gui.MainGui;
import src.gui.ChatApplicationNotifier;

import javax.jms.*;
import javax.naming.*;
//import java.io.*;
import java.rmi.Naming;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.StringTokenizer;
//import java.util.Properties;

import src.serverUtils.ChannelList;
import src.utils.*;

/**
 * @author   Vincenzo Frascino
 */

public class ConnectionManager implements ChannelManagerInterface,
		ConnectionManagerInterface {

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
    //Vector Clock
    private Map<String, Integer> vectorClock = new Hashtable<String, Integer>();
    
    private ChatApplicationNotifier notifier = null;
	/* (non-Javadoc)
	 * @see core.connectionManagerInterface#connect(java.lang.String)
	 */
	private ConnectionManager(String topicName, String username) throws Exception {
        
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
        connection.start( );
        
        // Invio un messaggio predefinito per segnalare la mia presenza nella stanza agli altri utenti
        TextMessage message = pubSession.createTextMessage( );
        message.setText("/login "+userName);
        message.setStringProperty("User", userName);
        this.login = true;
        userList.add(userName);
        pub.send(message);
        vectorClock.put(username, 0);
        
    }
    public  void setTextReceiver(ChatApplicationNotifier recv){
        this.notifier = recv;
    }
	/* (non-Javadoc)
	 * @see core.connectionManagerInterface#connect(java.lang.String)
	 */
	public static ConnectionManager connect(String topicName, String username) throws Exception{
		
		connections++;  //non capisco a cosa serve questo contatore
		return new ConnectionManager(topicName,username);
	
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
	
	}

	/* (non-Javadoc)
	 * @see core.connectionManagerInterface#login(java.lang.String)
	 */
	public ChannelManagerInterface login(String channel) {
		// TODO decidere se serve...
		return null;
	}
	
	public int connectionNumber() {
		
		return this.connections;
		
	}
	
	/* (non-Javadoc)
	 * @see core.channelManagerInterface#sendText(java.lang.String)
	 */
	public synchronized void sendText(String Text) throws JMSException{
		//TODO Sistemare l'object message
        ObjectMessage message1 = pubSession.createObjectMessage();
		TextMessage message = pubSession.createTextMessage( );
        message1.setStringProperty("Text", Text);
        message1.setStringProperty("User", userName);
        message.setText(Text);
        message.setStringProperty("User", userName);
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
        pub.send(message);
	
	}

	/* (non-Javadoc)
	 * @see core.channelManagerInterface#recvText(java.lang.String)
	 */
	public synchronized void onMessage(Message message) {
		//TODO Ricevere l'objectmessage e sistemare
		try {
            ObjectMessage objectMessage = (ObjectMessage) message;
            
            TextMessage textMessage = (TextMessage) message;
            String text = textMessage.getText( );
            String text1 = objectMessage.getStringProperty("Text");
            
            if (text.startsWith("/login") ){
                //salto fino allo prima lettera del nome
                String newUser = text.substring(7);
                                
                if (newUser.equals(userName) ){  //O sono io o è qualcuno con il mio nick
                    if (login) {
                        notifier.userJoin(userName);
                        login = false;
                    }
                    else {
                        change = true;
                        sendText("/change "+newUser);
                    }
                }
                else {
                    sendText("/hello "+userName);
                    

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
                    //TODO aggiungere la segnalazione
                    System.out.println("Username gia' in uso!!");
                    disconnect();
                }
            } else {
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
               else notifier.textReceived(textMessage.getStringProperty("User") + ": " + text);
            }
        } catch (JMSException e) {
            e.printStackTrace( );
        }
	
	}

}