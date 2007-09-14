package framework.core.fs;

/**
 * Astrae un file nel Filesystem Virtuale
 * 
 * @author Vincenzo Frascino
 *
 */
public class FileNode extends Node {
	
	private UFile file;
	
	public FileNode(UFile file) throws NullPointerException{
			
		if(file!=null) {
			this.file = file;
			setName(file.getName());
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
		
		return "Filename:"+file.getName()+"\n";
		
	}

}
