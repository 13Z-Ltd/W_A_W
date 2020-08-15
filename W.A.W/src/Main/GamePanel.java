/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import GameStates.GameStateController;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 *
 * @author Balla
 */
public class GamePanel extends JPanel 
    implements Runnable, KeyListener {
    
    // dimensions
    public static final int WIDTH = 640;   // 1280/2 = 640  WIDTH = 640
    public static final int HEIGHT = 360;   // 720/2 = 360  HEIGHT = 360
    public static final int SCALE = 2;
    
    // game thread
    private Thread thread;
    private boolean running;
    private int FPS = 60;
    private long targetTime = 1000/FPS;
    
    //image
    private BufferedImage image;
    private Graphics2D g;
    
    // Game state Controller
    private GameStateController gsc;
    
    
    public GamePanel() {
        super();
        setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));          //(WIDTH * SCALE, HEIGHT * SCALE));
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        requestFocus();
    }
    
    public void addNotify() {
        super.addNotify();
        if(thread == null) {
            thread = new Thread(this);
            addKeyListener(this);
            thread.start();
        }
    }
        
    public void run(){
        init();        
        
        long start;
        long elapsed;
        long wait;
        
        // game loop
        while (running) {
            start = System.nanoTime();
            
            update();
            draw();
            drawToScreen();
            
            elapsed = System.nanoTime() - start;
            
            wait = targetTime - elapsed / 1000000;
            if(wait < 0) wait = 5;
            
            try {
                Thread.sleep(wait);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    private void init() {        
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        g = (Graphics2D) image.getGraphics();
        
        gsc = new GameStateController();
        
        running = true;
    }
    
    private void update() {
        gsc.update();
    }
    
    private void draw() {
        gsc.draw(g);
    }
    
    private void drawToScreen(){
        //Graphics g2 = getGraphics();
        Graphics2D g2d = (Graphics2D) getGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.drawImage(image, 0, 0,
                WIDTH * SCALE,      //WIDTH * SCALE,
                HEIGHT * SCALE,     //HEIGHT * SCALE,
                null
        );
        g2d.dispose();
    }

    @Override
    public void keyTyped(KeyEvent key) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyPressed(KeyEvent key) {
        gsc.keyPressed(key.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent key) {
        gsc.keyReleased(key.getKeyCode());
    }
    
}
