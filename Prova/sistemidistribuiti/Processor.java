/*
 * Created on 18/set/07
 * 
 */
package sistemidistribuiti;

import java.util.Vector;

public class Processor {
    int num = 0;
    private Vector<Thread> v = new Vector<Thread>();
    private Vector<Object> r = new Vector<Object>();
 
    public synchronized int addJob(final Job j){
	r.add(null);
	num++;
	v.add(new Thread(new Runnable(){
	  
	    private int n = num;
	    public void run(){
		r.setElementAt(j.doJob(), n-1);
	    }
	    
	}));
	v.elementAt(num-1).start();
	return num-1;
    }
    
    public boolean isRunning(int jobN){
	return v.elementAt(jobN).isAlive();
    }
    
    public Object getResult(int jobN){
	return r.get(jobN);
    }
    

}
