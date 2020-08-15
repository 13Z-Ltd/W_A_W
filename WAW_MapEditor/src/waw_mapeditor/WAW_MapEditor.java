/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package waw_mapeditor;

import javax.swing.JFrame;

/**
 *
 * @author Balla
 */
public class WAW_MapEditor {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        OptionsPanel optionsPanel = new OptionsPanel();
        optionsPanel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        optionsPanel.setVisible(true);
        optionsPanel.setLocationRelativeTo(null);        
    }    
}
