package tuplespace;


import net.jini.core.entry.Entry;
import net.jini.core.lease.Lease;
import net.jini.core.transaction.Transaction;

public interface NodoLocaleInterface{
	
	public Lease write(Entry e, Transaction t, long l) throws Exception;
	public Entry read(Entry e, Transaction t, long l) throws Exception;
	public Entry take(Entry e, Transaction t, long l) throws Exception;
	public Entry readIfExists(Entry e, Transaction t, long l) throws Exception;
	public Entry takeIfExists(Entry e, Transaction t, long l) throws Exception;

	public boolean creaFederazione(String nome);
	public boolean cercaFederazione( String nome);
	public boolean join(String nome); 
	public boolean leave();
	
}
