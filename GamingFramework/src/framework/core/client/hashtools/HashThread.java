/*
 * Created on 23-mar-2006
 * 
 */
package framework.core.client.hashtools;

import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Vector;


/**
 * @author noname
 * 
 */
public class HashThread extends Thread {


    //  private static Hash hashMaker = null;

    private boolean run = true;

    private Vector<ShareFile> coda = new Vector<ShareFile>();
    /*
    private synchronized static void createInstance() {
        if (hashMaker == null) {
            hashMaker = new Hash();
            hashMaker.start();
        }
    }

    public static Hash getInstance(){
        if (hashMaker == null) createInstance();
        		return hashMaker;
    }
     */
    protected HashThread() {}

    public synchronized void addFile(ShareFile s){
	if (s.getTTHash() == null )
	    this.coda.addElement(s);    
    }

    public void run(){
	while (run){
	    this.consume();
	}
    }

    private synchronized void consume(){
	while (coda.size() > 0){
	    try {
		MessageDigest tt = new TigerTree();
		//TODO sistemare il thread per ricevere in ingresso dati di tipo Element e aggiornarli con in tth hash
		FileInputStream fis = new FileInputStream(((ShareFile)coda.firstElement()).getFullPath());
		int read;
		byte[] in = new byte[1024];
		while((read = fis.read(in)) > -1) {
		    tt.update(in,0,read);
		}
		fis.close();
		byte[] digest = tt.digest();
		String hash = new BigInteger(1,digest).toString(16);
		while(hash.length()<48) {
		    hash = "0"+hash;
		}
		coda.firstElement().setDigest(Base32.encode(digest));
		coda.firstElement().notifyMe();
		tt.reset();
		
		//return true;
	    }
	    catch (Exception e){
		e.printStackTrace();
		//return false;
	    }
	    coda.removeElementAt(0);
	}
    }

    public void kill(){
	this.run = false;
    }

}
