package server;


/**
 * Generated from IDL interface "DatiSicuri".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 22-mag-2007 13.06.01
 */

public interface DatiSicuriOperations
{
	/* constants */
	/* operations  */
	void addListener(client.HandleListener l, java.lang.String id);
	core.Handle riservaSpazio();
	int leggiDato(core.Handle h) throws server.ReadErrorException;
	boolean scriviDato(core.Handle h, int dato);
	void pubblica(core.Handle h, java.lang.String destinatario);
}
