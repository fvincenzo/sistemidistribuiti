/**
 * 
 */
package core;

import org.objectweb.joram.client.jms.admin.*;
import org.objectweb.joram.client.jms.*;
import org.objectweb.joram.client.jms.tcp.*;

import serverUtils.ChannelListAdmin;
import serverUtils.ChannelListServer;

import gui.AdminGuiInterface;

import java.io.IOException;
import java.net.ConnectException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;

import javax.jms.JMSException;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.NoInitialContextException;


/**
 * @author   Vincenzo Frascino
 * @author   Nicolas Tagliani
 */
public class JavaChatAdminService {

    private javax.jms.ConnectionFactory connFactory;
    private javax.naming.Context jndiCtx;
//  private int topicValue = 0;
//  private UserManager ute = UserManager.creator();
//  private Hashtable toN = new Hashtable();
//  private Hashtable nTo = new Hashtable();
    private AdminGuiInterface guiInterface;

    private ChannelListAdmin rmiServer;
    /**
     * @uml.property  name="address"
     */
    private String address = "localhost";
    /**
     * @uml.property  name="userName"
     */
    private String userName = "root";
    /**
     * @uml.property  name="password"
     */
    private String password = "root";
    /**
     * @uml.property  name="joramPort"
     */
    private int joramPort = 16010;

    private Registry myRegistry;

    public JavaChatAdminService() {
    }


    public JavaChatAdminService(String address, int joramPort, String userName,String password) {
	this.address = address;
	this.userName = userName;
	this.password = password;
	this.joramPort = joramPort;
    }

    public boolean initServer(){
	/*
	 * Faccio partire rmiregistry
	 */
	/*
	try {
	    myRegistry = LocateRegistry.createRegistry(1099);

	 }
	catch (RemoteException e){
	    try {
		myRegistry = LocateRegistry.getRegistry(1099);
	    }
	    catch (RemoteException re){
		//TODO log info
		System.out.println("Impossible inizializzare il server");
		return false;

	    }
	}
	 */

	/*
	 * Installo il servizio rmi
	 */


	try {
	    try {
		rmiServer = (ChannelListAdmin)Naming.lookup("rmi://localhost/"+"ChannelList");
	    }
	    catch (NotBoundException e){
		return false;
	    }
	    /*
	     * Controllo che esita un servizio rmi
	     */

	    /*
	     * Mi collego al sistema Joram
	     */

	    AdminModule.connect(address, joramPort, userName, password,10);

	    User.create("anonymous", "anonymous", 0);
	    jndiCtx = new javax.naming.InitialContext();

	    /*

	      try {
		 jndiCtx.lookup("JavaChat");
	     }
	     //catch (NoInitialContextException e){*/
	    try {
		connFactory = (ConnectionFactory)jndiCtx.lookup("JavaChat");
	    }
	    catch (Exception e){
//		e.printStackTrace();
		connFactory = TopicTcpConnectionFactory.create(address, 16010);
		jndiCtx.rebind("JavaChat", connFactory);
	    }
//	    }
	    List l = AdminModule.getDestinations();
	    Iterator i = l.iterator();

	    while (i.hasNext()){
		String  s = ((Destination)i.next()).getAdminName();
//		System.out.println(s);
		if ( !s.startsWith("#") ){
		    if (rmiServer.existsName(s)) {
			guiInterface.addLine(s, false);
		    }
		    else {
			guiInterface.addLine(s, true);
		    }
		}

	    }
	    return true;
	} catch (ConnectException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
//	    System.out.println(s);
	    return false;
	} 
	catch (Exception ex){
	    ex.printStackTrace();
	    return false;
	}



    }


    public boolean createChat(String name, boolean priv) {
	try {
	    Destination dest;
	    dest = org.objectweb.joram.client.jms.Topic.create(name);

	    dest.setFreeReading();
	    dest.setFreeWriting();

	    jndiCtx.bind(name , dest);
	    if (!priv) {
		rmiServer.addChannel(name);
	    }
	    guiInterface.addLine(name, priv);
	    return true;
	}catch (Exception e){

	    return false;
	}
    }

    public boolean editChat (String chat, boolean priv){
	try {
	    if (priv){
	    rmiServer.removeChannel(chat);
	}
	else {
	    rmiServer.addChannel(chat);
	}
	guiInterface.editLine(chat, priv);
	return true;
	}
	catch (RemoteException e){
	    return false;
	}
    }
    public boolean deleteChat (String chat){
	try{
	    try {
		Destination dest = (Destination)jndiCtx.lookup(chat);
		dest.delete();
		jndiCtx.unbind(chat);
	    }
	    catch (NamingException ex){
		ex.printStackTrace();
		return false;
	    }

	    rmiServer.removeChannel(chat);
	    guiInterface.deleteLine(chat);
	    return true;

	}catch (RemoteException e){
	    return false;
	}
	catch (AdminException e){
	    return false;
	}
	catch (ConnectException e){
	    return false;
	}
	catch (JMSException e){
	    return false;
	}

    }

    public boolean setPrivate(String chat, boolean privato){
	try {
	    if (privato){
		rmiServer.removeChannel(chat);
	    }
	    else {
		rmiServer.addChannel(chat);
	    }
	    guiInterface.deleteLine(chat);
	    guiInterface.addLine(chat, privato);
	    return true;
	} catch (RemoteException e){
	    System.out.println(e.getLocalizedMessage());
	    return false;
	}

    }

    public boolean disconnect() {

	try {

	    jndiCtx.close();
	    AdminModule.disconnect();
	    return true;

	} catch (Exception e) {
	    System.out.println(e.getLocalizedMessage());
	    return false;
	}

    }



    public void setInterfaccia(AdminGuiInterface gui){
	guiInterface = gui;
    }
    /*
     *********************************************************
     *           getter e setter dei parametri
     ********************************************************* 
     */
    /**
     * @return  the address
     * @uml.property  name="address"
     */
    public String getAddress() {
	return address;
    }


    /**
     * @param address  the address to set
     * @uml.property  name="address"
     */
    public void setAddress(String address) {
	this.address = address;
    }


    /**
     * @return  the joramPort
     * @uml.property  name="joramPort"
     */
    public int getJoramPort() {
	return joramPort;
    }


    /**
     * @param joramPort  the joramPort to set
     * @uml.property  name="joramPort"
     */
    public void setJoramPort(int joramPort) {
	this.joramPort = joramPort;
    }


    /**
     * @return  the password
     * @uml.property  name="password"
     */
    public String getPassword() {
	return password;
    }


    /**
     * @param password  the password to set
     * @uml.property  name="password"
     */
    public void setPassword(String password) {
	this.password = password;
    }


    /**
     * @return  the userName
     * @uml.property  name="userName"
     */
    public String getUserName() {
	return userName;
    }


    /**
     * @param userName  the userName to set
     * @uml.property  name="userName"
     */
    public void setUserName(String userName) {
	this.userName = userName;
    }

}
