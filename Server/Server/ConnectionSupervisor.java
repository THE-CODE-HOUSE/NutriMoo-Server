// package main.java.com.thecodehouse.nutrimoo.Server;

import java.io.*;
import java.net.*;
import java.util.*;

public class ConnectionSupervisor extends Thread{
    private double CMS=0;
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
        
        ObjectOutputStream transmissor = null;

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
                if(message==null) return;
                else if (message instanceof EmRequest){
                    EmRequest emRequest = (EmRequest) message;
                    Double weight = emRequest.getWeight();
                    switch (emRequest.getStage()){
                        case "VACA":
                        this.CMS = weight*0.025;
                        break;
                        
                        case "VACA EM LACTACAO":
                        this.CMS = weight*0.035;
                        break;

                        case "BEZERRA/NOVILHA":
                        this.CMS = weight*0.02;
                        break;

                        default:
                        this.CMS = weight*0.03;
                        break;
                    }
                }
                else if (message instanceof EmResponse){
                    this.user.send(new EmResponse(this.CMS));
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
