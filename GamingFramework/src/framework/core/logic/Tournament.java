package framework.core.logic;
import java.util.*;

import framework.core.fs.Node;
import framework.core.usermanager.Account;
import framework.core.usermanager.db.DbObject;

/**
 * Rappresenta un torneo all'interno del circuito arena.
 * @author Vincenzo Frascino
 */
public class Tournament extends DbObject {
	protected String description = "No description yet.";
	protected Game game;
	protected int type;
	protected Account owner;
	private Vector players;
	private Hashtable classifica; 
	private boolean open;
	private Node matches;

	public static final int SINGLE_PLAYER = 0;
	public static final int LEAGUE = 1;
	public static final int PLAYOFF = 2;
	public static final int TYPE_NUM = 3;

	/**
	 * Costruttore della classe Tournament.
	 * @param g Instanza del gioco associato al torneo
	 * @param type Tipo di torneo
	 * @param name Nome del torneo
	 * @param db Istanza del server sql utilizzato per la persistenza.
	 */
	public Tournament(String name, Account owner, int type, Game game) throws NullPointerException, IllegalArgumentException	{
		//if(game == null) throw new NullPointerException();
		//if(type > 2 || type < 0) throw new IllegalArgumentException();
		setName(name);
		this.owner = owner;
		if(game == null) throw new NullPointerException();
		this.game =(Game)game;
		this.type = type;
		players = new Vector();
		classifica = new Hashtable();
		open = true;
		matches = new Node("matches");
		this.addChild(matches);
	}
	/**
	 * Restiuisce il tipo del torneo
	 * @return Un intero che specifica il tipo del torneo
	 * TODO: Aggiungere descrizione dei tipi di torneo nella documentazione.
	 */
	public int getType() {
		return type;
	}
	/**
	 * Restituisce il gioco associato al torneo.
	 * @return Un istanza della classe Game che descrive il gioco associato
	 * al torneo.
	 */
	public Game getGame() {
		return game;
	}
	public boolean isOpen() {
	    return open;
	}
	public Account getOwner() {
	    return owner;
	}
	public String getDescription() {
		return description;
	}
	public boolean subscribe(Account acc) {
	    if(open) {
		players.add(acc);
		classifica.put(acc,new Integer(0));
		save();
		return true;
	    }
	    return false;
	}
	public void unsubscribe(Account acc) {
	    players.remove(acc);
	}
	/**
	 * Metodo che genera i tipi di torneo.
	 *
	 */
	public void start() {
	    open = false;
	    if(type == 0) {
	    	Object[] accs = null;
    		accs = players.toArray();
	    	if(players.size() == 1){
	    		Match m = new Match((Account)accs[0],game,this);
	    		matches.addChild(m);
	    		m.save();
	    	}
	    }else if(type == 1) {
	    	Object[] accs = null;
	    	accs = players.toArray();
	    	int i, j;
	    	for(i=0;i<accs.length-1;i++) {
	    		for(j=i+1;j<accs.length;j++) {
	    			Match m = new Match(((Account)accs[i]),((Account)accs[j]),game,this,1);
	    			matches.addChild(m);
	    			m.save();
	    		}
	    	}
	    }else if(type == 2){
	    	Object[] accs = null;
			accs = players.toArray();
			int k = players.size();
			int i;
			if(Utilities.dueAllaIMultiple(k)){
				for(i=0;i<k/2;i++){
					Match m = new Match(((Account)accs[i]),((Account)accs[(k/2)+i]),game,this,2);
					matches.addChild(m);
					m.save();
				}
			}else{
				System.out.println("Per generare questo tipo di torneo servono un numero di squadre pari.");
			}
		}else{
	    	System.out.println("Tipo di torneo sconosciuto!");
	    }
	}
	public void setDescription(String descr) throws NullPointerException{
		if(descr != null) {
			description = descr;
		} else throw new NullPointerException();
	}
	public void setObjectData(Dictionary data) {
		type = ((Integer)data.get("Type")).intValue();
		open = ((Boolean)data.get("Open")).booleanValue();
		description = (String)data.get("Description");
		game = (Game)find("/games/" + data.get("Game"));
		owner = (Account)find("/accounts/" + data.get("Owner"));
		String playersString = (String)data.get("Players");
		StringTokenizer t = new StringTokenizer(playersString, " ");
		while(t.hasMoreElements()) {
		    String e = t.nextToken(); 
		    Account acc = (Account)find("/accounts/" + e);
		    if(acc != null) {
			players.add(acc);
		    }
		}
	}
	public Dictionary getObjectData() {
		Dictionary d = new Hashtable();
		d.put("Game", game.getName());
		d.put("Type", new Integer(type));
		d.put("Open", new Boolean(open));
		d.put("Owner", owner.getName());
		d.put("Description", description);
		String playersString = "";
		Enumeration e = players.elements();
		while(e.hasMoreElements()) {
		    playersString = playersString + ((Account)e.nextElement()).getName() + " ";
		}
		d.put("Players", playersString);
		return d;
	}
	/**
	 * Converte il codice di una modalit� di torneo in una stringa
	 * descrittiva della modalit�.
	 * @param t Il codice del tipo di torneo
	 * @return Una stringa contenente una descrizione breve della modalit�
	 * di gioco.
	 * @throws IllegalArgumentException Se il codice del tipo di torneo non
	 * � riconosciuto tra quelli supportati.
	 */
	public static String typeToString(int t) throws IllegalArgumentException {
		switch(t) {
			case SINGLE_PLAYER:
				return "Giocatore singolo";
			case LEAGUE:
				return "Torneo all'italiana";
			case PLAYOFF:
				return "Eliminazione diretta";
			default:
				throw new IllegalArgumentException();
		}
	}
}
