package federazione;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;

import tuplespace.NodoRemotoInterface;

import net.jini.core.discovery.LookupLocator;
import net.jini.core.entry.Entry;
import net.jini.core.entry.UnusableEntryException;
import net.jini.core.lookup.ServiceRegistrar;
import net.jini.core.lookup.ServiceTemplate;
import net.jini.core.transaction.Transaction;
import net.jini.core.transaction.TransactionException;
import net.jini.space.JavaSpace;

public class TakeThread extends Thread {

//	private int source;
	private JavaSpace js;
	private Entry e;
	private Transaction t; 
	private long l;
//	private Response r;
	private boolean run = true;
	private TakeRequest tq;
	private long updateRate = 10000;
	private NodoRemotoInterface nodoSource;

	public TakeThread(TakeRequest tq, NodoRemotoInterface nodoSource, String address, Entry e, Transaction t, long l){
		System.out.println("Istanzio un nuovo TakeThread richiesto su:"+address+"\nEntry: "+e+"\nda: "+nodoSource);
		try {

			LookupLocator lookupL = new LookupLocator(address);
			ServiceRegistrar registrar = lookupL.getRegistrar();
			js = (JavaSpace)registrar.lookup(new ServiceTemplate(null, new Class[]{ JavaSpace.class }, null));
			this.tq=tq;
//			this.r=r;
			this.nodoSource= nodoSource;
//			this.source=source;
			this.e=e;
			this.t=t;
			this.l=l;

		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (RemoteException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}	
	}
	public void run(){
	System.out.println("TakeThread.run()");
		Entry ret = null;
//		Long lcopy = l;
		while (this.run ){
			try {
				if (l > updateRate){
					ret = js.take(e,t,updateRate);
					l = l-updateRate;
				}
				else {
					ret = js.take(e,t,l);
					this.run = false;
				}

			} catch (RemoteException e) {
				tq.throwException(nodoSource, e);
			} catch (UnusableEntryException e) {
				tq.throwException(nodoSource, e);
			} catch (TransactionException e) {
				tq.throwException(nodoSource, e);
			} catch (InterruptedException e) {
				tq.throwException(nodoSource, e);
			}

		}
		tq.gotResult(nodoSource, ret);
	}


	public void halt(){
		run= false;
	}

}
