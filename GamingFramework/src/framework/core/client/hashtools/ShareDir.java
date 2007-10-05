/*
 * Created on Mar 18, 2005
 *
 */
package framework.core.client.hashtools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.StringTokenizer;
import java.util.Vector;


/**
 * @author noname
 *
 */
public class ShareDir{
	
    private String FullPath;
    private String name;
	private boolean recursive;
	
	/** Refresh = true -> we don't have to calculate the hash of the file*/
	private boolean tthash;
	private Vector<ShareDir> subDir;
	private Vector<ShareFile> subFile;
	private long size;
	
	/*public ShareDir(boolean rec, boolean refresh){
	    this.recursive = rec;
	    this.tthash = refresh;
		subDir = new Vector();
		subFile = new Vector();
		size = 0;
	}
	*/
	public ShareDir(String path, boolean rec, boolean tthash) throws Exception {
		this.recursive = rec;
		this.tthash = tthash;
		File dir = new File(path);
		subDir = new Vector<ShareDir>();
		subFile = new Vector<ShareFile>();
		if (dir.isDirectory()) {
		    FullPath = dir.getAbsolutePath();
		    name = dir.getName();
			File[] tmp = dir.listFiles();
			for (int i=0; i<tmp.length && this.recursive;i++){
				if (tmp[i].isDirectory() && !tmp[i].isHidden()) {
					ShareDir sd = new ShareDir(tmp[i].getAbsolutePath(), recursive,tthash);
					
					subDir.add(sd);
				}
				if (tmp[i].isFile() && !tmp[i].isHidden()){
					try {
						ShareFile sf = new ShareFile(tmp[i].getAbsolutePath(), tthash);
						subFile.add(sf);
					}
					catch (Exception e){
						
					}
				}
			}
		this.size();
		}
		else 
		    throw new Exception();
	}
	
	
	private void size(){
		long size = 0;
		for (int i =0 ; i<subFile.size(); i++){
		    
			size += subFile.elementAt(i).getSize();
		}
		for (int i =0 ; i<subDir.size(); i++){
			size += subDir.elementAt(i).getSize();
		}
		this.size=size;
	}
	public String getName(){
	    return name;
	}
	public String toString(){
	    return name;
	}
	
/*	
	public void TTHash() {
		for (int i =0 ; i<subFile.size(); i++){
			((ShareFile)subFile.elementAt(i)).TTHash();
		}
		for (int i =0 ; i<subDir.size(); i++){
			((ShareDir)subDir.elementAt(i)).TTHash();
		}
	}
*/
	public long getSize() {
		return size;
	}

	public ShareDir[] getSubDir(){
	    ShareDir[] tmp = new ShareDir[subDir.size()];
	    for (int i = 0; i<subDir.size(); i++){
	        tmp[i] = subDir.elementAt(i);
	    }
	    return tmp; 
	}
	
	public ShareFile[] getSubFile(){
	    ShareFile[] tmp = new ShareFile[subFile.size()];
	    for (int i = 0; i<subFile.size(); i++){
	        tmp[i] = subFile.elementAt(i);
	    }
	    return tmp; 
	}
	
/*	public void refresh() {
	    //TODO Seganalare la lunghezza di questa operazione
	    size();
	    this.TTHash();
	}*/


/*	public boolean isIn(String name) {
		for (int i = 0; i<subFile.size();i++){
		    if ( ((String)subFile.elementAt(i)).endsWith(name) ){
		        return true;
		    }
		}
		boolean ret = false;
		for (int i = 0; i<subDir.size() && !ret; i++){
		    ret = ((ShareDir)subDir.elementAt(i)).isIn(name);
		}
		return ret;
		//while ()
	}*/
	
	public void match(String text, java.util.Vector result){
	    text = text.toLowerCase();
	    StringTokenizer tokenizer = new StringTokenizer(text,"$");
	    boolean c = true;
		while (tokenizer.hasMoreElements() && c){
		    if (((name).toLowerCase()).indexOf(tokenizer.nextToken()) < 0){ //Controllo per vedere se all'interno del file e' contenuta la stringa che si cerca
		        c = false;
		    }
		}
		if (c){
		    result.addElement(FullPath);
		}
		
		if (recursive){
		    for (int i =0; i<subDir.size(); i++) //ricercare nelle sottodir
		        ((ShareDir)subDir.elementAt(i)).match(text, result);
		}//ricercare nei sottofiles
		for (int i =0; i<subFile.size(); i++) //ricercare nelle sottodir
	        ((ShareFile)subFile.elementAt(i)).match(text, result);
	}
	
	public void printOut(String baseDir, PrintStream out){
	    for (int i = 0; i < subDir.size(); i++){
		subDir.elementAt(i).printOut(baseDir, out);
	    }
	    for (int i = 0; i < subFile.size(); i++){
		subFile.elementAt(i).printOut(baseDir, out);
	    }
	}
	public void tryPrintOut(String baseDir, PrintStream out) throws IllegalStateException{
	    for (int i = 0; i < subDir.size(); i++){
		subDir.elementAt(i).tryPrintOut(baseDir, out);
	    }
	    for (int i = 0; i < subFile.size(); i++){
		subFile.elementAt(i).tryPrintOut(baseDir, out);
	    }
	}
}
