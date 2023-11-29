// package main.java.com.thecodehouse.nutrimoo.Server;

public class EmRequest extends Message{
    private double weight;
    private String stage;

    public EmRequest(double value,String stage){
        this.weight = value;
        this.stage = stage;
    }

    public double getWeight(){
        return this.weight;
    }

    public String getStage(){
        return this.stage;
    }

    public String toString (){
        return(""+this.weight+""+this.stage);
    }
}
