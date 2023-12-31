package message;// package main.java.com.thecodehouse.nutrimoo.Server;

import java.net.*;
import java.util.*;

public class ConnectionAcceptor extends Thread{
    private ServerSocket request;
    private ArrayList<Bro> users;

    public ConnectionAcceptor(String port, ArrayList<Bro> users) throws Exception{
        if (port==null) throw new Exception ("Porta ausente");
        try{
            this.request = new ServerSocket(Integer.parseInt(port));
        }catch(Exception e){
            throw new Exception("Porta Invalida");
        }

        if(users == null) throw new Exception("Usuarios ausentes");

        this.users = users;
    }

    public void run ()
    {
        for(;;){
            Socket connection=null;
            try{
                connection = this.request.accept();
            }catch (Exception e){
                continue;
            }

            ConnectionSupervisor connectionSupervisor=null;
            // Aqui ele tenta iniciar a thread que irá supervisionar as solicitações do usuário
            try{
                connectionSupervisor = new ConnectionSupervisor(connection, users);
            }catch(Exception error){}
            connectionSupervisor.start();
        }
    }


}
