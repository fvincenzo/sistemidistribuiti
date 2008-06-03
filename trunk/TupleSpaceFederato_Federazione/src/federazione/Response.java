package federazione;

import tuplespace.NodoRemotoInterface;
import net.jini.core.entry.Entry;

public interface Response {
	
	public void response(NodoRemotoInterface source, Entry e);
	public void throwException(NodoRemotoInterface source, Exception e);
}
