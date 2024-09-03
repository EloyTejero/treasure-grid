package treasure.grid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;



public class Server {
    
    private ServerSocket serverSocket;
    private static final List<clientManager> clientes = new ArrayList<>();
    
    public void start(int port){
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server encendido");
            while(true){
                Socket clientSocket = serverSocket.accept();
                clientManager client = new clientManager(clientSocket);
                client.start();
                clientes.add(client);
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
    
    public synchronized void sendAll(String message){
        for(clientManager c:clientes){
            c.sendMessage(message);
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
                
                String message;
                while(true){
                    message = in.readLine();
                    if(message!= null){
                        System.out.println("Recibido: "+message);
                        sendMessage(message);
                        System.out.println("Enviado: "+message);
                    }
                }
            } catch (IOException ex) {
                System.out.println("Error trasmision de mensajes");
            }
        }
        
        private void sendMessage(String message){
            out.println(message);
        }
    }
    
    public static void main(String[] args){
        new Server().start(1234);
    }

    

}
