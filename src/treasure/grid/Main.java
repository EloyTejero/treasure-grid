package treasure.grid;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    
    static int[][] grid; // La cuadrícula del juego
    static int[] treasure = new int[2];
    
    public static void main(String[] args) {
        game(10);
    }
    
    public static void game(int gridSize) {
        grid = new int[gridSize][gridSize];
        
        placeTreasure();
        
        while(true){
            mostrarGrid();
            userChoice();
        }
    }
    
    static void mostrarGrid(){
        for (int[] row : grid) {
            //Arrays.stream(row).forEach(System.out::print);
            for(int num:row){
                System.out.print(num);
            }
            System.out.println("");
        }
        System.out.println("");
    }
    
    static void placeTreasure() {
        int x = (int) (Math.random() * 10);
        int y = (int) (Math.random() * 10);
        treasure[0] = x;
        treasure[1] = y;
        Arrays.stream(treasure).forEach(System.out::println);
        
        for (int[] row : grid) {
            Arrays.setAll(row, i -> 0);
        }
        grid[y][x] = 1;
    }

    private static void userChoice() {
        Scanner in = new Scanner(System.in);
        System.out.print("Ingrese x: ");
        int x = in.nextInt();
        in.nextLine();
        System.out.print("Ingrese y: ");
        int y = in.nextInt();
        in.nextLine();
        
        String respuesta = evaluarJuego(x, y);
        System.out.println(respuesta);
    }

    private static boolean isThereTreasure(int x, int y) {
        return (treasure[0] == x) && (treasure[1] == y);
    }

    private static String evaluarJuego(int x, int y) {
        marcarGrid(x, y);
        if(isThereTreasure(x,y)){
            ganar();
            return "ganaste";
        }
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
        return direccionTreasure;
    }

    private static void marcarGrid(int x, int y) {
        grid[y][x] = 2;
    }

    private static void ganar() {
        placeTreasure();
    }
    
}
