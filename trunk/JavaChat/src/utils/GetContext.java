/**
 * JavaChat Project
 */
package utils;

import javax.naming.InitialContext;
import javax.naming.Context;

import org.objectweb.joram.client.jms.admin.AdminModule;

import java.util.Properties;

/**
 * @author Vincenzo Frascino
 *
 */
public class GetContext {

public static InitialContext context() throws Exception {
        
        Properties props    = new Properties();
        Properties sysProps = System.getProperties();
        
        props.setProperty("java.naming.factory.initial", "fr.dyade.aaa.jndi2.client.NamingContextFactory");
        //ho tolto questa riga perchè mi dava un errore tipo protocollo sconosciuto: joram
//        props.setProperty("java.naming.provider.url", "joram://localhost:16400");
        
        sysProps.putAll(props);
        System.setProperties(sysProps);
        
        try {
            return ( new InitialContext() );
        } catch (Exception e) {
            throw new Exception( e.toString() );
        }
        
    }
    
    public static InitialContext context(String host, String port) throws Exception {
        
        Properties props    = new Properties();
        Properties sysProps = System.getProperties();
        
        props.setProperty("java.naming.factory.initial", "fr.dyade.aaa.jndi.NamingContextFactory");
        props.setProperty("java.naming.provider.url", "joram://" + host + ":" + port);
        
        sysProps.putAll(props);
        System.setProperties(sysProps);
        
        try {
            return ( new InitialContext() );
        } catch (Exception e) {
            throw new Exception( e.toString() );
        }
        
    }    
	
}
