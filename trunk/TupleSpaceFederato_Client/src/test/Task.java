package test;

import java.io.Serializable;

import net.jini.entry.AbstractEntry;

@SuppressWarnings("serial")
public class Task extends AbstractEntry implements Cloneable, Serializable{

    public Boolean executed;
    
    public Task() {
        executed = false;
    }
    
    public Task getResultTemplate() {
        Task t = null;
        try {
            t = (Task) this.clone();
            t.executed = true;
        } catch (CloneNotSupportedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return t;
    }
    
    public void execute() {
        computeResult();
        executed = true;
    }
    
    protected void computeResult() {};
    
}
