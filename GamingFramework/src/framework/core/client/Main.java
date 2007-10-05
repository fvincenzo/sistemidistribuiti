package framework.core.client;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//Lancio il server di FileSharing
        new Thread(new ClientFile()).start();

	}

}
