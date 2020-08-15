/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PanelGraphics;

import Main.GamePanel;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 *
 * @author Balla
 */
public class Background {
    
    private BufferedImage image;
    private ArrayList<BufferedImage> layers;
    
    private double x;
    private double y;
    private double dx;
    private double dy;
    
    private double moveScale;
    
    public Background(String s, double ms) {
        layers = new ArrayList<BufferedImage>();
        try {
            image = ImageIO.read(
                    getClass().getResourceAsStream(s)
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        layers.add(image);
        moveScale = ms;
    }
    // initilaize with multiple layers
    public Background(String[] namesOfLayers, double ms) {
        layers = new ArrayList<BufferedImage>();
        
        for (int i = 0; i < namesOfLayers.length; i++) {
            //String layerName = layers[i];
            //BufferedImage layer;
            try {
                image = ImageIO.read(
                    getClass().getResourceAsStream(namesOfLayers[i])
            );
            } catch (Exception e) {
                e.printStackTrace();
            }
            layers.add(image);
        }
        moveScale = ms;
    }
    
    public void setPosition(double x, double y) {
        this.x = (x * moveScale) % GamePanel.WIDTH;
    }
    
    public void setVector(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }
    
    
    public void update() {
        x += dx;
        y += dy;
    }
    
    public void draw(Graphics2D g) {
        g.drawImage(layers.get(0), 0, 0, null);
        //g.drawImage(layers.get(layers.size() - 1), (int) 0, (int) y, null);
        //for (int i = layers.size() - 1; i >= 0; i--) {
        for (int i = 1; i < layers.size(); i++) {
            /*if(layers.size() > 1 && i == 0){
                g.drawImage(layers.get(i), (int) x, (int) y-60, null);
                if(x < 0)
                    g.drawImage(layers.get(i), (int) x + GamePanel.WIDTH, (int) y-60, null);
            } else {
            */
                g.drawImage(layers.get(i), (int) x , (int) y, null);
                if(x < 0)
                    g.drawImage(layers.get(i), (int) x + GamePanel.WIDTH, (int) y, null);
            //}
        }
        
        /*
        g.drawImage(image, (int) x, (int) y, null);
        if(x < 0){
            g.drawImage(image, (int) x + GamePanel.WIDTH, (int) y, null);
        }
        */
    }
    /*
    public void drawMultipleLayers(Graphics2D g) {
        for (int i = layers.size() - 1; i >= 0; i--) {
            if(i == 0) 
                g.drawImage(layers.get(i), (int) x, (int) y-60, null);
            else
                g.drawImage(layers.get(i), (int) x, (int) y, null);
        }
    }
    */
}
