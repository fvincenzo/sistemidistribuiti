package framework.core.logic;
/*
 * Created on 14-mag-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
import java.util.*;

import framework.core.usermanager.Account;
import framework.core.usermanager.db.DbObject;

/**
 * @author Vincenzo Frascino
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class Match extends DbObject {
    private Account p1;
    private Account p2;
    private Game game;
    private Tournament tournament;
    private boolean played;
    private int result;
    int type;
    /**
     * Costruttori della classe Match
     */
    public Match(Account p1, Account p2, Game game, Tournament t,int i) {
	this.p1 = p1;
	this.p2 = p2;
	this.game = game;
	tournament = t;
	result = 0;
	if(i == 1){
		setName(t.getName()+"_"+ p1.getName()+"_VS_"+p2.getName()+"__LEAGUE_MATCH");
		this.type=i;
	}else if(i == 2){
		setName(t.getName()+"_"+ p1.getName()+"_VS_"+p2.getName()+"__CUP_MATCH");
		this.type=i;
	}
	played = false;
    }
    
    public Match(Account p1, Game game, Tournament t) {
    	this.p1 = p1;
    	this.game = game;
    	this.type=0;
    	tournament = t;
    	result = 0;
    	setName(t.getName()+"_"+p1.getName()+"__SINGLE_PLAYER");
    	played = false;
        } 
   
    public Match() {
    }
    /**
     * Metodo utile a scrivere le partite nel db.
     */
    public java.util.Dictionary getObjectData() {
	Dictionary d = new Hashtable();
	d.put("Game", game.getName());
	d.put("P1", p1.getName());
	d.put("P2", p2.getName());
	d.put("Played", new Boolean(played));
	d.put("Result", new Integer(result));
	d.put("Tournament", tournament.getName());
	return d;
    }
    /**
     * Metodo utile a leggere le partite dal db.
     */
    public void setObjectData(java.util.Dictionary data) {
	game = (Game)find("/games/" + data.get("Game"));
	tournament = (Tournament)find("/tournaments/" + data.get("Tournament"));
	p1 = (Account)find("/accounts/" + data.get("P1"));
	p2 = (Account)find("/accounts/" + data.get("P2"));
	played = ((Boolean)data.get("Played")).booleanValue();
	result = ((Integer)data.get("Result")).intValue();
	if(this.type == 0){
		setName(tournament.getName()+"_"+p1.getName()+"__SINGLE_PLAYER");
	}else if(this.type == 1){
		setName(tournament.getName()+"_"+ p1.getName()+"_VS_"+p2.getName()+"__LEAGUE_MATCH");
	}else if(this.type == 2){
		setName(tournament.getName()+"_"+ p1.getName()+"_VS_"+p2.getName()+"__CUP_MATCH");
	}
    }
}
