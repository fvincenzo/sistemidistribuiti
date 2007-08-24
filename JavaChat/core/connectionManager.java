/**
 * JavaChat Project
 */
package core;

import javax.jms.*;
import javax.naming.*;
import java.io.*;
import java.io.InputStreamReader;
import java.util.Properties;
import utils.*;

/**
 * @author Vincenzo Frascino
 *
 */

public class connectionManager implements channelManagerInterface,
		connectionManagerInterface {

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
    private static int        connections = 0;//Number of Connections

	/* (non-Javadoc)
	 * @see core.connectionManagerInterface#connect(java.lang.String)
	 */
	private connectionManager(String topicName, String username) throws Exception {
        
        InitialContext jndi = null;
        
        try {
            jndi = getContext.context(); //new InitialContext();
        } catch( Exception e) {
            System.out.println( e.toString() );
            System.exit(2);
        }
        
        // Ricerca di una connection Factory
        conFactory = (ConnectionFactory)jndi.lookup("factoryChat");
        
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
        
    }
    
	/* (non-Javadoc)
	 * @see core.connectionManagerInterface#connect(java.lang.String)
	 */
	public static connectionManager connect(String topicName, String username) throws Exception{
		
		connections++;
		return new connectionManager(topicName,username);
	
	}

	/* (non-Javadoc)
	 * @see core.connectionManagerInterface#getList()
	 */
	public String[] getList() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see core.connectionManagerInterface#disconnect()
	 */
	public void disconnect() throws JMSException{
		
		connection.close( );
	
	}

	/* (non-Javadoc)
	 * @see core.connectionManagerInterface#login(java.lang.String)
	 */
	public channelManagerInterface login(String channel) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public int connectionNumber() {
		
		return this.connections;
		
	}
	
	/* (non-Javadoc)
	 * @see core.channelManagerInterface#sendText(java.lang.String)
	 */
	public void sendText(String Text) throws JMSException{
		
		TextMessage message = pubSession.createTextMessage( );
        message.setText(Text);
        message.setStringProperty("User", userName);
        pub.send(message);
	
	}

	/* (non-Javadoc)
	 * @see core.channelManagerInterface#recvText(java.lang.String)
	 */
	public void onMessage(Message message) {
		
		try {
            TextMessage textMessage = (TextMessage) message;
            String text = textMessage.getText( );
            System.out.println(textMessage.getStringProperty("User") + ": " + text);
        } catch (JMSException e) {
            e.printStackTrace( );
        }
	
	}

}