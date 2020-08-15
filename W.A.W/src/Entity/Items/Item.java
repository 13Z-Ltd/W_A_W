/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity.Items;

import Entity.GameObject;
import Entity.Worm;
import PanelGraphics.Tile;
import PanelGraphics.TileMap;
import java.awt.image.BufferedImage;

/**
 *
 * @author Balla
 */
public class Item extends GameObject {
    
    public static final int BRANCH_TYPE = 1001;
    public static final int APPLE_TYPE = 1002;
    public static final int ROCK_TYPE = 1003;    
    public static final int CHECKPOINT = 1004;
    
    private int ID;
    private int itemType;
    private BufferedImage icon;
    //private boolean active;
    private boolean leftMovementBlocked;
    private boolean rightMovementBlocked;
    
    private boolean isDeleted = false;
    
    public Item(TileMap tm, int id) {
    //public Item(TileMap tm, int id, int type) {
        super(tm);
        this.ID = id;
        //this.itemType = type;
    }
    
    public void checkFalling() {
        currCol = (int)x / tileSize;
        currRow = (int)y / tileSize;
        
        xdest = x + dx;
        ydest = y + dy;
        
        xtemp = x;
        ytemp = y;
        
        //if(!falling) falling = true;
        
        if(!falling) {
            calculateCorners(x, ydest + 1, Tile.BLOCKED);
            if(!bottomLeft && !bottomRight) {
                falling = true;
            }
        } 
        
        
        calculateDownTiles(x, ydest);
        if(dy > 0) {
            if(bottomLeft || bottomRight) {
                dy = 0;
                falling = false;
                ytemp = (currRow + 1) * tileSize - cheight / 2;
            } else {
                ytemp += dy;
            }
        }        
    }
    
    private void calculateDownTiles(double x, double y) {
        int leftTile = (int)(x - cwidth / 2) / tileSize;
        int rightTile = (int)(x + cwidth / 2 - 1) / tileSize;
        //int topTile = (int)(y - cheight / 2) / tileSize;
        int bottomTile = (int)(y + cwidth / 2 - 1) / tileSize;
        
        //int tl = tileMap.getType(topTile, leftTile);
        //int tr = tileMap.getType(topTile, rightTile);
        int bl = tileMap.getType(bottomTile, leftTile);
        int br = tileMap.getType(bottomTile, rightTile);
        bottomLeft = (bl == Tile.BLOCKED) || (bl ==  Tile.WATER);
        bottomRight = (br == Tile.BLOCKED) || (br ==  Tile.WATER);
    }
    
    public void useItem(Worm worm) { }    
    public void whenCollisionWithPlayer(Worm worm) {};
    
    public int getID() { return ID; }
    public int getItemType() { return itemType; }    
    public void setItemType(int type) { itemType = type; }
    
    public void setInventoryIcon(BufferedImage image) {
        icon = image;
    }
    
    public BufferedImage getInventoryIcon() {
        return icon;
    }
    
    public double getDx() { return dx; }
    
    public double getStopSpeed() {
        return stopSpeed;
    }
    
    public double getMoveSpeed() {
        return moveSpeed;
    }
    
    public double getMaxSpeed() {
        return maxSpeed;
    }    
    
    public void stopItem() {
        left = right = false;
    }
    
    public void setRightMovementBlocked(boolean b) { rightMovementBlocked = b; }
    public void setLeftMovementBlocked(boolean b) { leftMovementBlocked = b; }
    
    public boolean getLeftBlocked() { return leftMovementBlocked; }
    public boolean getRightBlocked() { return rightMovementBlocked; }
    
    public boolean isDeleted() { return isDeleted; }
    public void setDeleted(boolean b) { isDeleted = b; }
    
    //public boolean isActive() { return active; }
    //public void setActive(boolean active) { this.active = active; }
    
    public void update() {
        
    }
}
