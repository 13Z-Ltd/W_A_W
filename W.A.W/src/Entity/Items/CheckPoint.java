package Entity.Items;

import Entity.Animation;
import Entity.Worm;
import PanelGraphics.TileMap;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 *
 * @author Balla
 */
public class CheckPoint extends Item {
    private BufferedImage[] sprites;
    
    public CheckPoint(TileMap tm, int id, int x, int y) {
        super(tm, id);
        setItemType(Item.CHECKPOINT);
                
        this.x = x ;
        this.y = y;
        
        width = 1;
        height = 1;
        cwidth = 10;
        cheight = 300;
        
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        sprites = new BufferedImage[1];
        sprites[0] = img;
        
        animation = new Animation();        
        animation.setFrames(sprites);
        animation.setDelay(2000);        
    }
    
    @Override
    public void useItem(Worm worm) {
        //move Item
        System.out.println("Entity.Items.CheckPoint.useItem()");
        worm.setCheckPoint((int)x, (int)y);
        setDeleted(true);
    }
    
    public void update() {
                
    }
    
    public void draw(Graphics2D g) {        
        setMapPosition();
        super.draw(g);
        g.setColor(Color.RED);
            Rectangle r1 = this.getRectangle();
            g.drawRect((int)(r1.x + xMapPos), (int)(r1.y + yMapPos), r1.width, r1.height);
    }   
    
}
