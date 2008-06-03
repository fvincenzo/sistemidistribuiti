package federationservice;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

import federazione.FederazioneInterface;

import tuplespace.NodoRemotoInterface;

public interface FederationServiceInterface extends Remote {
	
	public void creaFederazione(String nome) throws RemoteException;
	public boolean cercaFederazione(String nome) throws RemoteException;
	public Set<String> federazioniPresenti() throws RemoteException;
	public FederazioneInterface join(NodoRemotoInterface n, String nome) throws RemoteException;
	
}
