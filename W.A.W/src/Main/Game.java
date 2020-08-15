/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.awt.BorderLayout;
import javax.swing.JFrame;

/**
 *
 * @author Balla
 */
public class Game {
    
    public static void main(String[] args) {
        JFrame window = new JFrame("What A Worm Can Do");
        GamePanel gamePanel = new GamePanel();
        OptionsPanel optionsPanel = new OptionsPanel();
        
        window.add(gamePanel, BorderLayout.CENTER);
        window.add(optionsPanel, BorderLayout.SOUTH);
        //window.setContentPane(new GamePanel());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.pack();
        window.setVisible(true);
        window.setLocationRelativeTo(null);
    }
}
