/*
 * Created on Jan 5, 2007
 *
 */
package src.serverUtils;

import java.rmi.Naming;

public class Client {
    /**
     * Classe di prova per verificare se funziona RMI ;-)
     */

    /**
     * @param args
     */
    public static void main(String[] args) {
        try{
            ChannelList c = (ChannelList)Naming.lookup("ChannelList");
            
            String s[] = c.getAllChannels();
            for (int i = 0; i<s.length; i++)
                System.out.println(s[i]);
            System.out.println("Rimuovo Prova....");
            
            String s2[] = c.getAllChannels();
            for (int i = 0; i<s2.length; i++)
                System.out.println(s2[i]);
        }catch (Exception e){
            e.printStackTrace();
        }
        

    }

}
