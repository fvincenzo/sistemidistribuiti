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

import java.net.ConnectException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.*;

import javax.jms.JMSException;
import javax.naming.NamingException;


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
             * Installo il servizio rmi
             */
        try {
            ChannelListAdmin server = new ChannelListServer();
            Naming.rebind("ChannelList", server);
            /*
             * Controllo che esita un servizio rmi
             */
            rmiServer = (ChannelListAdmin)Naming.lookup("ChannelList");
            AdminModule.connect(address, joramPort, userName, password,60);

            User.create("anonymous", "anonymous", 0);
        }
        catch (Exception rem){
            return false;
        }
            
            try {
                jndiCtx = new javax.naming.InitialContext();
                jndiCtx.lookup("JavaChat");
            
            connFactory = TopicTcpConnectionFactory.create(address, 16010);

            jndiCtx.bind("JavaChat", connFactory);

            return true;
            } catch (NamingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
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
            System.out.println(e.getLocalizedMessage());
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
                System.out.println(ex.getLocalizedMessage());
                return false;
            }

            rmiServer.removeChannel(chat);
            guiInterface.deleteLine(chat);
            return true;

        }catch (RemoteException e){
            System.out.println(e.getLocalizedMessage());
            return false;
        }
        catch (AdminException e){
            System.out.println(e.getLocalizedMessage());
            return false;
        }
        catch (ConnectException e){
            System.out.println(e.getLocalizedMessage());
            return false;
        }
        catch (JMSException e){
            System.out.println(e.getLocalizedMessage());
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
