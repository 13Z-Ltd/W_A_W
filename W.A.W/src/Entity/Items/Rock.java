package Entity.Items;

import Entity.Animation;
import Entity.Worm;
import PanelGraphics.Tile;
import PanelGraphics.TileMap;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/**
 *
 * @author Balla
 */
public class Rock extends Item{
    // animations
    private BufferedImage[] sprites;
    
    public Rock(TileMap tm, int id) {
        super(tm, id);
        setItemType(Item.ROCK_TYPE);
        
        width = 30;
        height = 30;
        cwidth = 28;
        cheight = 28;
        
        moveSpeed = 0.45;
        maxSpeed = 2.5;
        stopSpeed = 0.12;
        fallSpeed = 1.0;
        maxFallSpeed = 8.0;
        
        facingRight = true;
        
        try {
            BufferedImage spritesheet = ImageIO.read(
                    getClass().getResourceAsStream("/Sprites/rock.png")
            );
            setInventoryIcon(ImageIO.read(
                    getClass().getResourceAsStream("/Sprites/rock.png")
            ));
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
    
    @Override
    public Rectangle getRectangle() {
        return new Rectangle(
                (int)x - cwidth / 2,
                (int)y - cheight / 2 - 2,
                cwidth,
                cheight + 2);
    }
    
    @Override
    public void whenCollisionWithPlayer(Worm worm) {
        Rectangle rect = this.getRectangle();
        if(worm.getx()  > rect.x - 4  && worm.getx() - 4 < (rect.x + rect.width) + 4){
            worm.setDy(0);
            worm.setFalling(false);
        }
        if(worm.gety() > rect.y){
            if(worm.getFacingRight() && worm.getx() < rect.x )
                worm.setRightMoveBlocked(true);
            if(!worm.getFacingRight() && worm.getx() > rect.x + rect.width  && worm.gety() > rect.y)
                worm.setLeftMoveBlocked(true);
        }
    }
    
    @Override
    public void useItem(Worm worm) {
        if(worm.getAction()) {
            Rectangle rect = this.getRectangle();
            //if(worm.getx() > rect.x  && worm.getx() < (rect.x + rect.width)){
            if(worm.getx() < rect.x  && worm.gety() > rect.y) {                 //worm.getFacingRight() &&
                worm.setPosition(rect.x - worm.getCWidth() / 2 + 2, worm.gety());
                if(worm.getRight() && !topRight) {
                    x += 1;
                }
                if(worm.getLeft() && !worm.getTopLeft()) {
                    x -= 1;
                }
            }         
            if(worm.getx() > rect.x + rect.width  && worm.gety() > rect.y){
                worm.setPosition(rect.x + rect.width + worm.getCWidth() / 2 - 2, worm.gety());
                if(worm.getRight() && !worm.getTopRight()) {
                    x += 1;
                }
                if(worm.getLeft() && !topLeft) {
                    x -= 1;
                }
            }
        }else { //} else if (shift) {
            if(worm.getMoveWithItem() != 0){
                if(worm.getMoveWithItem() > 0) {
                    worm.setMoveWithItem(worm.getMoveWithItem() - stopSpeed);
                    if(worm.getMoveWithItem() < 0)
                        worm.setMoveWithItem(0);
                } else {
                    worm.setMoveWithItem(worm.getMoveWithItem() + stopSpeed);
                    if(worm.getMoveWithItem() > 0) 
                        worm.setMoveWithItem(0);
                }
                worm.setDx(worm.getMoveWithItem());
            }
        }
    }
    
    public void update() {
        getNextPosition();
        checkTileMapCollision(Tile.BLOCKED);
        setPosition(xtemp, ytemp);
    }
    
    public void draw(Graphics2D g) {
        setMapPosition();        
        
        super.draw(g);        
    }
    
}
