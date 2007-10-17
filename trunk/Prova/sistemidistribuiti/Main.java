/*
 * Created on 18/set/07
 * 
 */
package sistemidistribuiti;

public class Main {

    /**
     * @param args
     */
    public static void main(String[] args) {
	Task t1 = new Task(10000,'1' ,"Task 1 finito");
	Task t2 = new Task(10000,'2' ,"Task 2 finito");
	Task t3 = new Task(10000,'3' ,"Task 3 finito");
	Task t4 = new Task(10000,'4' ,"Task 4 finito");
	Task t5 = new Task(10000,'5' ,"Task 5 finito");

	Processor p = new Processor();
	
	int i_t1 = p.addJob(t1);
	int i_t2 = p.addJob(t2);
	int i_t3 = p.addJob(t3);
	int i_t4 = p.addJob(t4);
	int i_t5 = p.addJob(t5);
	
	System.out.println("\n\nIl processo 1 ha numero "+ i_t1);
	System.out.println("\n\nIl processo 2 ha numero "+ i_t2);
	System.out.println("\n\nIl processo 3 ha numero "+ i_t3);
	System.out.println("\n\nIl processo 4 ha numero "+ i_t4);
	System.out.println("\n\nIl processo 5 ha numero "+ i_t5);
	while (p.isRunning(i_t1));
	System.out.println("\n\n"+p.getResult(i_t1));
	while (p.isRunning(i_t2));
	System.out.println("\n\n"+p.getResult(i_t2));
	while (p.isRunning(i_t3));
	System.out.println("\n\n"+p.getResult(i_t3));
	while (p.isRunning(i_t4));
	System.out.println("\n\n"+p.getResult(i_t4));
	while (p.isRunning(i_t5));
	System.out.println("\n\n"+p.getResult(i_t5));
	
    }

}
