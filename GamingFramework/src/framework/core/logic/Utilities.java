package framework.core.logic;

import framework.core.fs.Node;

/*
 * Created on 20-mag-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
/**
 * @author Vincenzo Frascino
 *
 * La classe contiene le utilities che vengono impiegate nel programma,
 * costituite da metodi statici.
 */
public class Utilities extends Node{
	/**
	 * Costruttore della classe Utilities
	 *
	 */
	public Utilities(){
		
	}
	/**
	 * Converte un booleano ad intero
	 * @param b booleano
	 * @return un intero
	 */
	public static int booleanToInt(boolean b){
		
		int a = 0;
		if(b == true){
			a = 1;
		}else{
			a = 0;
		}
		return a;
		
	}
	/**
	 * Converte un intero in booleano
	 * @param a intero
	 * @return un booleano
	 */
	public static boolean intToBoolean(int a){
		
		if(a==1){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * Calcola 2^x dato x.
	 * @param x 
	 * @return 2^x
	 */
	public static boolean dueAllaIMultiple(int x){
		int i;
		for (i=0;i<x;i++){
			if(Math.pow(2,i) == x){
				return true;
			}else if(Math.pow(2,i) > x){
				return false;
			}else{
				continue;
			}
		}
		return false;
	}
}
