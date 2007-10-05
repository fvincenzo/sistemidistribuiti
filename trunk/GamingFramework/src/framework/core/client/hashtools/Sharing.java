 /*
 * Created on Oct 19, 2005
 *
 */
package framework.core.client.hashtools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
//import java.io.FileDescriptor;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.IOException;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
//import org.w3c.dom.Node;
//import org.xml.sax.SAXException;

//import exceptions.DirErrorException;
//import exceptions.FileErrorException;
//import exceptions.FileErrorException;

/**
 * @author noname
 *
 */
public class Sharing {
    
    private boolean recursive = true;
    //TODO Estendere con un vettore delle directory e un array di dir daaggiungere
    private ShareDir[] sd;
    private String[] dir;
    private String shareSize;
    private java.util.Vector sdVect = new java.util.Vector();
    private Document sharingDoc = null;
    
    public Sharing(){
    
	/*File shareFile  = new File("sharing.xml");
        if (shareFile.exists() && shareFile.canRead()){
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            try {
                DocumentBuilder builder = factory.newDocumentBuilder();
                sharingDoc = builder.parse(shareFile);
                if (sharingDoc.hasChildNodes()){
                    sharingDoc = null;
                }
                
            }
            catch (Exception e){
                this.sharingDoc = null;
            }
        }  
        */
    }
    
    //TODO change search implementation due document type
    public Object[] search(String s){
        java.util.Vector res = new java.util.Vector();
       
        for (int i = 0; i<sd.length; i++)
            sd[i].match(s, res);
        /* for (int i = 0; i<res.size(); i++){
            if (sdVect.elementAt(i) instanceof ShareDir){
                if (((ShareDir)sdVect.elementAt(i)).match(s)){
                    res.add((ShareDir)sdVect.elementAt(i));
                }
            }
            if (sdVect.elementAt(i) instanceof ShareFile){
                if (((ShareFile)sdVect.elementAt(i)).match(s)){
                    res.add((ShareFile)sdVect.elementAt(i));
                }
            }
        }
    */    
        return res.toArray();
    
    }
    
    
    public String getShareSize(){
        return shareSize;
    }
    
    public void setRecursive(boolean r){
       this.recursive = r;
    }
    
    public boolean getRecursive(){
        return this.recursive;
    }

	/*
	 * public void setShareDir(String[] dir) {
	    if (this.recursive){
		if (dir.length>1) 
			dir = checkDir(dir);
		sd = new ShareDir[dir.length];
		long size =0;
		for (int i=0; i<dir.length; i++){
			try {
				sd[i] = new ShareDir(dir[i]);
				size += sd[i].getSize();
			}
			catch (DirErrorException e){
			    //TODO DebugInfo
			    System.out.println("Exception occurred in dir "+dir[i]);
			}
		}
		
		this.shareSize = Long.toString(size);
	    }
	    else {  //non ricorsivo: inserisci tutte le dir
	        sd = new ShareDir[dir.length];
			long size =0;
			for (int i=0; i<dir.length; i++){
				try {
					sd[i] = new ShareDir(dir[i]);
					size += sd[i].getSize();
				}
				catch (DirErrorException e){
				    //TODO DebugInfo
				    System.out.println("Exception occurred in dir "+dir[i]);
				}
			}
			this.shareSize = Long.toString(size);
	    }
	}
	
	*/
    
    public String getShareDir(int i){
        return sd[i].getName();
    }
    
    public int numShareDir(){
        return sd.length;
    }
    
    private long createShareTree(String path, Element supEl, Document doc){
        File dir = new File(path);
        long size = 0;
        if (dir.isDirectory()) {
           	File[] tmp = dir.listFiles();
			
           	for (int i=0; i<tmp.length ;i++){
				if (tmp[i].isDirectory() && !tmp[i].isHidden() && this.recursive) {
				    Element tmpEl =(Element)doc.createElement("shareDir");
				    tmpEl.setAttribute("absolutePath", tmp[i].getAbsolutePath());
				    tmpEl.setAttribute("name", tmp[i].getName());
				    size = size+createShareTree(tmp[i].getAbsolutePath(), tmpEl, doc);
				    tmpEl.setAttribute("size", Long.toString(size));
				    supEl.appendChild(tmpEl);
				}
				if (tmp[i].isFile() && !tmp[i].isHidden()){
				    	    long tmpl = tmp[i].length();
						Element tmpFileEl =(Element)doc.createElement("shareFile");
						tmpFileEl.setAttribute("absolutePath", tmp[i].getAbsolutePath());
					    tmpFileEl.setAttribute("name", tmp[i].getName());
					    tmpFileEl.setAttribute("Size", Long.toString(tmpl));
					    tmpFileEl.setAttribute("tthash","null");
					    supEl.appendChild(tmpFileEl);
					    size = size+tmpl;
				}
			}
           	
        }
        
        return size;
    }
    
