package main.java.com.thecodehouse.nutrimoo.Client;

public class EmRequest extends Message{
  private double value;

  public EmRequest( double value){
    this.value = value;
  }
  public double getValue(){
    return this.value;
  }
  public String toString(){
    return (""+this.value);
  }
}
