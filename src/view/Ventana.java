package view;

import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Ventana extends JFrame{
    
    public Ventana(){
        setSize(500,500);
        setLayout(null);
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 500, 300);
        panel.setBackground(Color.red);
        add(panel);
        
        JLabel label = new JLabel("a");
        label.setBounds(50, 0, 50, 50);
        label.setBackground(Color.blue);
        panel.add(label);
    }
    
    public static void main(String[] args){
        new Ventana();
    }
}
