package main.treasure.grid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JOptionPane;
import main.view.Ventana;

public class Client {
    static int[][] grid; // La cuadrícula del juego
    static PrintWriter out;
    static BufferedReader in;
    static Ventana view = new Ventana();
    static boolean win = false;
    
    public static void main(String[] args){
        try (Socket socket = new Socket("localhost", 1234)) {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            new Thread(() -> {
                try {
                    String message;
                    while (true) {
                        message = in.readLine();
                        if(message != null){
                            System.out.println("Recibido: "+message);
                            receiveMessage(message);
                        }
                    }
                } catch (IOException e) {
                    System.out.println(e); //TODO: Mandar al client devuelta a login
                }
            }).start();
            
            while(true){
                if(win){
                    view.setVisible(false);
                    view.dispose();
                    game();
                }
            }
                
        } catch (IOException e) {
            
        }
        finally{
            System.out.println("finally ejecutado");
            view.setVisible(false);
            view.dispose();
        }
    }
    
    public static void game() {
        //grid = new int[gridSize][gridSize];
        System.out.println("Spawn ventana");
        view = new Ventana();
    }
    
    public static void selectCellServer(int x, int y){
        String [] position = {String.valueOf(x),String.valueOf(y)};
        MessageManipulator protocolMessage = new MessageManipulator(String.join(",", position));
        String response = protocolMessage.getOutputInProtocol(MessageLevel.EVALUATE);
        out.println(response);
        out.flush();
        System.out.println("Coordenada enviada");
    }
    
    private static void receiveMessage(String msg){
        System.out.println("Recibiendoo.....");
        MessageManipulator mm = new MessageManipulator(msg);
        MessageLevel messageType = mm.getMessageLevel();
        switch (messageType) {
            case PAINT:
                paintView(mm);
                break;
            case WIN:
                JOptionPane.showMessageDialog(null, mm.getMessageInfo());
                break;
            case RESET:
                reset();
                break;
            case WAIT:
                JOptionPane.showConfirmDialog(null, mm.getMessageInfo());
                break;
            case ERROR:
                JOptionPane.showConfirmDialog(null, mm.getMessageInfo());
                break;
            case CONNECTION:
                JOptionPane.showConfirmDialog(null, mm.getMessageInfo());
                break;
            default:
                throw new AssertionError();
        }
    }
    
    
    
    private static void paintView(MessageManipulator message) {
        String info [] = message.getMessageInfo().split(",");
        int x = Integer.valueOf(info[0]);
        int y = Integer.valueOf(info[1]);
        String painting = info[2];
        view.pintarCell(x, y, painting);
        System.out.println("x: "+x+" y: "+y);
    }

    private static void reset() {
        view.reset();
    }
}
