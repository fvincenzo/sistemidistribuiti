/*
 * Created on 26-ott-2005
 * 
 */
package framework.core.client.hashtools;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

//import java.io.File;

/**
 * @author noname
 * 
 */
public class Prova {

    public static void main(String[] args) {
        if (args.length==0){
            System.out.println("Non hai inserito nulla!!");
        }
        else {
            //for (int i = 0; i<args.length; i++){
                if (args.length > 1){ //una sola dir inserita niente controlli
                    
                }
                else {  //pi� di una dir inserita: controllare che nessuna sia contenuta nelle altre
                    
                }
                try{
                    PrintStream out = new PrintStream(new FileOutputStream("fileout.share"));
                    Sharing s = new Sharing();
                    s.setRecursive(true);
                    s.setShareDir(args);
                    /*
                     * Richiesta non bloccante che al massimo lancia un'eccezione
                     * di tipo IllegalStateException
                     */
                    // s.tryPrintOut(out); 
                    /*
                     * Richiesta di tipo bloccante che aspetta la terminazione
                     * di tutti gli hash da parte del client
                     */
                    s.printOut(out);
                    Hash.getInstance().kill();
                    System.out.println("All done :-)");
                }
                catch(FileNotFoundException e ){
                    System.out.println("Impossibile creare il file di output!!!");
                }
            }
        //}
    }
}
