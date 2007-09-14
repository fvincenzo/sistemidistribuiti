package framework.core.usermanager.db;
import java.sql.*;
import java.util.*;

import framework.core.GameServer;

/**
 * Implementa un semplice server sql in grado di connettersi al database di arena
 * e gestire la persistenza dei dati.
 * DbServer fornisce metodi per gestire tabelle distinte da un singolo campo unico
 * di tipo stringa denominato 'Name' e campi aggiuntivi di tipo abitrario. Questa 
 * struttura di tabella facilita la sincronizzazione dei dati fra database e struttura
 * a nodi di GameServer. 
 * @author Vincenzo Frascino
 */
public class DbServer {
	private Connection conn;
	private GameServer arena;
	
	// Singleton instance.
	private static DbServer instance = null;
	/**
	 * Costruttore della classe DbServer.
	 * throws InstantiationException se un'istanza della classe DbServer � gi�
	 * stata creata.
	 */
	public DbServer() throws InstantiationException {
		if(instance == null) {
			instance = this;
			arena = GameServer.getInstance();
		}
		throw new InstantiationException();
	}
	/**
	 * Restituisce l'istanza sigleton di DbServer per il processo corrente.
	 * Se un'istanza di DbServer non � ancora stata creata, getInstance genera e
	 * restituisce una nuova istanza singleton di DbServer.
	 * @return Un'istanza singleton di DbServer.
	 */
	public static DbServer getInstance() {
		if(instance == null) {
			try {
				return new DbServer();
			} catch(InstantiationException e) {
				// Qui non ci finiremo mai.
			}
		}
		return instance;
	}
	/**
	 * Tenta la connessione al database specificato.
	 * @param host L'host sul quale � attivo il database, specificato nella forma
	 * 'hostname:port'
	 * @param dbName Il nome del database al quale si desidera accedere.
	 * @param user Il nome utente da utilizzare per l'autenticazione.
	 * @param password La password da utilizzare per l'autenticazione.
	 * @return True se la connessione ha avuto successo, false altrimenti.
	 */
	public boolean init(String host, String user, String password) {
		// EFFECTS Tenta la connessione al server del database.
		// MODIFIES conn;
		arena.log(":: DbServer init()");
		String addr = "jdbc:mysql://" + host;
		try {
			Class.forName("org.gjt.mm.mysql.Driver");
			conn = DriverManager.getConnection(addr, user, password);
			GameServer.getInstance().log("DbServer: connesso a " + addr); 
		}catch(Exception e) {
			// Facciamo un p� di diagnostica, che bravi che siamo..
			arena.log(":: DbServer init() fallito: " + e.toString());
			return false;
		}
		arena.log(":: DbServer init() ok");
		return true;
	}
	/**
	 * Chiude la connessione con il server del database.
	 * Se DbServer non � connesso ad alcun database, il metodo restituisce
	 * il controllo senza eseguire alcuna operazione.
	 */
	public void fini() {
		// EFFECTS Chiude la connessione con il server del database.
		// MODIFIES conn
		arena.log(":: DbServer fini()");
		if(conn != null) {
			try {
				conn.commit();
				conn = null;
			} catch(SQLException e){
				// Serve far qualcosa?
			}
		}
		arena.log(":: DbServer fini() ok");
	}
	/**
	 * Restituisce lo stato di connessione del server.
	 * @return True se il server � connesso ad un database, false altrimenti.
	 */
	public boolean isConnected() {
		if(conn != null) {
			return true;
		}
		return false;
	}
	/**
	 * Carica in un dizionario tutti i dati della tabella specificata.
	 * Il dizionario associa chiavi di tipo stringa, corrispondenti al campo
	 * univoco 'Name' presente nelle righe della tabella, a coppie (nome,oggetto)
	 * contenenti le informazioni aggiuntive associate alla chiave.
	 * @param name Il nome della tabella dalla quale prelevare i dati.
	 * @return
	 */
	public Dictionary getTableData(String name) throws NullPointerException,
													IllegalStateException {
		if(name != null) {
			if(conn != null) {
				String query = "SELECT * FROM " + name;
				Hashtable data = null;
				try {
					Statement stm = conn.createStatement();
					ResultSet rs = stm.executeQuery(query);
					if(rs != null) {
						data = new Hashtable();
						while(rs.next()) {
							// Copia i dati di ogni riga nella hashtable associandoli
							// al campo 'Name' della riga stessa.
							int len = rs.getMetaData().getColumnCount();
							Hashtable rowData = new Hashtable();
							int i;
							for(i = 2; i <= len; i++) {
								String columnName = rs.getMetaData().getColumnLabel(i); 
								rowData.put(columnName, rs.getObject(i));
							}
							data.put(rs.getString("Name"), rowData);
						}
					}
					return data;
				} catch(SQLException e) {
					GameServer.getInstance().log("DbServer: query " + query + " raised following exception: \n" + e.toString());
					return null;
				}
			} else throw new IllegalStateException();
		} else throw new NullPointerException();
	}
        /**
         * Riempie una riga della tabella specificata con i dati passati come parametro.
         * Se una riga con il nome rowName non esiste nella tabella identificata da tableName,
         * il metodo genera una nuova entry nel database.
         * @param tableName il nome della tabella da aggiornare.
         * @param rowName il campo 'Name' della riga da aggiornare.
         * @param data Dizionario contenente coppie (campo, valore) che rappresentano i dati
         * da inserire nella riga del database.
         */
	public boolean setRow(String tableName, String rowName, Dictionary data) {
		if(tableName != null && rowName != null && data != null) {
			if(conn != null) {
				String query = "[<undefined>]";
				try {
                                    Statement stm = conn.createStatement();
                                    String fields = "Name,";
                                    String values = "'" + rowName + "',";
                                    String updates = "";
                                    Enumeration keys = data.keys(); 
                                    while(keys.hasMoreElements()) {
                                            String keyName = (String)keys.nextElement(); 
                                            String value = "";
                                            Object o = data.get(keyName);
                                            // Converti i dati booleani in interi mentre scrivi
                                            // i valori.
                                            if(o.getClass() != Boolean.class) {
                                                    value = "'" + data.get(keyName) + "'";
                                            } else {
                                                    Boolean b = (Boolean)o;
                                                    if(b.booleanValue() == true) {		  						
                                                            value = "'1'";
                                                    } else {
                                                            value = "'0'";
                                                    }
                                            }
                                            fields = fields + keyName;
                                            values = values + value;
                                            updates = updates + keyName + "=" + value;
                                            if(keys.hasMoreElements()) {
                                                    fields = fields + ",";
                                                    values = values + ",";
                                                    updates = updates + ",";
                                            }
                                    }
                                    query = "SELECT * FROM " + tableName + " WHERE " + tableName + ".Name = '" + rowName + "'";
                                    ResultSet res = stm.executeQuery(query);
                                    if(!res.next()) {
                                        query="INSERT INTO " + tableName + " (" + fields + ") VALUES (" + values + ")"; 
                                        stm.executeUpdate(query);
                                    } else {
                                        query="UPDATE " + tableName + " SET " + updates + " WHERE " + tableName + ".Name = '" + rowName + "'";
                                        stm.executeUpdate(query);
                                    }
                                    return true;
				} catch(SQLException e) {
					GameServer.getInstance().log("DbServer: query " + query + " raised following exception: \n" + e.toString());
					return false;
				}
			} else throw new IllegalStateException();
		} else throw new NullPointerException();
		
	}
}
