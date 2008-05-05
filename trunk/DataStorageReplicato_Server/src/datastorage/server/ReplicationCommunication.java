/**
 * This class performa all the replication and syncronization issues
 */
package datastorage.server;

import java.util.Hashtable;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.InitialContext;

/**
 * 
 * @author Nicolas Tagliani
 *
 */
public class ReplicationCommunication {
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

	private ReplicationServer replServer;

	public ReplicationCommunication(ReplicationServer replServer, String address, String port, String user, String password) throws Exception{
		this.replServer = replServer;
		Hashtable<String, Object> environment = new Hashtable<String, Object>();
		environment.put("java.naming.factory.initial", "fr.dyade.aaa.jndi2.client.NamingContextFactory");
		environment.put("java.naming.factory.host", address);
		environment.put("java.naming.factory.port", port);
		InitialContext jndi = new InitialContext(environment);
		// Ricerca di una connection Factory
		conFactory = (ConnectionFactory)jndi.lookup("ReplicationNetwork");

		// Creazione di una connessione alla Factory
		connection = conFactory.createConnection(user, password);

		// Creazione di 2 connectionObject
		pubSession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		subSession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

		// Look up di un topic
		Topic topic = (Topic)jndi.lookup("Replication");

		// Creazione del publisher e del subscriber
		pub = pubSession.createProducer(topic);
		sub = subSession.createConsumer(topic);

		// Attacco un message listener
		sub.setMessageListener(this.replServer);

		// Inizializzazione della connessione
		connection.start( );

		// Invio un messaggio predefinito per segnalare la mia presenza nella stanza agli altri utenti
//		vectorClock.put(username, 0);

	}

	public String sendPrivMessage(String destinationUser, String text) throws JMSException{
		TextMessage message = pubSession.createTextMessage();
		message.setText("TOSERVER: "+destinationUser+" "+text);
		pub.send(message);
		return message.getJMSMessageID();

	}
	public String sendMessage(String text) throws JMSException{
		TextMessage message = pubSession.createTextMessage( );
		message.setText(text);
		pub.send(message);
		return message.getJMSMessageID();
	}

}
