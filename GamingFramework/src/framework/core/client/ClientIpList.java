package framework.core.client;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Vector;

public class ClientIpList {

	public static Vector ipList() {
		
		Vector<String> v = new Vector<String>();
		try {
			Enumeration<NetworkInterface> e1 = (Enumeration<NetworkInterface>) NetworkInterface.getNetworkInterfaces();
			while(e1.hasMoreElements()) {
				NetworkInterface ni = e1.nextElement();
				String net = new String();
				net = net+ni.getDisplayName()+" : [";
				Enumeration<InetAddress> e2 = ni.getInetAddresses();
				while(e2.hasMoreElements()) {
					InetAddress ia = e2.nextElement();
					net = net+ia;
					if(e2.hasMoreElements()) {
						net += ",";
					}
				}
				net += "]";
				v.add(net);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return v;
		
	}
	
}
