/*
 * Created on 25-dic-2005
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
public class Hash {
    /**
     * Singleton interface for the HashThread Class
     * 
     * The perfect solution!!! ;-)
     * 
     */
    
    private static HashThread hashMaker = null;
    
    private static Hash instance = null;
    
    private boolean run = true;
    
    private Vector coda = new Vector();
    
    private synchronized static void createInstance() {
        if (instance == null) {
            instance = new Hash();
            
        }
    }
   
    public static Hash getInstance(){
        if (instance == null) createInstance();
        		return instance;
    }
    
    private Hash() {
        hashMaker = new HashThread();
        hashMaker.start();
    }
    
    public void addFile(ShareFile s){
        hashMaker.addFile(s);   
    }
    
    
  /*  private void consume(){
        while (coda.size() > 0){
            try {
        		MessageDigest tt = new TigerTree();
        		//TODO sistemare il thread per ricevere in ingresso dati di tipo Element e aggiornarli con in tth hash
                FileInputStream fis = new FileInputStream(((String)coda.firstElement()));
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
                    ((ShareFile)coda.firstElement()).setDigest(Base32.encode(digest));
                    notifyAll();
                    tt.reset();
                    //TODO Debug message
                    System.out.println("Hash calcolato e notificato");
                    //return true;
        		}
        		catch (Exception e){
        		  //return false;
        		}
        		coda.removeElementAt(0);
        }
    }*/
    
    public void kill(){
        hashMaker.kill();
    }
}
