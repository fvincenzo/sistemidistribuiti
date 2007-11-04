/**
 * JavaChat Project
 */
package core;

import javax.jms.*;

/**
 * @author Vincenzo Frascino
 *
 */
public interface ChannelInterface extends javax.jms.MessageListener{

	public abstract void disconnect()throws JMSException;
	
//	public abstract ChannelInterface login(String channel);
//	public abstract int connectionNumber();
	
	public abstract void sendText(String Text)throws JMSException;
	//public abstract void recvText(Message message);
	
}
