package datastorage.server;

import java.util.Vector;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

/**
 * Classe principale del servizio server.
 * Questa classe si occupa di istanziare il servant del datastorage replicato
 * 
 * @author Vincenzo Frascino
 * @author Nicolas Tagliani
 *
 */
public class MainClass {


	private static void Start(String orbdHost, String orbdPort, String jndiHost, String jndiPort, String user, String password){
		try {
			String args[] = {"-ORBInitialPort", "", "-ORBInitialHost", ""};
			args[1] = orbdPort;
			args[3] = orbdHost;
			ReplicationServer servant = new ReplicationServer(jndiHost, jndiPort, user, password);
			ORB orb = ORB.init(args, null);
			POA rootPoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
			rootPoa.the_POAManager().activate();
			Object ref = rootPoa.servant_to_reference(servant);
			Object nameServiceRef = orb.resolve_initial_references("NameService");
			NamingContextExt nameService = NamingContextExtHelper.narrow(nameServiceRef);
			NameComponent path[] = nameService.to_name("ReplicationServer");
			nameService.rebind(path,ref);
			System.out.println("Replication server up and running");
			orb.run();
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	private static void printHelp(){
		System.out.println("USAGE:");
		System.out.println("java DataStorageReplicato.Main COMMAND OPTIONS");
		System.out.println("COMMAND can be:");
		System.out.println("help:\tPrints this help message");
		System.out.println("start:\tStarts the replication server\n");
		System.out.println("OPTIONS");
		System.out.println("If you choose the start command you must specify also:");
		System.out.println("-u\tThe server Username in the replication system");
		System.out.println("-p\tThe server Password in the replication system");
		System.out.println("-oh\tThe host where orbd has been started [default is localhost]");
		System.out.println("-op\tThe port where orbd is listening [This MUST be specified]");
		System.out.println("-jh\tThe host where jndi has been started [default is localhost]");
		System.out.println("-jp\tThe port where jndi is listening [default is 16400]");

	}
	public static void main(String[] args) {
		String oh = "localhost";
		String op = null;
		String jh = "localhost";
		String jp = "16400";
		String user = null;
		String password = null;
		if (args.length != 0){
			if (args[0].equalsIgnoreCase("help")) {
				printHelp();
			}
			if (args[0].equalsIgnoreCase("start")){
				Vector<String> v_args = new Vector<String>();
				for (String i : args) v_args.add(i);
				try {
					for (String i : v_args){
						if (i.equals("-oh")){
							oh = args[v_args.indexOf("-oh")+1];
						}
						if (i.equals("-op")){
							op = args[v_args.indexOf("-op")+1];
						}
						if (i.equals("-jp")){
							jp = args[v_args.indexOf("-jp")+1];
						}
						if (i.equals("-jh")){
							jh = args[v_args.indexOf("-jh")+1];
						}
						if (i.equals("-u")){
							user = args[v_args.indexOf("-u")+1];
						}
						if (i.equals("-p")){
							password = args[v_args.indexOf("-p")+1];
						}
					}
					if (op != null && user != null && password != null){
						Start(oh, op, jh, jp, user, password);
					}
					else{
						System.out.println("MISSING VALUES:");
						System.out.println("You must at least specify orbd port and server's username and password\n");
						printHelp();
					}
				}catch (IndexOutOfBoundsException e) {
					printHelp();
				}
			}
			else {
				printHelp();
			}

		}
		else printHelp();


	}

}
