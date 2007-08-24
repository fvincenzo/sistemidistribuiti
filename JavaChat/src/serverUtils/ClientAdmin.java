/*
 * Created on Jan 6, 2007
 *
 */
package src.serverUtils;

import java.rmi.Naming;

public class ClientAdmin {
    /**
     * 
     * Classe di prova per verificare se funziona RMI ;-)
     */

    /**
     * @param args
     */
    public static void main(String[] args) {
        try{
            ChannelListAdmin c = (ChannelListAdmin)Naming.lookup("ChannelList");
            c.addChannel("prova1");
            c.addChannel("prova1");
            c.addChannel("prova1");
            c.addChannel("prova1");
            c.addChannel("prova2");
            c.addChannel("prova3");
            c.addChannel("prova4");
            c.addChannel("prova5");
            c.addChannel("prova6");
            c.addChannel("prova3");
            c.addChannel("prova1");
            
            String s[] = c.getAllChannels();
            for (int i = 0; i<s.length; i++)
                System.out.println(s[i]);
            System.out.println("Rimuovo Prova2 e prova 9....");
            c.removeChannel("prova2");
            c.removeChannel("prova9");
            String s2[] = c.getAllChannels();
            for (int i = 0; i<s2.length; i++)
                System.out.println(s2[i]);
        }catch (Exception e){
            e.printStackTrace();
        }
        

    }

}
