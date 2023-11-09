package main.java.com.thecodehouse.nutrimoo.Client;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.Semaphore;

public class Bro {
  private Socket connection;
  private ObjectInputStream receptor;
  private ObjectOutputStream transmissor;
  private Message NextMessage = null;
  private Semaphore mutEx = new Semaphore(1,true);
  public Bro(Socket connection, ObjectInputStream receptor, ObjectOutputStream transmissor) throws Exception {
    if(connection ==null) throw new Exception("Conexao ausente");
    if(receptor ==null) throw new Exception("Receptor ausente");
    if(transmissor == null) throw new Exception("Transmissor ausente");
    this.connection = connection;
    this.transmissor = transmissor;
    this.receptor = receptor;
  }


  public void send(Message x) throws Exception {
    try{
      this.transmissor.writeObject(x);
      this.transmissor.flush();
    }catch(IOException e){
      throw new Exception("Erro de transmissao");
    }
  }

  public Message peek() throws Exception {
    try{
      this.mutEx.acquireUninterruptibly();
      if(this.NextMessage == null) this.NextMessage = (Message) this.receptor.readObject();
      this.mutEx.release();
      return this.NextMessage;
    }catch(Exception e){
      throw new Exception("Erro de recepção");
    }
  
  }
  public Message receive()throws Exception {
    try{
      if(this.NextMessage == null)this.NextMessage = (Message)this.receptor.readObject();
      Message ret = this.NextMessage;
      this.NextMessage = null;
      return ret;
    }catch(Exception e){
      throw new Exception("Erro de recepcao");
    }
  }
  public void goodbye() throws Exception{
    try{
      this.transmissor.close();
      this.receptor   .close();
      this.connection .close();
    }catch (Exception e){
        throw new Exception ("Erro de desconexao");
      }
  }


}
