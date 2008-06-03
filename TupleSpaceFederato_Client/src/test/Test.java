package test;


import java.rmi.Naming;

import federationservice.FederationServiceInterface;
import federazione.FederazioneInterface;


import tuplespace.NodoLocale;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		
		FederationServiceInterface fed= (FederationServiceInterface)Naming.lookup("rmi://localhost/FederationService");
		fed.creaFederazione("ciao1");
		fed.creaFederazione("ciao2");
		fed.creaFederazione("ciao3");
		fed.creaFederazione("ciao4");
		System.out.println(fed.cercaFederazione("ciao1"));
		FederazioneInterface f = fed.join(new NodoLocale("jini://localhost", "jini://localhost"), "ciao1");
		if (f == null) System.out.println("errore");
		else System.out.println("tutto ok");
	}

}
