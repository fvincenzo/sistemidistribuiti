package test;

import net.jini.entry.AbstractEntry;

@SuppressWarnings("serial")
public class ProvaEntry extends AbstractEntry {

	private String testo;
	private int valore;
	/**
	 * @return the testo
	 */
	
	public boolean equals(Object o){
		if (o instanceof ProvaEntry){
			if (testo.equals(((ProvaEntry)o).getTesto()) && valore == ((ProvaEntry)o).getValore())
				return true;
		}
		return false;
	}
	public String getTesto() {
		return testo;
	}
	/**
	 * @param testo the testo to set
	 */
	public void setTesto(String testo) {
		this.testo = testo;
	}
	/**
	 * @return the valore
	 */
	public int getValore() {
		return valore;
	}
	/**
	 * @param valore the valore to set
	 */
	public void setValore(int valore) {
		this.valore = valore;
	}
	
}
