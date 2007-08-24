/*
 * Created on 24/mag/07
 * 
 */
package client;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;

import net.jini.core.discovery.LookupLocator;
import net.jini.core.lookup.ServiceRegistrar;
import net.jini.core.lookup.ServiceTemplate;

import server.ServerInterface;

public class Launcher {

    /**
     * @param args
     */
    public static void main(String[] args) {

      try {
        LookupLocator l = new LookupLocator("jini://localhost");
          ServiceRegistrar r = l.getRegistrar();

          ServerInterface f = (ServerInterface) r.lookup(new ServiceTemplate(
                              null,new Class[] {ServerInterface.class},null));
          System.out.println(f.prova());
    } catch (MalformedURLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (RemoteException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (ClassNotFoundException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    }

}
