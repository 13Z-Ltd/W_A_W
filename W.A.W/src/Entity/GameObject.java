/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import PanelGraphics.Tile;
import PanelGraphics.TileMap;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 *
 * @author Balla
 */
public class GameObject {
    //protected int itemID;
    
    protected TileMap tileMap;
    protected int tileSize;
    protected double xMapPos;
    protected double yMapPos;
    // maximum Y coordinate
    protected double heightOfTileMap;
    
    // position and vector
    protected double x;
    protected double y;
    protected double dx;
    protected double dy;
    
    // dimensions
    protected int width;
    protected int height;
    
    // collision box
    protected int cwidth;
    protected int cheight;
    
    // collision
    protected int currRow;
    protected int currCol;
    protected double xdest;
    protected double ydest;
    protected double xtemp;
    protected double ytemp;
    protected boolean topLeft;
    protected boolean topRight;
    protected boolean bottomLeft;
    protected boolean bottomRight;
    
    // animation
    protected Animation animation;
    protected int currentAction;
    protected int previousAction;
    protected boolean facingRight;
    
    // movement
    protected boolean left;
    protected boolean right;
    protected boolean up;
    protected boolean down;
    protected boolean jumping;
    protected boolean falling;
    
    //movement attributes
    protected double moveSpeed;
    protected double maxSpeed;
    protected double stopSpeed;
    protected double fallSpeed;
    protected double maxFallSpeed;
    protected double jumpStart;
    protected double stopJumpSpeed;
    
    //constructor
    public GameObject(TileMap tm) {
        tileMap = tm;
        tileSize = tileMap.getTileSize();
        heightOfTileMap = tileMap.getHeight() - tileSize;
        System.out.println("tile map height " + heightOfTileMap);
    }
    
    public void objectFallOut() { }
    
    public boolean intersects(GameObject o) {
        Rectangle r1 = getRectangle();
        Rectangle r2 = o.getRectangle();
        return r1.intersects(r2);
    }
    
    //public void checkTileMapCollision() {
    public void checkTileMapCollision(int tileType) {    
        currCol = (int)x / tileSize;
        currRow = (int)y / tileSize;
        
        xdest = x + dx;
        ydest = y + dy;
        
        xtemp = x;
        ytemp = y;
        
        calculateCorners(x, ydest, tileType);
        if(dy < 0) {
            if(topLeft || topRight) {
                dy = 0;
                ytemp = currRow * tileSize + cheight / 2;
            } else {
                ytemp += dy;
            }
        }
        if(dy > 0) {
            if(bottomLeft || bottomRight) {
                dy = 0;
                falling = false;
                ytemp = (currRow + 1) * tileSize - cheight / 2;
            } else {
                ytemp += dy;
            }
        }
        
        calculateCorners(xdest, y, tileType);
        if(dx < 0) {
            if(topLeft || bottomLeft) {
                dx = 0;
                xtemp = currCol * tileSize + cwidth / 2;
            } else {
                xtemp += dx;
            }
        }
        if(dx > 0) {
            if(topRight || bottomRight) {
                dx = 0;
                xtemp = (currCol + 1) * tileSize - cwidth / 2;
            } else {
                xtemp += dx;
            }
        }
        
        if(!falling) {
            calculateCorners(x, ydest + 1, tileType);
            if(!bottomLeft && !bottomRight) {
                falling = true;
            }
        }        
    }
    
    //public void calculateCorners(double x, double y) {
    public void calculateCorners(double x, double y, int tileType) {
        
        int leftTile = (int)(x - cwidth / 2) / tileSize;
        int rightTile = (int)(x + cwidth / 2 - 1) / tileSize;
        int topTile = (int)(y - cheight / 2) / tileSize;
        int bottomTile = (int)(y + cheight / 2 - 1) / tileSize;
        //int bottomTile = (int)(y + cwidth / 2 - 1) / tileSize;
        
        int tl = tileMap.getType(topTile, leftTile);
        int tr = tileMap.getType(topTile, rightTile);
        int bl = tileMap.getType(bottomTile, leftTile);
        int br = tileMap.getType(bottomTile, rightTile);
        
        topLeft = tl == Tile.BLOCKED;
        topRight = tr == Tile.BLOCKED;
        bottomLeft = bl == tileType;
        bottomRight = br == tileType;
        //bottomLeft = bl == Tile.BLOCKED;
        //bottomRight = br == Tile.BLOCKED;        
    }
    
    public int getx() { return (int)x; }
    public int gety() { return (int)y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getCWidth() { return cwidth; }
    public int getCHeight() { return cheight; }
    public boolean getFacingRight() { return facingRight; }
    
    //public int getItemID() { return itemID; }
    
    public void setPosition(double x, double y) {       
        this.x = x;
        this.y = y;
        
        if(y > heightOfTileMap)
            objectFallOut();
        //System.out.println("X: " + x + ", Y: " + y);
    }
    public void setVector(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }
    
    public void setDx(double dx) {
        this.dx = dx;
    }
    
    public void setDy(double dy) {
        this.dy = dy;
    }
    
    public void setMapPosition() {
        xMapPos = tileMap.getX();
        yMapPos = tileMap.getY();
    }
    
    public Rectangle getRectangle() {
        return new Rectangle(
                (int)x - cwidth / 2,
                (int)y - cheight / 2,
                cwidth,
                cheight);
    }
    
    public void setLeft(boolean b) { left = b; }
    public void setRight(boolean b) { right = b; }
    public void setUp(boolean b) { up = b; }
    public void setDown(boolean b) { down = b; }
    public void setJumping(boolean b) { jumping = b; }
    public void setFalling(boolean b) { falling = b; }
    
    public void draw(Graphics2D g) {
        if(facingRight) {
            g.drawImage(
                    animation.getImage(),
                    (int)(x + xMapPos - width / 2),
                    (int)(y + yMapPos - height / 2),
                    null
            );
        } else {
            g.drawImage(
                    animation.getImage(),
                    (int)(x + xMapPos - width / 2 + width),
                    (int)(y + yMapPos - height / 2),
                    -width,
                    height,
                    null
            );
        }
        //g.fillOval((int)(x + xMapPos)- 1, (int)(y + yMapPos)- 1, 2, 2);        
        /*
        g.setColor(Color.RED);
        Rectangle r1 = getRectangle();
        g.drawRect((int)(r1.x + xMapPos), (int)(r1.y + yMapPos), r1.width, r1.height);
        */
    }
}
