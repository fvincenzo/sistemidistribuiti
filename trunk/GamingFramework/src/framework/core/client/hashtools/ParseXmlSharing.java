/*
 * Created on 2-feb-2006
 * 
 */
package framework.core.client.hashtools;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.parsers.FactoryConfigurationError;
//import javax.xml.parsers.ParserConfigurationException;
//import org.xml.sax.SAXException;
//import org.xml.sax.SAXParseException;
import java.io.File;
//import java.io.IOException;
import org.w3c.dom.Document;
//import org.w3c.dom.DOMException;
//import org.w3c.dom.Element;
import javax.xml.transform.*;
/**
 * @author noname
 * 
 */
public class ParseXmlSharing {

    public static void main(String[] args) {
        
        String[] dir = new String[1];
		dir[0]= "/Users/noname/provadir";
		Sharing s = new Sharing();
		s.setShareDir(dir);
    }
    public static void creaShare(String path){    
    		/*DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
         try {
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.newDocument();  
                // Create from whole cloth
                Element sharing = (Element)document.createElement("sharing");
                
                document.appendChild(sharing);
                writeXmlFile(document, "prova.xml");
         }
         catch (Exception e )
         {
             
             System.out.println(e.getMessage());
         }
        
          Leggere un documento*/
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
        //factory.setValidating(true);   
        //factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File("PublicHubList.xml"));
        System.out.println("Fatto");
        }
        catch (Exception e ){
            System.out.println(e.getMessage());
        }/**/
        }
    
        
//  This method writes a DOM document to a file
    public static void writeXmlFile(Document doc, String filename) {
        try {
            // Prepare the DOM document for writing
            Source source = new javax.xml.transform.dom.DOMSource(doc);
    
            // Prepare the output file
            File file = new File(filename);
            Result result = new javax.xml.transform.stream.StreamResult(file);
    
            // Write the DOM document to the file
            Transformer xformer = TransformerFactory.newInstance().newTransformer();
            xformer.transform(source, result);
        } catch (TransformerConfigurationException e) {
        } catch (TransformerException e) {
        }
    }
}
