package datastorage.server;

import java.util.Hashtable;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.InitialContext;

/**
 * Questa classe si occupa dell'invio di messaggi tra server replicati astraendo la presenza di uno o piu' server jms.
 * 
 * @author Vincenzo Frascino
 * @author Nicolas Tagliani
 *
 */
public class ReplicationCommunication {
	
	/**
	 * Connection Factory di jms
	 */
	private ConnectionFactory conFactory;
	/**
	 * Message producer di jms
	 */
	private MessageProducer   pub;
	/**
	 * Sessione del message producer
	 */
	private Session           pubSession;
	/**
	 * Message subscriber di jms
	 */
	private MessageConsumer   sub;
	/**
	 * Sessione del message subscriber
	 */
	private Session           subSession;
	/**
	 * Connessione al servizio jms
	 */
	private Connection        connection;

	/**
	 * Riferimento all'oggetto replicationServer per inoltrare le risposte tramite il metodo onMessage
	 */
	private MessageListener replServer;

	/**
	 * Costruttore della classe ReplicationCommunication. 
	 * Inizializza la connessione a un topic su un server jms.
	 * 
	 * @param replServer Il MessageListener di jms
	 * @param address Indirizzo su cui risiede la connection factory per il server jms
	 * @param port Porta della connection factory
	 * @param user Username del server che si deve inserire nella rete 
	 * @param password Password del server che si deve inserire nella rete 
	 * @throws Exception
	 */
	public ReplicationCommunication(MessageListener replServer, String address, String port, String user, String password) throws Exception{
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

	}

	/**
	 * Invia un messaggio che ha come destinatario un server singolo
	 * 
	 * @param destinationUser Il nome del server che deve ricevere il messaggio
	 * @param text Il testo del messaggio da inviare
	 * @return l'id del messaggio inviato per controllare i messaggi in fase di ricezione
	 * @throws JMSException
	 */
	public String sendPrivMessage(String destinationUser, String text) throws JMSException{
		TextMessage message = pubSession.createTextMessage();
		message.setText("TOSERVER: "+destinationUser+" "+text);
		pub.send(message);
		return message.getJMSMessageID();
	}
	
	/**
	 * Invia un messaggio sulla rete dei server
	 * 
	 * @param text Il testo del messaggio da inviare 
	 * @return l'id del messaggio inviato per controllare i messaggi in fase di ricezione
	 * @throws JMSException
	 */
	public String sendMessage(String text) throws JMSException{
		TextMessage message = pubSession.createTextMessage( );
		message.setText(text);
		pub.send(message);
		return message.getJMSMessageID();
	}

}
