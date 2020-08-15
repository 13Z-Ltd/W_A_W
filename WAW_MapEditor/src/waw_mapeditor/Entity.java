package waw_mapeditor;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 *
 * @author Balla
 */
public class Entity {
    public int id;
    public int x;
    public int y;
    public int width;
    public int height;
    
    public BufferedImage image;
    
    public Entity(int id, int width, int height, BufferedImage image){
        this.id = id;
        this.width = width;
        this.height = height;
        this.image = image;
    }
    
    public Entity(Entity original){
        this.id = original.id;
        this.width = original.width;
        this.height = original.height;
        this.image = original.image;
    }
    
    public void setEntityPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public Rectangle getShowedRectangle() {
        return new Rectangle(
            (int) (x - width / 2),
            (int) (y - height / 2),
            width,      //WIDTH * SCALE,
            height     //HEIGHT * SCALE,
            );
    }
    
    public int getID() { return id; }
    public BufferedImage getImage() { return image; }
}
