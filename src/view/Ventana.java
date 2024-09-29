package view;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import treasure.grid.Client;

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
                button.setName(j+","+i);
                button.addActionListener(clickListener);
                panel.add(button);
                grid[i][j] = button;
            }
        }

        // Añadir el panel al marco
        add(panel);
        setVisible(true);
    }
    
    public void pintarCell(int x, int y, String pintar){
        grid[y][x].setText(pintar);
    }
    
    public void reset(){
        for (int i = 0; i < grid.length; i++) {
            for(int j=0;j < grid[i].length; j++){
                grid[i][j].setText("");
            }
        }
    }
    
    /*public static void main(String[] args){
        new Ventana();
    }*/
}
