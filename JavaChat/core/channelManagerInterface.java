/**
 * JavaChat Project
 */
package core;

import javax.jms.*;

/**
 * @author Vincenzo Frascino
 *
 */
public interface channelManagerInterface extends javax.jms.MessageListener{

	public abstract void sendText(String Text)throws JMSException;
	//public abstract void recvText(Message message);
	
}
