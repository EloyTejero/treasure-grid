package treasure.grid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    static int[][] grid; // La cuadrícula del juego
    static Scanner scanner = new Scanner(System.in);
    static PrintWriter out;
    static BufferedReader in;
    
    public static void main(String[] args){
        try (Socket socket = new Socket("localhost", 1234)) {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            new Thread(() -> {
                try {
                    System.out.println("holña");
                    String respuesta;
                    while (true) {
                        System.out.println("asdasd");
                        respuesta = in.readLine();
                        System.out.println("hola");
                        if(respuesta != null){
                            System.out.println("Recibido: "+respuesta);
                            receiveCell(respuesta);
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
                game(10);
            //}
        } catch (IOException e) {
        }
        finally{
            scanner.close();
        }
    }
    
    public static void game(int gridSize) {
        grid = new int[gridSize][gridSize];
                
        mostrarGrid();
        while(true){
            userChoice();
        }
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
    private static void userChoice() {
        System.out.print("Ingrese x: ");
        int x = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Ingrese y: ");
        int y = scanner.nextInt();
        
        //TODO: verificar que las coordenadas esten dentro de la grilla de juego
        
        //Al ser por consola hay que invertir el eje Y, ya que tecnicamente la primera fila desde abajo
        //es el indice maximo, si la grilla es de 10 va a ser 9. Pero para que sea mas facil para el usuario
        //que cuando escriba 0 se refiera a la ultima linea.
        //y = grid.length - y; 
        scanner.nextLine();
        
        //enviar al server el punto elegido
        selectCellServer(x, y);
    }
    private static void selectCellServer(int x, int y){
        //TODO: falta establecer protocolo
        String [] position = {String.valueOf(x),String.valueOf(y)};
        out.println(String.join(",", position));
        System.out.println("Coordenada enviada");
        out.flush();
    }
    
    private static void receiveCell(String cell){
        System.out.println("Recibiendoo.....");
        String[] coordenadas = cell.split(",");
        int x = Integer.valueOf(coordenadas[0]);
        int y = Integer.valueOf(coordenadas[1]);
        marcarGrid(x, y);
        System.out.println("x: "+x+" y: "+y);
        mostrarGrid();
    }
    
    private static void marcarGrid(int x, int y) {
        grid[y][x] = 2;
    }
}