    private void createShare(String[] path){    
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            sharingDoc = builder.newDocument();  
            // Create from whole cloth
            long tmpSize= 0;
            for (int i = 0; i < path.length; i++){
                Element sharing = (Element)sharingDoc.createElement("sharing");
                sharing.setAttribute("fullpath",path[i]);
 
                long size = createShareTree(path[i], sharing, sharingDoc);
                
                sharing.setAttribute("size",Long.toString(size));
                tmpSize=tmpSize+size;
                sharingDoc.appendChild(sharing);
            }
            this.shareSize = Long.toString(tmpSize);
            writeXmlFile(sharingDoc);
            
            
        }
        catch (Exception e )
        { 
            System.out.println(e.getMessage());
        }
    }
    private void syncNode(Element n){
        File f = new File(n.getAttribute("fullpath"));
        if (f.exists()) {
            if (n.getNodeName().equals("shareDir")) {
                //this is a shareDir we have to control also the childSubdirs in the 
                //filesystem and check that they are here in the node
                
            }
            else if (n.getNodeName().equals("shareFile") && f.length()==Long.parseLong(n.getAttribute("size"))) {
                
            }
            else {
                //Error while parsing the file... the file is not valid!!!
            }
            
        } else {
            //
            
        }
        
    }
    private void refresh(String[] path){
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            try {
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document tmpDoc = builder.newDocument();  
                // Create from whole cloth
                long tmpSize= 0;
                for (int i = 0; i < path.length; i++){
                    Element sharing = (Element)tmpDoc.createElement("sharing");
                    sharing.setAttribute("fullpath",path[i]);
     
                    long size = createShareTree(path[i], sharing, tmpDoc);
                    
                    sharing.setAttribute("size",Long.toString(size));
                    tmpSize=tmpSize+size;
                    tmpDoc.appendChild(sharing);
                }
                /**
                 * A questo punto abbiamo 2 alberi:
                 * sharingDoc e tmpDoc e dobbiamo 
                 * controllare che siano uguali
                 * ed eventualmente tenere il secondo
                 * ma senza ricalcolare l'hash
                 */
                if (!tmpDoc.equals(sharingDoc)){
                    shareSize = Long.toString(tmpSize);
                    this.sharingDoc = tmpDoc;
                    writeXmlFile(tmpDoc);
                }
                        
                
                
                
            }
            catch (Exception e )
            { 
                System.out.println(e.getMessage());
            }
        
        
    }
    
    private void writeXmlFile(Document d) {
        try {
            // Prepare the DOM document for writing
            Source source = new javax.xml.transform.dom.DOMSource(d);
            
            // Prepare the output file
            File file = new File("sharing.xml");
            Result result = new javax.xml.transform.stream.StreamResult(file);
            
            // Write the DOM document to the file
            Transformer xformer = TransformerFactory.newInstance().newTransformer();
            xformer.transform(source, result);
        } catch (TransformerConfigurationException e) {
        } catch (TransformerException e) {
        }
    }
    
    public void setShareDir(String[] dir) {
	    //if (this.recursive){
	this.dir= dir;
        if (dir.length >1 && recursive) 
	        //dir = checkDir(dir);
        	this.dir = checkDir(dir);
        if (sharingDoc == null){
		/*
		//create here the document
		//
		    createShare(dir);
		}
		else {
		    refresh(dir);
		}
        */
		sd = new ShareDir[dir.length];
		long size =0;
		for (int i=0; i<dir.length; i++){
			try {
				sd[i] = new ShareDir(dir[i], this.recursive, true);
				size += sd[i].getSize();
			}
			catch (Exception e){
			    //TODO DebugInfo
			    System.out.println("Exception occurred in dir "+dir[i]);
			}
		}
		this.shareSize = Long.toString(size);
        }
		
		
	    }
	  /* 	for (int i=0; i<dir.length; i++){
				try {
					sdVect.add(new ShareFile(dir[i]));
					size += sd[i].getSize();
				}
				catch (FileErrorException e){
				    //TODO DebugInfo
				    System.out.println("Exception occurred in dir "+dir[i]);
				}
			}
			this.shareSize = Long.toString(size);
	    
	}*/
	private String[] checkDir(String[] dir){
	    
		Vector tmp = new Vector();
		
		for (int i = 0; i < this.sd.length; i++){
			   tmp = addDir (tmp, this.sd[i].getName());
			}
	
		for (int i = 0; i < dir.length; i++){
		   tmp = addDir (tmp, dir[i]);
		}
		String[] ret = new String[tmp.size()];
		for (int i = 0; i< tmp.size(); i++){
		    ret[i]=(String)tmp.elementAt(i);
		}
		return ret; 
	}
	
	private Vector addDir(Vector v, String dir){
	    boolean add = true;
	    for (int i = 0; i<v.size() && add; i++){
	        if ( dir.startsWith((String)v.elementAt(i)) ){
	            add=false;
	        }
	        else if ( ((String)v.elementAt(i)).startsWith(dir) ) {
	            v.removeElementAt(i);
	            v.add(i,dir);
	            add=false;
	        }
	    }
	    if (add){
	        v.add(dir);
	    }
	    return v;
	}
	
	/**
	 * 
	 * @param out E' il printStream su cui verranno scritti i risultati
	 */
	public void printOut(PrintStream out){
	    for (int i = 0 ; i< sd.length; i++){
		sd[i].printOut(this.dir[i], out);
	    }
	    
	}
	
	public void tryPrintOut(PrintStream out) throws IllegalStateException {
	    for (int i = 0 ; i< sd.length; i++){
		sd[i].tryPrintOut(this.dir[i], out);
	    }
	}
	
	

}
