package Entity.Enemies;

import Entity.Animation;
import PanelGraphics.Tile;
import PanelGraphics.TileMap;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/**
 *
 * @author Balla
 */
public class Hedgehog extends Enemy {
    private BufferedImage[] sprites;

    public Hedgehog(TileMap tm) {
        super(tm);
        
        moveSpeed = 0.2;
        maxSpeed = 1.4;
        fallSpeed = 0.2;
        maxFallSpeed = 10.0;
        
        width = 30;
        height = 30;
        cwidth = 25;
        cheight = 23;
        
        health = maxHealth = 2;
        damage = 1;
        
        // load Sprites
        try {
            BufferedImage spritesheet = ImageIO.read(
                    getClass().getResourceAsStream("/Sprites/hedgehog.png")
            );
            
            sprites = new BufferedImage[1];
            sprites[0] = spritesheet;
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        animation = new Animation();
        animation.setFrames(sprites);
        animation.setDelay(300);
        
        right = true;
        facingRight = true;
    }
    
    private void getNextPosition() {
        
        if(left) {
            dx -= moveSpeed;
            if(dx < -maxSpeed) {
                dx = -maxSpeed;
            }
        } else if(right) {
            dx += moveSpeed;
            if(dx > maxSpeed) {
                dx = maxSpeed;
            }
        }
        
        if(falling) {
            dy += fallSpeed;
        }
    }
    
    public void update() {
        // update position
        getNextPosition();
        checkTileMapCollision(Tile.BLOCKED);
        setPosition(xtemp, ytemp);
        
        /*
        // check flinching
        if(flinching) {
            long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
            if (elapsed > 400) {
                flinching = false;
            }
        }
        */
        
        //if it hits a wall, go other direction
        if(right && dx == 0) {
            right = false;
            left = true;
            facingRight = false;
        }
        else if (left && dx == 0) {
            right = true;
            left = false;
            facingRight = true;
        }
        
        // update animation
        animation.update();
    }

    public void draw(Graphics2D g) {
        setMapPosition();
        
        super.draw(g);
    }
}
