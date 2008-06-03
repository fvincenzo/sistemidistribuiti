package test;

@SuppressWarnings("serial")
public class SumTask extends Task {

    public Integer a;
    public Integer b;
    public Integer result;
    
    public SumTask() {}
    
    public SumTask(Integer a, Integer b) {
        this.a = a;
        this.b = b;
    }
    
    protected void computeResult() {
        result = a+b;
    }
    
}
