/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

/**
 *
 * @author Balla
 */
public class OptionsPanel extends JPanel {
    
    JButton btnNewGame;
    
    public OptionsPanel() {
        setBorder(new TitledBorder(BorderFactory.createLoweredSoftBevelBorder(), "Options"));
        setBackground(Color.WHITE);
        
        btnNewGame = new JButton("New Game");
        btnNewGame.setName("New Game");
        btnNewGame.setFocusable(false);
        add(btnNewGame);
    }
}
