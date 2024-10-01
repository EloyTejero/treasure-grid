package main.treasure.grid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import main.view.Ventana;

public class Client {
    static int[][] grid; // La cuadrícula del juego
    private PrintWriter out;
    private BufferedReader in;
    private Ventana view = new Ventana(this);
    private volatile boolean explosion = false;
    private int score = 0;
    
    public static void main(String[] args){
        new Client().start();
    }
    
    public void start(){
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
                if(explosion){
                    System.out.println("bombini");
                    view.setVisible(false);
                    view.dispose();
                    //game();
                    out.println(new MessageManipulator("bomb").getOutputInProtocol(MessageLevel.DISCONNECTION));
                    JOptionPane.showMessageDialog(null, "Tocaste una bomba perdiste, vuelva a conectarse");
                    break;
                }
            }
                
        } catch (IOException e) {
            
        }
        finally{
            try {
                out.close();
                in.close();
                System.out.println("finally ejecutado");
                view.setVisible(false);
                view.dispose();
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void game() {
        //grid = new int[gridSize][gridSize];
        System.out.println("Spawn ventana");
        view = new Ventana(this);
    }
    
    public void selectCellServer(int x, int y){
        String [] position = {String.valueOf(x),String.valueOf(y)};
        MessageManipulator protocolMessage = new MessageManipulator(String.join(",", position));
        String response = protocolMessage.getOutputInProtocol(MessageLevel.EVALUATE);
        out.println(response);
        out.flush();
        System.out.println("Coordenada enviada");
    }
    
    private void receiveMessage(String msg){
        System.out.println("Recibiendoo.....");
        MessageManipulator mm = new MessageManipulator(msg);
        MessageLevel messageType = mm.getMessageLevel();
        switch (messageType) {
            case PAINT:
                paintView(mm);
                break;
            case WIN:
                score = Integer.parseInt(mm.getMessageInfo());
                view.updateScore(score);
                JOptionPane.showMessageDialog(null, "WIN");
                break;
            case RESET:
                reset();
                break;
            case WAIT:
                JOptionPane.showMessageDialog(null, mm.getMessageInfo());
                break;
            case ERROR:
                JOptionPane.showMessageDialog(null, mm.getMessageInfo());
                break;
            case CONNECTION:
                JOptionPane.showMessageDialog(null, mm.getMessageInfo());
                break;
            case DISCONNECTION:
                System.out.println("bombaaaa");
                explosion = true;
                break;
            default:
                throw new AssertionError();
        }
    }
    
    private void paintView(MessageManipulator message) {
        String info [] = message.getMessageInfo().split(",");
        int x = Integer.valueOf(info[0]);
        int y = Integer.valueOf(info[1]);
        String painting = info[2];
        String color = info[3];
        view.pintarCell(x, y, painting, color);
        System.out.println("x: "+x+" y: "+y);
    }

    private void reset() {
        view.reset();
    }
}
