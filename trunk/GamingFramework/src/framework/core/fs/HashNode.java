package framework.core.fs;

public class HashNode extends Node {

private UFile file;
	
	public HashNode(UFile file) throws NullPointerException{
			
		if(file!=null) {
			this.file = file;
			setName(file.getHash());
		} else throw new NullPointerException();
			
	}
	/**
	 * Metodo che ritorna un oggetto di tipo file
	 * @return un file dell'utente x
	 */
	public UFile getUFile() {
		return this.file;
	}
	
	/**
	 * Overrida il Metodo della classe Nodo
	 */
	public String getInfo() {
		
		return "Owner:"+file.getOwner()+"\n"+"File:"+file.getName()+"\n"+"Hash:"+file.getHash()+"\n";
		
	}

	
}
