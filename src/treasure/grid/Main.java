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
        
        for (int[] row : grid) {
            Arrays.stream(row).forEach(System.out::print);
        }
        System.out.println("");
        
        userChoice();
        
    }
    
    static void placeTreasure() {
        treasure[0] = (int) (Math.random() * 10);
        treasure[1] = (int) (Math.random() * 10);
        Arrays.stream(treasure).forEach(System.out::println);
        
        for (int[] row : grid) {
            Arrays.setAll(row, x -> 0);
        }
    }

    private static void userChoice() {
        Scanner in = new Scanner(System.in);
        System.out.print("Ingrese x: ");
        int x = in.nextInt();
        in.nextLine();
        System.out.print("Ingrese y: ");
        int y = in.nextInt();
        in.nextLine();
        
        if(isThereTreasure(x,y)){
            System.out.println("Ganaste");
        }else{
            System.out.println("no ganaste");
        }
    }

    private static boolean isThereTreasure(int x, int y) {
        return (treasure[0] == x) && (treasure[1] == y);
    }
    
    
    
    
    
}
