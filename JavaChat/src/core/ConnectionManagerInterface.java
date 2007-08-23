/**
 * JavaChat Project
 */
package core;

import javax.jms.*;

/**
 * @author Vincenzo Frascino
 *
 */
public interface ConnectionManagerInterface {
	
	//public connectionManager connect(String topicName, String username);
//	 public abstract String []getList(String server);
	public abstract void disconnect()throws JMSException;
	public abstract ChannelManagerInterface login(String channel);
	public abstract int connectionNumber();
	
}
