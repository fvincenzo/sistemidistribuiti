/*
 * Created on 22/mag/07
 * 
 */
package server;

import client.HandleListener;
import core.Handle;


public class DatiSicuriServant extends DatiSicuriPOA {

    public void addListener(HandleListener l, String id) {
        // TODO Implementare il metodo addListener
    }

    public int leggiDato(Handle h) throws ReadErrorException {
        // TODO Implementare il metodo leggiDato
    }

    public void pubblica(Handle h, String destinatario) {
        // TODO Implementare il metodo pubblica
    }

    public Handle riservaSpazio() {
        // TODO Implementare il metodo riservaSpazio
    }

    public boolean scriviDato(Handle h, int dato) {
        // TODO Implementare il metodo scriviDato
    }

}
