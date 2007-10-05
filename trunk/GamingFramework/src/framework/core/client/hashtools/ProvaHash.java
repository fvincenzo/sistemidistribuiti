/*
 * Created on 26-gen-2006
 * 
 */
package framework.core.client.hashtools;

/**
 * @author noname
 * 
 */
public class ProvaHash {

    public static void main(String[] args) {
        try {
	       // Hash h = Hash.getInstance();
	        ShareFile s1 = new ShareFile("/Users/noname/codice",false);
	        ShareFile s2 = new ShareFile("/Users/noname/icap", false);
	        ShareFile s3 = new ShareFile("/Users/noname/eric_australia.jpg", false);
	        ShareFile s4 = new ShareFile("/Users/noname/progettoSI.sql", false);
	        System.out.println(s1.getTTHash());
	      //  h.start();
	    //    h.addFile(s1);
	    //    h.addFile(s2);
	    //    h.addFile(s3);
	    //    h.addFile(s4);
	        while (s1.getTTHash() == null){
	           // System.out.print(".");
	        }
	        System.out.println("S1 TTHASH: "+s1.getTTHash());
	        
	        while (s2.getTTHash() == null){
	  //          System.out.print(".");
	        }
	        System.out.println("S2 TTHASH: "+s2.getTTHash());
	        
	        while (s3.getTTHash() == null){
	//            System.out.print(".");
	        }
	        System.out.println("S3 TTHASH: "+s3.getTTHash());
	        
	        while (s4.getTTHash() == null){
//	            System.out.print(".");
	        }
	        System.out.println("S4 TTHASH: "+s4.getTTHash());
	        Hash.getInstance().kill();
	    }
        catch (Exception e){
            System.out.println("Errore nel file "+e.getMessage());
        }
    }
}
