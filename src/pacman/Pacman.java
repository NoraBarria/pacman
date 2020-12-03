package pacman;

import java.awt.EventQueue;
import javax.swing.JFrame;

public class Pacman {

    private static void Comenzar() {
        JFrame jframe = new JFrame();
        jframe.add(new Controlador());
        jframe.setTitle("Trabajo Practico");
        jframe.setDefaultCloseOperation(jframe.EXIT_ON_CLOSE);
        jframe.setSize(380, 420);
        jframe.setLocationRelativeTo(null);
        jframe.setVisible(true);        
    }

    public static void main(String[] args) {
        Comenzar();
    }
}