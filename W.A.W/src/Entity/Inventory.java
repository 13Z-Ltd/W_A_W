package Entity;

import Entity.Items.Item;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *
 * @author Balla
 */
public class Inventory {
    
    private Worm worm;
    
    private int numberOfItems;
    private int maxNumberOfItems;
    private ArrayList<Item> items;
    
    private Item selectedItem;
    private int selectedItemIndex;
    
    private int startPointX = 440;
    private int startPointY = 10;
    private int inventWidht = 190;
    private int inventHeight = 50;
    private Color backgroundColor;
    private boolean visible;
    
    public Inventory(Worm w) {
        worm = w;
        visible = false;
        backgroundColor = new Color(211,211,211, 50);
        
        numberOfItems = 0;
        maxNumberOfItems = 4;
        items = new ArrayList<>();
    }
    
    public void changeVisible() { visible = !visible; }
    public boolean isVisible() { return visible; }
    
    public void nextItem() {
        if(selectedItemIndex < numberOfItems - 1 && visible) {
            selectedItemIndex++;
        }
    }
    
    public void prevItem() {
        if(selectedItemIndex > 0 && visible) {
            selectedItemIndex--;
        }
    }
    
    public void addItem(Item item) {
        if(numberOfItems < maxNumberOfItems) {
            items.add(item);
            numberOfItems++;        
        }
    }
    
    public void deleteItem(Item item) {
        items.remove(item);
        numberOfItems--;
        if(selectedItemIndex >= numberOfItems && numberOfItems != 0) 
            selectedItemIndex--;
        selectedItem = null;
    }
    
    public Item selectedItem(){
        if(numberOfItems > 0) {
            //selectedItem = items.get(0);        
            selectedItem = items.get(selectedItemIndex);
        } else {
            selectedItem = null;
        }
        
        return selectedItem;
    }
    
    public void draw(Graphics2D g) {
        if(visible) {
            g.setColor(backgroundColor);            
            g.fillRect(startPointX, 10, inventWidht, inventHeight);
            g.setColor(new Color(250, 250, 250, 150));
            g.drawString("Inventory", startPointX + 2, startPointY + 2 );
            
            float[] dashingPattern1 = {2f, 2f};
            g.setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.0f, dashingPattern1, 2.0f));
            // dark transparent gray
            g.setColor(new Color(43, 45, 47, 150));
            for (int i = 0; i < maxNumberOfItems; i++) {
                g.fillRect(startPointX + 10 + (i * 45), startPointY + 5, 40, 40);
                
                if(i < numberOfItems) {
                    BufferedImage image = items.get(i).getInventoryIcon();                
                    g.drawImage(image, startPointX + 30 + (45 * i) - (image.getWidth()/2), startPointY + 25 - (image.getHeight() / 2), null);
                }                
            }
            if (numberOfItems != 0) {
                g.setColor(new Color(186, 165, 0, 150));
                g.drawRect(startPointX + 10 + (selectedItemIndex * 45), startPointY + 5, 40, 40);
            }                
        }
    }    
}
