/**
 * JavaChat Project
 */
package core;

import javax.jms.*;

/**
 * @author Vincenzo Frascino
 *
 */
public interface connectionManagerInterface {
	
	//public connectionManager connect(String topicName, String username);
	public abstract String []getList();
	public abstract void disconnect()throws JMSException;
	public abstract channelManagerInterface login(String channel);
	public abstract int connectionNumber();
	
}
