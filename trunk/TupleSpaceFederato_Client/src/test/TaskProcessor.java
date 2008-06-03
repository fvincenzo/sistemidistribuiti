package test;

import java.io.IOException;
import net.jini.core.lease.Lease;
import tuplespace.NodoLocale;
public class TaskProcessor {

	static NodoLocale js;

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		js = new NodoLocale("jini://localhost","jini://localhost" );
		js.setFederationServiceAddress("localhost");
		if (js.cercaFederazione("fed1")){
			js.join("fed1");
		}
		else {
			if (!js.creaFederazione("fed1")) System.out.println("ERRORE!!");
			js.join("fed1");
		}
		System.out.println("Federato");
//		System.out.println(js.leave());
		new TaskProcessor().run();

	}

	private void run() {
		while (true) {
			Task t;
			try {

				System.out.println("taking item");
				t = (Task) js.take(new Task(), null,Lease.FOREVER);
				if (t == null){
					System.out.println("non ho trovato niente");
				}else {
					System.out.println("executing item");
					t.execute();
					System.out.println("writing result");
					js.write(t, null, Lease.FOREVER);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
