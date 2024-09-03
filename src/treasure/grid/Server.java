package treasure.grid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;



public class Server {
    
    private ServerSocket serverSocket;
    
    public void start(int port){
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server encendido");
            while(true){
                Socket clientSocket = serverSocket.accept();
                new clientManager(clientSocket).start();
            }
        } catch (IOException ex) {
            System.out.println("Error en runtime server");
        } finally{
            stop();
        }
    }
    
    public void stop(){
        try {
            serverSocket.close();
        } catch (IOException ex) {
            System.out.println("Error al cerrar el server");
        }
    }
    
    private static class clientManager extends Thread{
        
        private final Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        
        public clientManager(Socket clientSocket) {
            socket = clientSocket; 
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream());
                
                out.println("Bienvenido al Server");
                
                String message;
                while((message = in.readLine()) != null){
                    System.out.println("Recibido: "+message);
                    out.print("Recibido: "+message);
                }
            } catch (IOException ex) {
                System.out.println("Error trasmision de mensajes");
            }
        }
    }
    
    public static void main(String[] args){
        new Server().start(1234);
    }

    

}
