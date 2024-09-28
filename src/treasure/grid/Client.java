package treasure.grid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import javax.swing.JOptionPane;
import view.Ventana;

public class Client {
    static int[][] grid; // La cuadrícula del juego
    static Scanner scanner = new Scanner(System.in);
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
                }
            }).start();
            
            //String message;
            //while(true){
                /*
                message=scanner.nextLine();
                if(message!=null){
                    System.out.println(message);
                    out.println(message);
                    out.flush();
                }*/
            while(true){
                if(win){
                    view.setVisible(false);
                    view.dispose();
                    game();
                }
            }
                
            //}
        } catch (IOException e) {
        }
        finally{
            System.out.println("finally ejecutado");
            scanner.close();
            view.setVisible(false);
            view.dispose();
        }
    }
    
    public static void game() {
        //grid = new int[gridSize][gridSize];
        System.out.println("Spawn ventana");
        view = new Ventana();
                
        /*mostrarGrid();
        while(true){
            userChoice();
        }*/
    }
    
    private static void mostrarGrid(){
        System.out.println("");
        for (int[] row : grid) {
            //Arrays.stream(row).forEach(System.out::print);
            for(int num:row){
                System.out.print(num);
            }
            System.out.println("");
        }
        System.out.println("");
    }
    public static void userChoice(int x, int y) {
        /*
        System.out.print("Ingrese x: ");
        int x = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Ingrese y: ");
        int y = scanner.nextInt();
        */
        
        //TODO: verificar que las coordenadas esten dentro de la grilla de juego
        
        //Al ser por consola hay que invertir el eje Y, ya que tecnicamente la primera fila desde abajo
        //es el indice maximo, si la grilla es de 10 va a ser 9. Pero para que sea mas facil para el usuario
        //que cuando escriba 0 se refiera a la ultima linea.
        //y = grid.length - y; 
        scanner.nextLine();
        
        //enviar al server el punto elegido
        selectCellServer(x, y);
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
                JOptionPane.showMessageDialog(null, "WIN");
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
}
