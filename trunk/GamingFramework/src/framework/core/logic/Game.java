package framework.core.logic;

import framework.core.fs.Node;
import framework.core.usermanager.User;

/**
 * @author Vincenzo Frascino
 */
public abstract class Game extends Node {
	private int minPlayers;
	private int maxPlayers;
	protected String p_Description;
	/**
	 * costruttore della classe game
	 */
	public Game(int min,int max,String description){
		this.maxPlayers = max;
		this.minPlayers = min;
		p_Description = description;
	}
	/**
	 * @return il numero minimo di giocatori
	 */
	public int getMinPlayers() {
		return minPlayers;
	}
	/**
	 * 
	 * @return il numero massimo di giocatori
	 */
	public int getMaxPlayers() {
		return maxPlayers;
	}
	/**
	 * 
	 * @return la desrizione del gioco
	 */
	public String getDescription(){
		return p_Description;
 	}
	/**
	 * Gioca una partita.
	 * @param p1 Istanza del primo giocatore
	 * @param p2 Istanza del secondo giocatore. Pu� essere null se il gioco
	 * supporta la modalit� a giocatore singolo.
	 * @return Un intero i cui valori corrispondono ai seguenti stati:
	 * 		0 - I giocatori hanno pareggiato.
	 * 		1 - Il primo giocatore ha vinto.
	 * 		2 - Il secondo giocatore ha vinto.
	 */
	public abstract int Play(User p1, User p2);
}
