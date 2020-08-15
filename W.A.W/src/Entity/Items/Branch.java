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
public class Branch extends Item {
    // animations
    private BufferedImage[] sprites;
    //private BufferedImage icon;
    
    public Branch(TileMap tm, int id) {
        super(tm, id);
        setItemType(Item.BRANCH_TYPE);
        
        //ID = 1001;
        
        width = 30;
        height = 7;
        cwidth = 30;
        cheight = 7;
        
        moveSpeed = 0.45;
        maxSpeed = 2.5;
        stopSpeed = 0.12;
        fallSpeed = 0.25;
        maxFallSpeed = 2.0;
        
        facingRight = true;
        
        try {
            BufferedImage spritesheet = ImageIO.read(
                    getClass().getResourceAsStream("/Sprites/treebranch30.png")
            );
            setInventoryIcon(ImageIO.read(
                    getClass().getResourceAsStream("/Sprites/branch_icon.png")
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
    
    @Override
    public void whenCollisionWithPlayer(Worm worm) {
        Rectangle rect = this.getRectangle();
        if(worm.getx()  > rect.x - 6  && worm.getx() - 5 < (rect.x + rect.width) + 6){
            worm.setDy(0);
            worm.setFalling(false);
        }
        //dy = 0; // majd lehet ezt máshova kéne
        //falling = false;
    }
    
    @Override
    public void useItem(Worm worm) { //, double xxx) {
        //Rectangle rect = this.getRectangle();
        //System.out.println("Worm: " + worm.getx() + ", " + worm.gety() + "  Branch: " + rect.x + ", " + (rect.x + rect.width));
        if(worm.getAction()) {
            Rectangle rect = this.getRectangle();
            if(worm.getx() > rect.x  && worm.getx() < (rect.x + rect.width)){
            if(worm.getFacingRight()) {
                if(!getRightBlocked())
                    worm.setMoveWithItem(worm.getMoveWithItem() + moveSpeed);
                else
                    worm.setMoveWithItem(0);

                if (worm.getMoveWithItem() > maxSpeed) 
                    worm.setMoveWithItem(maxSpeed); // = itemMaxSpeed;

                this.dx = worm.getMoveWithItem();
                worm.setDx(worm.getMoveWithItem());
            } else {
                if(!getLeftBlocked())
                    worm.setMoveWithItem(worm.getMoveWithItem() - moveSpeed); // -= itemMoveSpeed;
                else
                    worm.setMoveWithItem(0);

                if (worm.getMoveWithItem() < -maxSpeed)
                    worm.setMoveWithItem(-maxSpeed); // = -itemMaxSpeed;

                dx = worm.getMoveWithItem();
                worm.setDx(worm.getMoveWithItem());
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
    
    @Override
    public Rectangle getRectangle() {
        return new Rectangle(
                (int)x - cwidth / 2,
                (int)y - cheight / 2 - 3,
                cwidth,
                cheight);
    }
    
    private void getNextPosition() {
        
        if(falling) {
            dy +=fallSpeed;
            
            if (dy > maxFallSpeed) dy = maxFallSpeed;
        }
        
        /*
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
        */
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
        //}
        
    }
    
    private void checkHorizontalMoving(){
        currCol = (int)x / tileSize;
        currRow = (int)y / tileSize;
        
        xdest = x + dx;
        //ydest = y + dy;        
        xtemp = x;
        
        calculateHorizontalTiles(xdest, y);
        if(dx > 0) {
            if(topRight || bottomRight) {
                dx = 0;
                setRightMovementBlocked(true);
                //falling = false;
                xtemp = (currCol + 1) * tileSize - cwidth / 2;
            } else {
                xtemp += dx;
                setLeftMovementBlocked(false);
            }
        } else {
            if(topLeft || bottomLeft) {
                dx = 0;
                setLeftMovementBlocked(true);
                //falling = false;
                xtemp = (currCol) * tileSize + cwidth / 2;
            } else {
                xtemp += dx;
                setRightMovementBlocked(false);
            }
        }
    }
    
    private void calculateHorizontalTiles(double x, double y) {
        int leftTile = (int)(x - cwidth / 2) / tileSize;
        int rightTile = (int)(x + cwidth / 2 - 1) / tileSize;
        int topTile = (int)(y - cheight / 2) / tileSize;
        int bottomTile = (int)(y + cwidth / 2 - 1) / tileSize;
        
        int tl = tileMap.getType(topTile, leftTile);
        int tr = tileMap.getType(topTile, rightTile);
        int bl = tileMap.getType(bottomTile, leftTile);
        int br = tileMap.getType(bottomTile, rightTile);
        topLeft = (tl == Tile.BLOCKED);
        topRight = (tr == Tile.BLOCKED);
        bottomLeft = (bl == Tile.BLOCKED); // || (bl ==  Tile.WATER);
        bottomRight = (br == Tile.BLOCKED); // || (br ==  Tile.WATER);
    }
    
    public void update() {
        //checkTileMapCollision(Tile.WATER);
        getNextPosition();
        checkFalling();
        if(dx != 0) checkHorizontalMoving();        
        //checkOnWaterPositions();
        
        setPosition(xtemp, ytemp);        
    }
    
    public void draw(Graphics2D g) {
        
        setMapPosition();
        
        super.draw(g);
    }        
    
    /*
    public BufferedImage getInventoryIcon() {
        return icon;
    }
    */
    
    /*
    public void checkOnWaterPositions() {
        xtemp = x + dx;
        if(xtemp < waterFrom) xtemp = waterFrom;
        if(xtemp > waterTo) xtemp = waterTo;
    }
    */
}