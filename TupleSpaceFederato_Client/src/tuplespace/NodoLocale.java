package tuplespace;

import tuplespace.NodoRemotoInterface;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
//import java.rmi.MarshalledObject;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import federationservice.FederationServiceInterface;
import federazione.FederazioneInterface;
import net.jini.core.discovery.LookupLocator;
import net.jini.core.entry.Entry;
import net.jini.core.entry.UnusableEntryException;
//import net.jini.core.event.EventRegistration;
//import net.jini.core.event.RemoteEventListener;
import net.jini.core.lease.Lease;
import net.jini.core.lookup.ServiceRegistrar;
import net.jini.core.lookup.ServiceTemplate;
import net.jini.core.transaction.Transaction;
import net.jini.core.transaction.TransactionException;
//import net.jini.lease.LeaseListener;
//import net.jini.lease.LeaseRenewalEvent;
//import net.jini.lease.LeaseRenewalManager;
import net.jini.space.JavaSpace;

@SuppressWarnings("serial")
public class NodoLocale implements NodoLocaleInterface , NodoRemotoInterface, Serializable {

//	private String nomeFederazione;
	private String federationServiceAddress = "localhost";
	private JavaSpace js;
	private FederazioneInterface f = null;
	private FederationServiceInterface remFedService = null;
//	private LeaseRenewalManager leaseRenewal;
	private Entry result = null;
	private Exception resultException = null;
	@SuppressWarnings("unused")
	private boolean r_present = false;
	private int result_status = 0;
//	private NodoLocaleEventListener listener = null;
	private String jSpaceRemoteAddress;
	private boolean debug = false;

	public NodoLocale(String externalAddress) throws IOException, ClassNotFoundException, RemoteException{
		
		LookupLocator l = new LookupLocator("jini://localhost");
		ServiceRegistrar r = l.getRegistrar();
		js = (JavaSpace)r.lookup(new ServiceTemplate(null, new Class[]{ JavaSpace.class }, null));
		jSpaceRemoteAddress = externalAddress;

	}


	public boolean cercaFederazione(String nome) {
		try{
			if (remFedService == null){
				remFedService = (FederationServiceInterface)Naming.lookup("rmi://"+federationServiceAddress+"/FederationService");
			}

			return remFedService.cercaFederazione(nome);
		} catch (RemoteException e){
			return false;
		} catch (NotBoundException e){
			return false;
		}
		catch (MalformedURLException e){
			return false;
		}
	}

	public boolean creaFederazione(String nome) {
		try{
			if (remFedService == null){
				remFedService = (FederationServiceInterface)Naming.lookup("rmi://"+federationServiceAddress+"/FederationService");
			}

			remFedService.creaFederazione(nome);
			return true;
		} catch (RemoteException e){
			return false;
		}catch (NotBoundException e){
			return false;
		}
		catch (MalformedURLException e){
			return false;
		}
	}

	public boolean join(String nome) {
		try{
			if (remFedService == null){
				remFedService = (FederationServiceInterface)Naming.lookup("rmi://"+federationServiceAddress+"/FederationService");
			}
			UnicastRemoteObject.exportObject(this,0);
			if (debug) System.out.println("hash dell'oggetto locale: "+this.hashCode());
			f = remFedService.join((NodoRemotoInterface)this, nome);
			return true;
		} catch (RemoteException e){
			return false;
		}catch (NotBoundException e){
			return false;
		}
		catch (MalformedURLException e){
			return false;
		}
	}

	public boolean leave() {
		if (f != null){
			try{
				if (f.leave(this)){
					f = null;
					return true;
				}
			}catch(RemoteException e){
				return false;
			}
		}
		return false;
	}

	public Entry read(Entry e, Transaction t, long l) throws Exception {
		if (f!=null){
			Entry ret = js.readIfExists(e,t,l);
			if (ret != null){
				return ret;
			}
			else{
				f.read(this, e, t, l);
				return getResult();
			}
		}
		return js.read(e,t,l);
	}

	public Entry readIfExists(Entry e, Transaction t, long l) throws Exception {
		if (f!=null){
			return f.readIfExists(this, e, t, l);
		}
		return js.readIfExists(e,t,l);
	}

