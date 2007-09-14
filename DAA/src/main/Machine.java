/*
 * Created on 07/giu/07
 *
 */
package main;


public class Machine implements Comparable<Machine> {
    private int number;
    private int load = 0;
    
    public Machine(int machine_number){
        this.number = machine_number;
    }
    
    public void addJob(int lenght) {
        load += lenght;
    }

    public int getLoad() {
        return load;
    }

    public int getNumber() {
        return number;
    }

    public int compareTo(Machine arg0) {
        return -(this.load - arg0.getLoad());
    }
}
