/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Entity.Items.Item;
import PanelGraphics.Tile;
import PanelGraphics.TileMap;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 *
 * @author Balla
 */
public class Worm extends GameObject{
    private int health;
    private int maxHealth;
    private boolean dead;
    private boolean flinching;
    private long flinchTimer;
    private Point startPoint;
    private Point checkPoint;
    
    // animations
    private ArrayList<BufferedImage[]> sprites;
    //private final int [] numFrames = {8, 8, 10};
    private final int [] numFrames = {3, 8, 10};
    
    // Item/Inventory
    private Item activeItem;
    private Inventory inventory;
    
    //my own bigJump
    private boolean bigJump;
    private boolean bigJumpTimerIsOn;    
    private double bigJumpTimer;
    private long previousJumpTime;
    
    //for move direct enteties
    private boolean action;
    private boolean shift;
    private boolean rightMoveBlocked;
    private boolean leftMoveBlocked;
    
    // item parameters
    private double moveWithItem;
    
    private static final int IDLE = 0;
    private static final int WALKING = 1;
    private static final int ATTACK = 2;
    
    public Worm(TileMap tm) {
        super(tm);
        
        health = 4;
        maxHealth = 5;
        
        width = 40;
        height = 40;
        cwidth = 30;
        cheight = 30;
        
        moveSpeed = 0.6;
        maxSpeed = 3.0;
        stopSpeed = 0.7;
        fallSpeed = 0.3;
        maxFallSpeed = 8.0;
        jumpStart = -9;
        stopJumpSpeed = 0.6;
        
        facingRight = true;
        
        activeItem = null;
        inventory = new Inventory(this); 
        
        // load sprites
        try {
            BufferedImage spritesheet = ImageIO.read(
                    getClass().getResourceAsStream("/Sprites/Worm/King_Worm_Tileset.png")
                    //getClass().getResourceAsStream("/Sprites/Worm/King_Worm.png")
            );
            
            sprites = new ArrayList<BufferedImage[]>(); //sprites = new BufferedImage[1];
            
            for (int i = 0; i < 3; i++) {
                BufferedImage[] bi = new BufferedImage[numFrames[i]];
                for (int j = 0; j < numFrames[i]; j++) {
                    if(i == 1){
                        bi[j] = spritesheet.getSubimage(
                                (int) (j * width * 0.92),
                                i * height,
                                width - 3,
                                height
                        );
                    } else {
                        bi[j] = spritesheet.getSubimage(
                                j * width,
                                i * height,
                                width,
                                height
                        );
                    }
                }
                sprites.add(bi);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        animation = new Animation();
        currentAction = IDLE;
        animation.setFrames(sprites.get(IDLE));
        animation.setDelay(100);
    }
    
    public void setStartPoint(int x, int y) { startPoint = new Point(x, y); setPosition(x, y); }
    public void setCheckPoint(int x, int y) { checkPoint = new Point(x, y); }
    public int getHealth() { return health; }
    public void addHealth(int health) { this.health += health; }
    public int getMaxHealth() { return maxHealth; }    
    
    public boolean hasActiveItem(){ return activeItem != null; }    
    public Item getActiveItem() { return activeItem;  }    
    public void setInventoryVisibality() { inventory.changeVisible(); }
    
    public Item addItemToInventory() {
        if(hasActiveItem()){
            inventory.addItem(activeItem);
            return activeItem;
        } else 
            return null;        
    }
    
    public void deleteUsedItem(Item item) {
        if(item != null)
            inventory.deleteItem(item);
    }
    
    public Item dropSelectedItem() {
        if(inventory.selectedItem() != null) {
            if (facingRight)
                inventory.selectedItem().setPosition(this.x + 25, this.y - 10);
            else
                inventory.selectedItem().setPosition(this.x - 25, this.y - 10);
        }
        return inventory.selectedItem();
    }
    
    public void inventoryNext() { inventory.nextItem(); }    
    public void inventoryPrev() { inventory.prevItem(); }    
    public void setAction(boolean b) { action = b; }
    public boolean getAction() { return action; }
    public void setShift(boolean b) { shift = b; }
    public void setRightMoveBlocked(boolean b) { rightMoveBlocked = b; }
    public void setLeftMoveBlocked(boolean b) { leftMoveBlocked = b; }
    public boolean getLeft() { return left; }
    public boolean getRight() { return right; }
    public boolean getTopLeft() { return topLeft; }
    public boolean getTopRight() { return topRight; }
    public boolean isDead() { return dead; }
    
    public void setMoveWithItem(double dx) { this.moveWithItem = dx; }    
    public double getMoveWithItem() { return moveWithItem; }    
    
    public boolean getBigJumpTimerIsOn() { return bigJumpTimerIsOn; }    
    public void setBigJumpTimerIsOn(boolean on_off) { this.bigJumpTimerIsOn = on_off; }   
    
    public void getHit(int damage) {
        if(flinching) return;
        health -= damage;
        if(health < 0) health = 0;
        if(health == 0) dead = true;
        
        flinching = true;
        flinchTimer = System.nanoTime();
    }
    
    public void bigJumpTimerStart() {
        if(!bigJumpTimerIsOn) {
            bigJumpTimerIsOn = true;
            bigJumpTimer = System.nanoTime(); //10ed másodpercek
        }
    }
    
    public void bigJumpTimerStop() {
        if(bigJumpTimerIsOn) {
            bigJumpTimer = (System.nanoTime() - bigJumpTimer) / 2000000000; //10ed másodpercek
            if(bigJumpTimer + 0.2 > 0.6) bigJumpTimer = 0.6;

            bigJumpTimerIsOn = false;
            bigJump = true;
        }
        //if(bigJumpTimer < 0.1) jumping = true;
        //if(bigJumpTimer >= 0.1 && bigJumpTimer <= 0.6) 
            //bigJump = true;
    }
    
    private void getNextPosition() {
        // movement
        if(left && !bigJumpTimerIsOn && !action){           // if(left && !bigJumpTimerIsOn
            dx -= moveSpeed;
            if(dx < -maxSpeed){
                dx = -maxSpeed;
            }
        } else if(right && !bigJumpTimerIsOn && !action){                       //} if(right && !bigJumpTimerIsOn)
            dx += moveSpeed;
            if(dx > maxSpeed) {
                dx = maxSpeed;
            }
        }       // my new part
        else if(left && bigJumpTimerIsOn) {
            if(dx < 0){
                dx += stopSpeed / 2;
                if(dx > 0) {
                    dx = 0;
                }
            } else { 
                dx = 0;
            }            
        } else if(right && bigJumpTimerIsOn) {
            if(dx > 0){
                dx -= stopSpeed / 2;
                if(dx < 0) {
                    dx = 0;
                }
            } else { 
                dx = 0;
            }
        } // eddig
        else { //if(!action) {
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
        
        // jumping
        if(jumping && !falling && !shift) {
            dy = jumpStart;
            falling = true;
        }
        
        //bigJump
        if(bigJump && !falling) {
            dy = jumpStart * (1.0 + bigJumpTimer);       //1.6;
            bigJump = false;
            falling = true;
        }
        
        // falling
        if(falling) {
            //if(dy > 0 )
            dy += fallSpeed;
            
            if(dy > 0) jumping = false;
            if(dy < 0) dy += stopJumpSpeed;
            
            if(dy > maxFallSpeed) dy = maxFallSpeed;            
            //System.out.println("X: " + x + ", Y: " + y);
        }
        
        // blocked movve checking
        if(dx > 0 && rightMoveBlocked) dx = 0;
        if(dx < 0 && leftMoveBlocked) dx = 0;
            
        // move direct enteties          
        if(activeItem != null) {
            activeItem.useItem(this);
        }
    }
    
    public void checkCollisionWithGameItems(ArrayList<Item> items){
        Item currentItem = null;
        for (Item item : items) {
            if(intersects(item)) {
                item.whenCollisionWithPlayer(this);
                //dy = 0; // majd lehet ezt máshova kéne
                //falling = false;
                currentItem = item;
            }
        }
        if (currentItem == null) {
            if(activeItem != null) {
                activeItem.stopItem();
                activeItem = null;
                //System.out.println("Unload activeItem");
            }
        } else {
            if((activeItem != null && activeItem.getID() != currentItem.getID()) || activeItem == null) {                
                activeItem = currentItem;
                //System.out.println("Load activeItem");
            }
        }
    }   
    
    @Override
    public void objectFallOut(){
        getHit(1);
        
        if(checkPoint == null)
            setPosition(startPoint.x, startPoint.y);
        else 
            setPosition(checkPoint.x, checkPoint.y);
    }
    
    public void update() {
        if(flinching) {
            long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
            if(elapsed > 1000)
                flinching = false;
        }
        
        // movement
        if(shift && jumping) { bigJumpTimerStart();}
        
        getNextPosition();
        checkTileMapCollision(Tile.BLOCKED);
        setPosition(xtemp, ytemp);
        
        if(left || right) {
            if(currentAction != WALKING) {
                currentAction = WALKING;
                animation.setFrames(sprites.get(WALKING));
                animation.setDelay(40);
                width = 35;
            }
        } else {
            if(currentAction != IDLE) {
                currentAction = IDLE;
                animation.setFrames(sprites.get(IDLE));
                animation.setDelay(100);
                width = 40;
            }
        }
        
        animation.update();
        
        // set direction
        if(right) facingRight = true;
        if(left) facingRight = false;
        
        //clear blocked moves
        leftMoveBlocked = rightMoveBlocked = false;
    }
    
    public void draw(Graphics2D g) {        
        setMapPosition();
        
        // draw inventory
        inventory.draw(g);
        
        // draw player worm
        if(flinching){
            long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
            if(elapsed / 100 % 2 == 0)
                return;
        }        
        super.draw(g);
        /*
        if(activeItem != null) {
            g.setColor(Color.RED);
            Rectangle r1 = activeItem.getRectangle();
            g.drawRect((int)(r1.x + xMapPos), (int)(r1.y + yMapPos), r1.width, r1.height);
        }
        */        
    } 
    
    // old move with item
                /*
            if(action) {            
                if(facingRight) {
                    //activeItem.setLeft(true);
                    //dx = activeItem.getDx();
                    if(!activeItem.getRightBlocked())
                        moveWithItem += itemMoveSpeed;
                    else
                        moveWithItem = 0;
                    
                    if (moveWithItem > itemMaxSpeed) moveWithItem = itemMaxSpeed;
                    //activeItem.moveItem(moveWithItem);
                    activeItem.useItem(moveWithItem);
                    dx = moveWithItem;
                } else {
                    if(!activeItem.getLeftBlocked())
                        moveWithItem -= itemMoveSpeed;
                    else
                        moveWithItem = 0;
                    
                    if (moveWithItem < -itemMaxSpeed) moveWithItem = -itemMaxSpeed;
                    activeItem.useItem(moveWithItem);
                    dx = moveWithItem;
                }
            }else { //} else if (shift) {
                if(moveWithItem != 0){
                    if(moveWithItem > 0) {
                        moveWithItem -= itemStopSpeed;
                        if(moveWithItem < 0) moveWithItem = 0;
                        //dx = moveWithItem;
                    } else {
                        moveWithItem += itemStopSpeed;
                        if(moveWithItem > 0) moveWithItem = 0; 
                        //dx = moveWithItem;
                    }
                    dx = moveWithItem;
                }
            }
            */
}