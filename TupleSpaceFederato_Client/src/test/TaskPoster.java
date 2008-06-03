package test;
/*
 * Created on Nov 6, 2006
 *
 */


import java.rmi.RemoteException;
import net.jini.core.entry.UnusableEntryException;
import net.jini.core.lease.Lease;
import net.jini.core.transaction.TransactionException;
import tuplespace.NodoLocale;
public class TaskPoster {

    static NodoLocale js;
    /**
     * @param args
     * @throws Exception 
     * @throws Exception 
     * @throws InterruptedException 
     * @throws UnusableEntryException 
     * @throws TransactionException 
     * @throws RemoteException 
     */
    public static void main(String[] args) throws Exception  {
        js = new NodoLocale("jini://localhost", "jini://localhost");
        js.setFederationServiceAddress("localhost");
        if (js.cercaFederazione("fed1")){
        	js.join("fed1");
        }
        else {
        	if (!js.creaFederazione("fed1")) System.out.println("ERRORE!!");
        	js.join("fed1");
        }
        System.out.println("Fatto");
//        System.out.println("Mi metto in attesa per prendere il risultato");
//        SumTask s = (SumTask)js.take(new Task(), null, Lease.FOREVER);
//        System.out.println(""+s.a + " "+s.b);
        
        SumTask sumTask = new SumTask(2,9);
        System.out.println("Mi preparo a scrivere il task");
        js.write(sumTask, null, Lease.FOREVER);
        System.out.println("Scrtto");

        
        SumTask executedTask = (SumTask) js.take(sumTask.getResultTemplate(), null, Long.MAX_VALUE);
        System.out.println(executedTask.result);
        js.leave();
//        js.creaFederazione("jini://localhost", "Federazione1");
    }

}
