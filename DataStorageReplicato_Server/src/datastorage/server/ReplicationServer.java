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
 * @author Nicolas Tagliani
 * @uml.stereotype uml_id="Standard::ImplementationClass" 
 */
@SuppressWarnings("serial")
public class ReplicationServer extends ServerPOA implements MessageListener {
//	private static boolean debug = true;
	private ReplicationCommunication replComm;
	private String lastMsgId = "";
	private Object lock = new Object();
	private String myAccount = "";
	private String lastJoin1 = "";
	private String lastJoin2 = "";

	/** 
	 * @uml.property name="localData"
	 */
	private Map<Integer, Integer> localData = new HashMap<Integer, Integer>();



	/** 
	 * Costruttore per la classe ReplicationServer
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


	public int read(int dataId) throws NoValue {
		synchronized (lock) {
			if (!localData.containsKey(dataId)) throw new NoValue();
			return localData.get(dataId);
		}
	}

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
//							System.out.println("Devo inviare le informazioni a "+lastJoin1+" prima volta");
						}
						else 
							if (lastJoin2.equals("")){
								lastJoin2 = tok.nextToken();
								String out = "";
								for (Integer i : localData.keySet()) out=out+i+" "+localData.get(i)+" ";
								lastMsgId = replComm.sendPrivMessage(lastJoin1, out );
//								System.out.println("Devo inviare le informazioni a "+lastJoin2+" seconda volta");
							}
					}
					if (command.equals("TOSERVER:") && tok.nextToken().equals(myAccount) ){
						/**
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
					System.out.println("Ho ricevuto questo messaggio che ho inviato io:\n"+((TextMessage)message).getText());
				}
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}

}
