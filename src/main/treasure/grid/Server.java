package main.treasure.grid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;



public class Server {
    
    private ServerSocket serverSocket;
    private static final List<clientManager> clientes = new CopyOnWriteArrayList<>();
    private static final int [] treasure = new int[2];
    private static boolean gameStarted = false;
    private static List<String> grid = new ArrayList<>(); // La cuadrícula del juego
    
    public void start(int port){
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server encendido");
            placeTreasure();
            while(true){
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente aceptado");
                clientManager client = new clientManager(clientSocket);
                client.start();
                clientes.add(client);
                System.out.println("Numero clientes: "+clientes.size());
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
    
    public static synchronized void sendAll(String message){
        for(clientManager c:clientes){
            try{
                c.sendMessage(message);
            } catch(NullPointerException e){
                System.out.println("usuario null");
                clientes.remove(c);
            }
        }
    }
    
    public static synchronized void sendAllExcept(clientManager client, String message){
        for(clientManager c:clientes){
            if(c!=client){
                c.sendMessage(message);
            }
        }
    }
    
    public static synchronized void disconnect(clientManager client){
        clientes.remove(client);
        
    }
    
    public static synchronized void checkGame(){
        if(clientes.size()<2){
            if(gameStarted){
                gameStarted = false;
                sendAll(new MessageManipulator("Contrincante desconectado, reseteando a la espera").getOutputInProtocol(MessageLevel.ERROR));
                reset();
            }
            return;
        }
        
        if(!gameStarted){ //si no esta empezado lo arranca
            startGame(); //pero si entra alguien y la partida esta empezada no le va a salir el cartel de start
        }
    }
    
    public static synchronized void startGame(){
        gameStarted = true;
        sendAll(new MessageManipulator("start").getOutputInProtocol(MessageLevel.CONNECTION));
    }
    
    static void placeTreasure() {
        int x = (int) (Math.random() * 10);
        int y = (int) (Math.random() * 10);
        treasure[0] = x;
        treasure[1] = y;
        System.out.println("Tesoro: "+x+","+y);
    }
    
    private static synchronized boolean isThereTreasure(int x, int y) {
        return (treasure[0] == x) && (treasure[1] == y);
    }
    
    public static synchronized void reset(){
        grid.clear();
        placeTreasure();
        MessageManipulator message = new MessageManipulator("1");
        String protocolMessage = message.getOutputInProtocol(MessageLevel.RESET);
        sendAll(protocolMessage);
    }
    
    private static class clientManager extends Thread{
        
        private final Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        int score = 0;
        
        public clientManager(Socket clientSocket) {
            socket = clientSocket; 
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream());
                System.out.println("Generado IO: "+out.toString()+" "+in.toString());
                
                checkGame();
                
                if(gameStarted){
                    for(String m:grid){
                        sendMessage(m);
                    }
                }
                
                String message;
                while(true){
                    
                    if((message = in.readLine())!= null){
                        System.out.println("Recibido: "+message);
                        receiveMessage(message);
                    }
                }
            } catch (IOException ex) {
                System.out.println("Error trasmision de mensajes");
                System.out.println(clientes.size());
            } finally{
                disconnect(this);
                //out.close();
                try {
                    in.close();
                    socket.close();
                } catch (IOException ex) {
                    System.out.println("Error de desconexion");
                }                
                checkGame();
                System.out.println(clientes.size());
            }
        }
        
        private void sendMessage(String message){
            System.out.println("Enviandooo... a client: "+this.toString());
            System.out.println("OUT: "+out.toString());
            out.println(message);
            out.flush();
        }
        
        private void receiveMessage(String CodedMessage){
            MessageManipulator message = new MessageManipulator(CodedMessage);
            MessageLevel messageType = message.getMessageLevel();
            switch (messageType) {
                case EVALUATE -> evaluateCell(message);
                default -> throw new AssertionError();
            }
        }

        private void evaluateCell(MessageManipulator message) {
            MessageManipulator response;
            
            if(!gameStarted){
                response = new MessageManipulator("El juego no comenzo");
                sendMessage(response.getOutputInProtocol(MessageLevel.WAIT));
                return;
            }
            
            String [] coords = message.getMessageInfo().split(",");
            int x = Integer.valueOf(coords[0]);
            int y = Integer.valueOf(coords[1]);
            
            if(!isThereTreasure(x,y)){
                final int BIT_ARRIBA = 1;
                final int BIT_ABAJO = 1 << 1;
                final int BIT_DERECHA = 1 << 2;
                final int BIT_IZQUIERDA = 1 << 3;
                int posicionFlag = 0;

                System.out.println("no ganaste");

                if(treasure[1] < y) posicionFlag |= BIT_ARRIBA;
                if(treasure[1] > y) posicionFlag |= BIT_ABAJO;
                if(treasure[0] > x) posicionFlag |= BIT_DERECHA;
                if(treasure[0] < x) posicionFlag |= BIT_IZQUIERDA;

                String direccionTreasure = "";

                if((posicionFlag & BIT_ARRIBA) != 0){            
                    direccionTreasure+="U";            
                }
                if((posicionFlag & BIT_ABAJO) != 0){            
                    direccionTreasure+="D";            
                }
                if((posicionFlag & BIT_DERECHA) != 0){
                    direccionTreasure+="R";
                }
                if((posicionFlag & BIT_IZQUIERDA) != 0){
                    direccionTreasure+="L";
                }
                
                response = new MessageManipulator(x+","+y+","+direccionTreasure);
                
                grid.add(response.getOutputInProtocol(MessageLevel.PAINT)+",r");
                
                sendAllExcept(this, response.getOutputInProtocol(MessageLevel.PAINT)+",r");
                sendMessage(response.getOutputInProtocol(MessageLevel.PAINT)+",g");
                return;
            }
            
            score++;
            response = new MessageManipulator(x+","+y+",T");
            sendAllExcept(this, response.getOutputInProtocol(MessageLevel.PAINT)+",r");
            sendMessage(response.getOutputInProtocol(MessageLevel.PAINT)+",g");
            response = new MessageManipulator(String.valueOf(score));
            sendMessage(response.getOutputInProtocol(MessageLevel.WIN));
            response = new MessageManipulator("reset");
            sendAll(response.getOutputInProtocol(MessageLevel.RESET));
            
            reset();
        }
    }
    
    public static void main(String[] args){
        new Server().start(1234);
    }

    

}
