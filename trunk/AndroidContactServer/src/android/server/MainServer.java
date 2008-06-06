package android.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MainServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub

		ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(4444);
        } catch (IOException e) {
            System.err.println("Non posso mettermi in ascolto sulla porta: 4444.");
            System.exit(1);
        }

        try {
        	while(true) {
        		 Socket clientSocket = serverSocket.accept();
        		 new SocketServer(clientSocket);
        	}
        } catch (IOException e) {
            System.err.println("Non posso accettare la connessione.");
            System.exit(1);
        }
		
	}

}
