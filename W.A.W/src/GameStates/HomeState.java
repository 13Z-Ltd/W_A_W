/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameStates;

import PanelGraphics.Background;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

/**
 *
 * @author Balla
 */
public class HomeState extends GameState {
    
    private Background bg;
    
    private String[] options = {
        "Start Game", "Options", "Exit" };
    private int currentChoice = 0;
    
    private Color titleColor;
    private Font titleFont;
    private Font font;
    
    public HomeState(GameStateController gsc) {
        this.gsc = gsc;
        
        try {           
            bg = new Background("/Backgrounds/mainbg2.png", 1);
            
            titleColor = new Color(200, 200, 200);
            titleFont = new Font("Century Gothic", Font.PLAIN, 28);
            font = new Font("Arial", Font.PLAIN, 12);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update() {
        
    }

    @Override
    public void draw(Graphics2D g) {
        // draw background
        bg.draw(g);
        
        // draw the title
        g.setColor(titleColor);
        g.setFont(titleFont);
        g.drawString("What a Worm", 220, 160);
        
        g.setFont(font);
        for (int i = 0; i < options.length; i++) {
            if(i == currentChoice) {
                g.setColor(Color.GREEN);
            } else {
                g.setColor(Color.GRAY);
            }
            g.drawString(options[i], 260, i * 20 + 200);
        }
    }

    @Override
    public void keyPressed(int k) {
        if(k == KeyEvent.VK_ENTER) {
            gsc.setNotDraw(true);
            select();
            gsc.setNotDraw(false);
        }
        if(k == KeyEvent.VK_UP) {
            currentChoice--;
            if(currentChoice < 0) {
                currentChoice = options.length - 1;
            }
        }
        if(k == KeyEvent.VK_DOWN) {
            currentChoice++;
            if(currentChoice == options.length) {
                currentChoice = 0;
            }
        }
    }

    @Override
    public void keyReleased(int k) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void select() {
        if (currentChoice == 0) {
            gsc.setSate(GameStateController.FORRESTLEVEL1);
        }
        if (currentChoice == 2) {
            System.exit(0);
        }
    }
    
}
