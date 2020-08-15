package Entity.Items;

import Entity.Animation;
import Entity.Worm;
import PanelGraphics.TileMap;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/**
 *
 * @author Balla
 */
public class Apple extends Item {
    
    private BufferedImage[] sprites;
    
    public Apple(TileMap tm, int id) {
        super(tm, id);
        setItemType(Item.APPLE_TYPE);
        
        width = 18;
        height = 18;
        cwidth = 13;
        cheight = 15;
        
        moveSpeed = 0.45;
        maxSpeed = 2.5;
        stopSpeed = 0.12;
        fallSpeed = 1.0;
        maxFallSpeed = 8.5;
        
        facingRight = true;
        
        try {
            BufferedImage spritesheet = ImageIO.read(
                    getClass().getResourceAsStream("/Sprites/apple.png")
            );
            setInventoryIcon(spritesheet);
            sprites = new BufferedImage[1];
            sprites[0] = spritesheet;
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        animation = new Animation();
        animation.setFrames(sprites);
        animation.setDelay(400);
    }
    
    private void getNextPosition() {
        
        if(falling) {
            dy +=fallSpeed;
            
            if (dy > maxFallSpeed) dy = maxFallSpeed;
        }
        
        if(left){           // if(left && !bigJumpTimerIsOn
            dx -= moveSpeed;
            if(dx < -maxSpeed){
                dx = -maxSpeed;
            }
        } else if(right){                       //} if(right && !bigJumpTimerIsOn)
            dx += moveSpeed;
            if(dx > maxSpeed) {
                dx = maxSpeed;
            }
        } else {
            if(dx > 0){
                dx -= stopSpeed;
                if(dx < 0) {
                    dx = 0;
                }
            } else if(dx < 0){
                dx += stopSpeed;
                if(dx > 0) {
                    dx = 0;
                }
            }
        }
    }
    
    @Override
    public Rectangle getRectangle() {
        return new Rectangle(
                (int)x - cwidth / 2,
                (int)y - cheight / 2,
                cwidth,
                cheight);
    }
    
    @Override
    public void useItem(Worm worm) {
        //move Item
        if(worm.getAction()) { 
            if(worm.getHealth() < worm.getMaxHealth()) {
                worm.addHealth(1);
                setDeleted(true);
            } 
        }
    }
    
    public void update() {
        //checkTileMapCollision(Tile.WATER);
        getNextPosition();
        checkFalling();
        
        setPosition(xtemp, ytemp);        
    }
    
    public void draw(Graphics2D g) {        
        setMapPosition();        
        super.draw(g);
    }   
}