	public Entry take(Entry e, Transaction t, long l) throws Exception {
		
		if (f!=null){
//			Entry ret = null;
			Entry ret = js.takeIfExists(e,t,l);

			if (ret != null){
				return ret;
			}
			else{
				if (debug) System.out.println("Eseguo la take sulla federazione");
				if (debug) System.out.println("Take da parte di :"+this.hashCode());
				f.take(this, e, t, l);
				if (debug) System.out.println("Fatto");
				return getResult();
			}
		}

		return js.take(e,t,l);
	}

	public Entry takeIfExists(Entry e, Transaction t, long l) throws Exception {
		if (f!=null){
			return f.takeIfExists(this, e, t, l);
		}
		return js.takeIfExists(e,t,l);
	}

	public Lease write(Entry e, Transaction t, long l) throws Exception {
		return js.write(e,t,l);

	}


	public Entry remoteReadIfExists(Entry e, Transaction t, long l)
	throws RemoteException {
		try {
			return js.readIfExists(e,t,l);
		} catch (UnusableEntryException e1) {
			throw new RemoteException(e1.getMessage());
		} catch (TransactionException e1) {
			throw new RemoteException(e1.getMessage());
		} catch (InterruptedException e1) {
			throw new RemoteException(e1.getMessage());
		}
	}



	public Entry remoteTakeIfExists(Entry e, Transaction t, long l)
	throws RemoteException {
		try {
			return js.takeIfExists(e,t,l);
		} catch (UnusableEntryException e1) {
			throw new RemoteException(e1.getMessage());
		} catch (TransactionException e1) {
			throw new RemoteException(e1.getMessage());
		} catch (InterruptedException e1) {
			throw new RemoteException(e1.getMessage());
		}
	}
//	public void register(Entry e, Transaction t, RemoteEventListener r, long l) throws RemoteException{
//		System.out.println("NodoLocale.register()");
//		try {
//			System.out.println("NodoLocale.register()");
//			leaseRenewal = new LeaseRenewalManager();
//			/**
//			 * inserire l'oggetto di hangback per identificare chi ha fatto partire
//			 * il notify
//			 */
//			EventRegistration myReg;
//			try {
//				myReg = js.notify(e, null, r, 30000,new MarshalledObject(new Integer(432)));
//				leaseRenewal.renewFor(myReg.getLease(), Lease.FOREVER, 30000, new DebugListener());
//			} catch (IOException e1) {
//				throw new RemoteException("Impossibile registrare l'oggetto di ritorno");
//			}
//		}
//
//
//		catch (TransactionException e1) {
//			throw new RemoteException(e1.getMessage());
//		}
//
//	}

	/**
	 * @return the federationService
	 */
	public String getFederationServiceAddress() {
		return federationServiceAddress;
	}

	private Entry getResult() throws Exception {
		if (debug) System.out.println("NodoLocale.getResult()");
	/*
	 	while (!r_present){
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		r_present = false;
		return result;
*/
		
		while (result_status == 0){
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (result_status == 1){
			result_status = 0;
			return result;
		}
		else {
			result_status = 0;
			throw new Exception(resultException);
		}
		

		
	}

	public void putResult(Entry e) throws RemoteException{
		if (debug) System.out.println("NodoLocale.putResult()");
		result = e;
//		notify();
//		r_present = true;
		result_status = 1;
	}

	
	public void throwException(Exception e) throws RemoteException {
		this.resultException = e;
		result_status  = -1;
		
	}

	/**
	 * @param federationService the federationService to set
	 */
	public void setFederationServiceAddress(String federationServiceAddress) {
		this.federationServiceAddress = federationServiceAddress;
	}

	public String getJavaSpaceAddress() throws RemoteException {
		return jSpaceRemoteAddress;
	}



//	private static class DebugListener implements LeaseListener {
//		public void notify(LeaseRenewalEvent anEvent) {
//			System.out.println("Got lease renewal problem");
//
//			System.out.println(anEvent.getException());
//			System.out.println(anEvent.getExpiration());
//			System.out.println(anEvent.getLease());
//		}
//	}
//
//	/**
//	 * @return the listener
//	 */
//	public NodoLocaleEventListener getListener() {
//		return listener;
//	}
//
//
//	/**
//	 * @param listener the listener to set
//	 */
//	public void setListener(NodoLocaleEventListener listener) {
//		this.listener = listener;
//	}
//
//


}
