package datastorage.server;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import datastorage.corba.ServerPOA;
import datastorage.corba.NoValue;
import datastorage.corba.CantUpdate;

/**
 * Classe principale che implementa il servant generato da interface.idl
 * 
 * @author Vincenzo Frascino
 * @author Nicolas Tagliani
 * @uml.stereotype uml_id="Standard::ImplementationClass" 
 */
@SuppressWarnings("serial")
public class ReplicationServer extends ServerPOA implements MessageListener {

	/**
	 * Riferimento all'oggetto che si occupa della gestione della comunicazione nella fase di invio dei messaggi.
	 */
	private ReplicationCommunication replComm;
	/**
	 * Id dell'ultimo messaggio inviato
	 */
	private String lastMsgId = "";
	/**
	 * Oggetto su cui vengono effettuati i lock dei metodi sincronizzati
	 */
	private Object lock = new Object();
	/**
	 * Riferimento del nome utente che usa questo server
	 */
	private String myAccount = "";
	/**
	 * Identificativo dell'ultimo server che si e' unito alla rete.
	 */
	private String lastJoin1 = "";
	/**
	 * Identificativo del penultimo server che si e' unito alla rete.
	 */
	private String lastJoin2 = "";
	/**
	 * Struttura dati per la memorizzazione dei dati locali e remoti
	 */
	private Map<Integer, Integer> localData = new HashMap<Integer, Integer>();

	/**
	 * Costruttore della classe ReplicationServer+
	 * 
	 * @param jndiAddress Indirizzo a cui trovare la connection factory
	 * @param jndiPort Porta a cui fa riferimento la connection factory
	 * @param topicUser Username usato da questo server
	 * @param topicPassword password usata da questo server
	 */
	public ReplicationServer(String jndiAddress, String jndiPort, String topicUser, String topicPassword) {
		this.myAccount = topicUser;
		try {
			replComm = new ReplicationCommunication(this, jndiAddress, jndiPort, topicUser, topicPassword);
			synchronized (lock) {
				lastMsgId = replComm.sendMessage("JOIN: "+topicUser);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Operazione di lettura esportata tramite corba
	 * 
	 * @param dataId l'id del dato da leggere
	 * @return il dato letto
	 * @throws NoValue Lancia l'eccezione NoValue se il dato non e' presente nel sistema
	 */
	public int read(int dataId) throws NoValue {
		synchronized (lock) {
			if (!localData.containsKey(dataId)) throw new NoValue();
			return localData.get(dataId);
		}
	}
	
	/**
	 * Operazione di scrittura esportata tramite corba
	 * 
	 * @param dataId Id del dato da scrivere
	 * @param newValue Nuovo valore del dato da scrivere
	 * @throws CantUpdate Eccezione lanciata se avviene qualche errore sulla rete e il dato non viene propagato
	 */
	public void write(int dataId, int newValue) throws CantUpdate {
		synchronized (lock) {

			try {
				lastMsgId = replComm.sendMessage("ID: "+dataId+" "+newValue);
			} catch (JMSException e) {
				e.printStackTrace();
				throw new CantUpdate();
			}
			localData.put(dataId, newValue);
		}
	}

/*	public void quit(){
		synchronized (lock) {
			try {
				lastMsgId = replComm.sendMessage("LEAVE: "+myAccount);
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}
	*/

	/**
	 * Metodo ereditato dall'interfaccia MessageListener di joram.
	 * Implementa il protocollo di comunicazione in fase di ricezione dei messaggi.
	 * 
	 * @param message il message ricevuto
	 */
	public void onMessage(Message message)  {
		synchronized (lock) {
			try {
				if (!message.getJMSMessageID().equals(lastMsgId)){
					System.out.println("Ricevo: "+((TextMessage)message).getText());
					StringTokenizer tok = new StringTokenizer(((TextMessage)message).getText());
					String command = tok.nextToken();
					if (command.equals("ID:")){
						Integer id = Integer.parseInt(tok.nextToken());
						Integer newValue = Integer.parseInt(tok.nextToken());
						localData.put(id, newValue);
					}
					if (command.equals("JOIN:")){
						if (lastJoin1.equals("")){
							lastJoin1 = tok.nextToken();
							String out = "";
							for (Integer i : localData.keySet()) out=out+i+" "+localData.get(i)+" ";
							lastMsgId = replComm.sendPrivMessage(lastJoin1, out );
						}
						else 
							if (lastJoin2.equals("")){
								lastJoin2 = tok.nextToken();
								String out = "";
								for (Integer i : localData.keySet()) out=out+i+" "+localData.get(i)+" ";
								lastMsgId = replComm.sendPrivMessage(lastJoin1, out );								
							}
					}
					if (command.equals("TOSERVER:") && tok.nextToken().equals(myAccount) ){
						/*
						 * Ho ricevuto un aggiornamento sui dati presenti
						 */
						while ( tok.countTokens() >= 2 ){
							Integer id = Integer.parseInt(tok.nextToken());
							Integer newValue = Integer.parseInt(tok.nextToken());
							localData.put(id, newValue);
						}
					}
						
				/*	if (command.equals("LEAVE: ")){
						String userLeaving = tok.nextToken();
						if (userLeaving.equals(lastJoin1)) {
							lastJoin1 = lastJoin2;
							lastJoin2 = "";	
						}
						else if (userLeaving.equals(lastJoin2)) lastJoin2 = "";
					}*/
					}
				else {
//					System.out.println("Ho ricevuto questo messaggio che ho inviato io:\n"+((TextMessage)message).getText());
				}
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}

}
