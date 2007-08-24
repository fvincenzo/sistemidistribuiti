/*
 * Created on 22/mag/07
 * 
 */
package client;


public interface ClientInterface {
    
    public boolean riservaSpazio();
    public int leggiDato(String id); /**ritornare null se non trova il dato*/
    public boolean scriviDato(String id, int valore);
    public boolean pubblica(String id, String destinatario);
    

}
