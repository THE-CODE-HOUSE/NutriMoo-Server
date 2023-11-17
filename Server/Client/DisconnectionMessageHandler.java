// package main.java.com.thecodehouse.nutrimoo.Client;

public class DisconnectionMessageHandler extends Thread {
  private Bro server;
  public DisconnectionMessageHandler(Bro server)throws Exception {
    if(server == null)throw new Exception("Porta Invalida");
    this.server = server;
  }

  public void run (){
      for(;;){
        try{
          if (this.server.peek() instanceof DisconnectionMessage){
            System.out.println ("\nO servidor vai ser desligado agora;");
            System.err.println ("volte mais tarde!\n");
            System.exit(0);
          }
        }catch (Exception e){}
      }
  }
}
