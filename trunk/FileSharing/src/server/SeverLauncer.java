/*
 * Created on 24/mag/07
 * 
 */
package server;

import java.io.IOException;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;



import net.jini.core.discovery.LookupLocator;
import net.jini.core.entry.Entry;
import net.jini.core.lease.Lease;
import net.jini.core.lookup.ServiceID;
import net.jini.core.lookup.ServiceItem;
import net.jini.core.lookup.ServiceRegistrar;
import net.jini.discovery.LookupDiscovery;
import net.jini.lease.LeaseRenewalManager;
import net.jini.lookup.JoinManager;
import net.jini.lookup.ServiceIDListener;
import net.jini.lookup.entry.Name;

public class SeverLauncer {

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
        ServiceItem serviceItem;
        ServiceRegistrar registrar;
        
        ServerInterface s1;

        Entry[] attr = new Entry[1];
        attr[0] = (Entry)new Name("p2pServer");
        s1 = new ServerImpl();
        serviceItem = new ServiceItem(null, s1, attr);

        LookupLocator lookup = new LookupLocator ("jini://localhost");
        registrar  = lookup.getRegistrar();
        registrar.register(serviceItem, Lease.FOREVER);

        System.out.println("Serrvice Ready ...");
        }catch (Exception e){
            e.printStackTrace();
        }

      
    }

}
