package main.java.com.thecodehouse.nutrimoo.Server;

import java.io.*;
import java.net.*;
import java.util.*;

public class ConnectionSupervisor extends Thread{
    private double EM=0;
    private Bro user;
    private Socket connection;
    private ArrayList<Bro> users;

    public ConnectionSupervisor(Socket connection, ArrayList<Bro> users) throws Exception{
        if (connection==null) throw new Exception ("Conexao ausente");
        if (users==null) throw new Exception ("Usuarios ausentes");

        this.connection  = connection;
        this.users = users;    
    }

    public void run (){
        
        ObjectOutputStream transmissor;

        try{
            transmissor = new ObjectOutputStream(this.connection.getOutputStream());
        }catch(Exception e){
            return;
        }

        ObjectInputStream receptor=null;
        try{
            receptor = new ObjectInputStream(this.connection.getInputStream());
        }catch(Exception e){
            try{
                transmissor.close();
            }catch(Exception error){} // so tentando fechar antes de acabar a thread
            
            return;
        }

        try{
            this.user = new Bro(this.connection, receptor, transmissor);
        }catch(Exception error){} // sei que passei os parametros corretos

        try{
            synchronized (this.users){
                this.users.add(this.user);
            }

            for(;;){
                Message message = this.user.receive();
                Double NEm = 0.077;

                if(message==null) return;
                else if (message instanceof EmRequest){
                    EmRequest emRequest = (EmRequest) message;
                    Double weight = emRequest.getWeight();
                    Double PM = Math.pow(weight,0.75);
                    this.EM = NEm * PM;
                }
                else if (message instanceof EmResponse){
                    this.user.send(new Result (this.EM));
                }
                else if (message instanceof RequestToLeave){
                    synchronized(this.users){
                        this.users.remove(this.user);
                    }
                    this.user.goodBye();
                }
            }
        }catch (Exception erro){
            try{
                transmissor.close ();
                receptor.close ();
            }
            catch (Exception falha) {} // so tentando fechar antes de acabar a thread
            return;
        }
    }
}
