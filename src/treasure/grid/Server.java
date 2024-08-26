package treasure.grid;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
    public static void main(String[] args){
        try(ServerSocket server = new ServerSocket(1234)){
            System.out.println("Server encendido");
            while(true){
                Socket client = server.accept();
                System.out.println("Cliente aceptado: "+client.getPort()+" "+client.getInetAddress().getHostAddress());
            }
        }catch(IOException e){
        
        }
    }

}
