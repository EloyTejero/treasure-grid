package view;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Ventana extends JFrame{
    
    public Ventana(){
        setSize(500,400);
        setMinimumSize(new Dimension(500,400));
        //setLayout(null);
        //setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(10, 10)); // Cuadrícula de 4x4
        
        ActionListener clickListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JButton source = (JButton) e.getSource();
                    System.out.println("Clic en: " + source.getText());
                }
        };
        
        // Añadir botones a la cuadrícula
        for (int i = 1; i <= 100; i++) {
            JButton button = new JButton(""+i);
            button.addActionListener(clickListener);
            panel.add(button);
        }

        // Añadir el panel al marco
        add(panel);
        
        setVisible(true);
    }
    
    public static void main(String[] args){
        new Ventana();
    }
}
