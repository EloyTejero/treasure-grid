/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.treasure.grid;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;

/**
 *
 * @author Eloy
 */
public class ImagePanelTest extends JPanel {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Eliminar Ícono de un Botón");
        JButton button = new JButton();

        // Cargar el ícono usando el ClassLoader
        ImageIcon ii = new ImageIcon(ImagePanelTest.class.getClassLoader().getResource("icons/right-arrow.png"));
        System.out.println(ii);
        button.setIcon(ii);
        button.setPreferredSize(new Dimension(100, 100));
        Border border = BorderFactory.createLineBorder(Color.BLACK, 3); // Borde rojo de 3 píxeles
        button.setBorder(border);
        button.setBackground(Color.WHITE);

        // Agregar un ActionListener para eliminar el ícono al hacer clic
        button.addActionListener(e -> {
            //button.setIcon(null); // Eliminar el ícono
            
            button.setBackground(Color.red);
        });

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());
        frame.add(button);
        frame.setSize(300, 300);
        frame.setVisible(true);
    }
}
