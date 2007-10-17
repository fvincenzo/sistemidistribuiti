/*
 * Created on 18/set/07
 * 
 */
package sistemidistribuiti;

public class Task implements Job {

    private int num;
    private Object result;
    private char c;
    public Task(int num, char c, Object result){
	this.num = num;
	this.c = c;
	this.result = result;
    }
    
    public Object doJob() {
	for (long i = 0; i<num; i++){
	    System.out.print(c);
	}
	return result;
    }

}
