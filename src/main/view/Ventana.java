package main.view;

import com.formdev.flatlaf.FlatLightLaf;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import main.treasure.grid.Client;

public class Ventana extends JFrame{
    
    Client client;
    JButton [][] grid = new JButton[10][10];
    private final JLabel scoreLabel;
    
    public Ventana(Client client){
        //FlatLightLaf.setup();

        this.client = client;
        
        setSize(500,400);
        setMinimumSize(new Dimension(500,400));
        //setLayout(null);
        //setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Panel principal con BorderLayout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        
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
                    client.selectCellServer(x, y);
                }
        };
        
        // Añadir botones a la cuadrícula
        for (int i = 0; i < grid.length; i++) {
            for(int j=0;j < grid[i].length; j++){
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(30, 30));
                button.setName(j+","+i);
                button.setBackground(Color.WHITE);
                //Border border = BorderFactory.createLineBorder(Color.BLACK, 1); // Borde negro de 1 píxeles
                //button.setBorder(border);
                
                button.addActionListener(clickListener);
                button.setOpaque(true);
                panel.add(button);
                grid[i][j] = button;
            }
        }
        // Etiqueta de puntaje
        scoreLabel = new JLabel("Score: 0", JLabel.CENTER); // Etiqueta centrada
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));

        // Añadir la cuadrícula al centro y el puntaje abajo
        mainPanel.add(panel, BorderLayout.CENTER); // Cuadrícula en el centro
        mainPanel.add(scoreLabel, BorderLayout.SOUTH); // Etiqueta de puntaje abajo

        // Añadir el panel principal al marco
        add(mainPanel);
        setVisible(true);
//        // Añadir el panel al marco
//        add(panel);
//        setVisible(true);
    }
    
    public void pintarCell(int x, int y, String direction, String color){
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
        
        Color color_ = Color.white;
        switch (color){
            case "r" -> color_ = Color.red;
            case "g" -> color_ = Color.GREEN;
        }
        
        URL iconUrl = getClass().getResource(imagePath);
        ImageIcon icon = new ImageIcon(iconUrl);
        
        Image img = icon.getImage();
        Image newImg = img.getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        icon = new ImageIcon(newImg);

        grid[y][x].setIcon(icon);
        grid[y][x].setBackground(color_);
        grid[y][x].setEnabled(false);
    }
    
    public void reset(){
        for (int i = 0; i < grid.length; i++) {
            for(int j=0;j < grid[i].length; j++){
                grid[i][j].setIcon(null);
                grid[i][j].setBackground(Color.WHITE);
                grid[i][j].setEnabled(true);
            }
        }
    }
    
    public void updateScore(int score) {
        scoreLabel.setText("Score: " + score);
    }
    
    /*
    public static void main(String args[]){
        new Ventana();
    }
    */
}
