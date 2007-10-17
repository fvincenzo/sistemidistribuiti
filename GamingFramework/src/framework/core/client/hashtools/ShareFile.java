/*
 * Created on Mar 18, 2005
 *
 */
package framework.core.client.hashtools;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.StringTokenizer;


/**
 * @author noname
 *
 */
public class ShareFile implements ShareFileInterface {

    private String fullPath;
    private boolean tthash;
    private String name;
    private long size;
	//private File shareFile;
	private volatile String digest = null;
	
/**
 * @param path This is the fullpath of the file to share
 * @param tthash This value indicates if the hash must be done or not
 * @throws FileErrorException This exception is thrown if the file is a directory or something different from a regular file
 */
	public ShareFile(String path, boolean tthash) throws Exception {
		this.tthash = tthash;
	    File shareFile = new File(path);
		if (shareFile.isDirectory()) throw new Exception();
		this.fullPath=path;
		this.size = shareFile.length();
		this.name = shareFile.getName();
		if (tthash){
		    /*Yhis means that the hash must be calculated*/
		    Hash hashMaker = Hash.getInstance();
		    hashMaker.addFile(this);
		}
		else {
		    this.digest = null;
		}
		
       }
	public synchronized void notifyMe(){
	    this.notifyAll();
	}
	public long getSize() {
		return size;
	}
	
	protected void setDigest(String digest){
	    this.digest = digest;
	}
	public String getTTHash() {
		return digest;
	}

	public String getName() {
		return name;
	}
	
	protected String getFullPath() {
	    return fullPath;
	}
	
	public String toString() {
		return name;
	}
	
	public synchronized void printOut(String baseDir, PrintStream out){
	    while(digest == null){
		try {
		    this.wait();
		} catch (InterruptedException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	    }
	    out.println(fullPath.substring(baseDir.length()+1));
	    out.println(digest);    
	    
	}
	
	public synchronized void tryPrintOut(String baseDir, PrintStream out) throws IllegalStateException {
	    if (digest == null){
		throw new IllegalStateException("Digest for file "+fullPath+" not yet ready");
	    }
	    out.println(fullPath.substring(baseDir.length()+1));
	    out.println(digest);    
	    
	}
	
	public void match(String text, java.util.Vector result){
	    text = text.toLowerCase();
	    StringTokenizer tokenizer = new StringTokenizer(text,"$");
	    boolean c = true;
		while (tokenizer.hasMoreElements() && c){
		    if ((name.toLowerCase()).indexOf(tokenizer.nextToken()) < 0){ //Controllo per vedere se all'interno del file � contenuta la stringa che si cerca
		        c = false;
		    }
		}
		if (c){
		    result.addElement(name);
		}
	}

}
