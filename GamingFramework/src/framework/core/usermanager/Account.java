package framework.core.usermanager;

import java.util.*;

import framework.core.usermanager.db.DbObject;

/**
 * Implementa un Account che contiene informazioni riguardo un singolo utente. 
 * @author Vincenzo Frascino
 */
public class Account extends DbObject {
	protected String password;
	protected boolean admin;
	protected String firstName;
	protected String surname;
	/**
	 * Costruttori della classe Account
	 */
	public Account()  {
		admin = false;
		password = "";
		firstName = "";
		surname = "";
	}
	/**
	 * Imposta il Nome associato all'account
	 * 
	 * @param a
	 *            nome
	 */
	public void setFirstName(String a) {
		firstName = a;
	}
	/**
	 * @return il Nome associato all'account
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * Imposta il cognome associato all'account
	 * 
	 * @param a
	 *            cognome
	 */
	public void setSurname(String a) {
		surname = a;
	}
	/**
	 * @return il Cognome associato all'account
	 */
	public String getSurname() {
		return surname;
	}
	/**
	 * Imposta la password per l'account.
	 * 
	 * @param a
	 *            password
	 */
	public void setPassword(String a) {
		password = a;
	}
	/**
	 * @return La password associata all'account.
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * Imposta la tipologia di utente.
	 * 
	 * @param a
	 *            True se l'utente � un amministratore, false altrimenti.
	 */
	public void setAdmin(boolean a) {
		admin = a;
	}
	/**
	 * @return Restituisce true se l'account � amministratore, false altrimenti.
	 */
	public boolean getAdmin(){
		return admin;
	}
	/**
	 * Metodo utile a scrivere i dati sul db.
	 */
	public void setObjectData(Dictionary data) {
		firstName = (String)data.get("FirstName");
		surname = (String)data.get("Surname");
		password = (String)data.get("Password");
		admin = ((Boolean)data.get("Admin")).booleanValue();
	}
	/**
	 * Metodo utile a leggere i dati sul db.
	 */
	public Dictionary getObjectData() {
		Hashtable res = new Hashtable();
		res.put("FirstName", firstName);
		res.put("Surname", surname);
		res.put("Admin", Boolean.valueOf(admin));
		res.put("Password", password);
		return res;
	}
}