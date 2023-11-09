package main.java.com.thecodehouse.nutrimoo.Server;

public class EmRequest extends Message{
    private double weight;

    public EmRequest(double value){
        this.weight = value;
    }

    public double getWeight(){
        return this.weight;
    }

    public String toString (){
        return(""+this.weight);
    }
}
