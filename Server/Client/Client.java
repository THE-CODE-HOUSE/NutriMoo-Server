// package main.java.com.thecodehouse.nutrimoo.Client;
import java.net.*;
import java.io.*;
 
public class Client{
  public static final String DEFAULT_HOST = "localhost";

  public static final int DEFAULT_PORT = 3000;

  public static void main(String[] args){
    if(args.length>2){
      System.err.println("Uso esperado: java Cliente [HOST [PORTA]]\n");
      return;
    }

    Socket connection = null;
    try {
      String host = Client.DEFAULT_HOST;
      int    port = Client.DEFAULT_PORT;

      if(args.length>0)host = args[0];
      if(args.length == 2) port = Integer.parseInt(args[1]);

      connection = new Socket(host, port);
  }catch(Exception e){
    System.err.println("Indique o servidor e a porta corretos!\n");
    return;
  }

  ObjectOutputStream transmissor = null;
  try{
    transmissor = new ObjectOutputStream(connection.getOutputStream());
  }catch(Exception e){
    System.err.println ("Indique o servidor e a porta corretos!\n");
    return;
  }
  
  ObjectInputStream receptor = null;
  try{
    receptor = new ObjectInputStream(connection.getInputStream());
  }catch(Exception e){
    System.err.println ("Indique o servidor e a porta corretos!\n");
    return;
  }

  Bro server = null;
  try{
    server = new Bro (connection, receptor, transmissor);
  }catch (Exception e){
    System.err.println ("Indique o servidor e a porta corretos!\n");
    return;
  }

  DisconnectionMessageHandler disconnectionMessageHandler = null;
  try{
		disconnectionMessageHandler = new DisconnectionMessageHandler (server);
	}catch(Exception e){}

  disconnectionMessageHandler.start();
  
  double option = 0;

  do{

    System.out.print ("Digite o peso do gado:  ");

    
    try {
      option = Teclado.getUmDouble();
    } catch (Exception e) {
      System.err.println("Opção invalida\n");
      continue;
    }
    try{
      server.send(new EmRequest(option));
    }catch(Exception e){
      System.err.println ("Erro de comunicacao com o servidor;");
			System.err.println ("Tente novamente!");
			System.err.println ("Caso o erro persista, termine o programa");
			System.err.println ("e volte a tentar mais tarde!\n");    
    }
    
    try{

      server.send(new EmResponse());
      Message message = null;

      do{
        message = (Message)server.peek();
      }while(!(message instanceof Result));
      
      Result result = (Result)server.receive();
      System.out.println("Resultado atual: "+result.getResult()+"\n");


    }catch(Exception e){
      System.err.println ("Erro de comunicacao com o servidor;");
			System.err.println ("Tente novamente!");
			System.err.println ("Caso o erro persista, termine o programa");
			System.err.println ("e volte a tentar mais tarde!\n");    
    }

  }while (option != -1);
  try{
		server.send (new ExitRequest ());
	}catch (Exception e){}
  System.out.println ("Obrigado por usar este programa!");
	System.exit(0);
  }
}