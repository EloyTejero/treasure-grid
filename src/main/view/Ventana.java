package main.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import main.treasure.grid.Client;

public class Ventana extends JFrame{
    
    JButton [][] grid = new JButton[10][10];
    
    public Ventana(){
        setSize(500,400);
        setMinimumSize(new Dimension(500,400));
        //setLayout(null);
        //setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(10, 10)); // Cuadrícula de 10x10
        
        ActionListener clickListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JButton source = (JButton) e.getSource();
                    String coordenada = source.getName();
                    
                    System.out.println("Clic en: " + coordenada);
                    String[] coordenadas = coordenada.split(",");
                    int x = Integer.valueOf(coordenadas[0]);
                    int y = Integer.valueOf(coordenadas[1]);
                    Client.selectCellServer(x, y);
                }
        };
        
        // Añadir botones a la cuadrícula
        for (int i = 0; i < grid.length; i++) {
            for(int j=0;j < grid[i].length; j++){
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(30, 30));
                button.setName(j+","+i);
                button.setBackground(Color.WHITE);
                Border border = BorderFactory.createLineBorder(Color.BLACK, 1); // Borde rojo de 3 píxeles
                button.setBorder(border);
                
                button.addActionListener(clickListener);
                
                panel.add(button);
                grid[i][j] = button;
            }
        }

        // Añadir el panel al marco
        add(panel);
        setVisible(true);
    }
    
    public void pintarCell(int x, int y, String direction){
        String imagePath = "";
        switch (direction) {
            case "U" -> imagePath = "/icons/up-arrow.png";
            case "D" -> imagePath = "/icons/down-arrow.png";
            case "L" -> imagePath = "/icons/left-arrow.png";
            case "R" -> imagePath = "/icons/right-arrow.png";
            case "UR" -> imagePath = "/icons/up-right-arrow.png";
            case "UL" -> imagePath = "/icons/up-left-arrow.png";
            case "DR" -> imagePath = "/icons/down-right-arrow.png";
            case "DL" -> imagePath = "/icons/down-left-arrow.png";
            case "T" -> imagePath = "/icons/treasure-chest.png";
        }
        
        URL iconUrl = getClass().getResource(imagePath);
        ImageIcon icon = new ImageIcon(iconUrl);
        
        Image img = icon.getImage();
        Image newImg = img.getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        icon = new ImageIcon(newImg);
        
        grid[y][x].setIcon(icon);
    }
    
    public void reset(){
        for (int i = 0; i < grid.length; i++) {
            for(int j=0;j < grid[i].length; j++){
                grid[i][j].setIcon(null);
            }
        }
    }
    /*
    public static void main(String args[]){
        new Ventana();
    }
    */
}
