/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameStates;

import Entity.Enemies.Enemy;
import Entity.Enemies.Hedgehog;
import Entity.HUD;
import Entity.Items.*;       //.Branch;
import Entity.Worm;
import Main.GamePanel;
import PanelGraphics.Background;
import PanelGraphics.TileMap;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author Balla
 */
public class ForrestLevel1 extends GameState {    
    private Background bg;
    private TileMap tileMap;
    
    private Worm worm;
    private HUD hud;
    
    private ArrayList<Item> items;
    private ArrayList<Enemy> enemies;
    
    
    public ForrestLevel1(GameStateController gsc) {
        this.gsc = gsc;
        init();
    }

    @Override
    public void init() {
        // load background
        String[] names = {"/Backgrounds/layer11.png", "/Backgrounds/layer2.png"};
        bg = new Background(names, 0.1);            
        //load tileMap
        tileMap = new TileMap(40);
        
        items = new ArrayList<Item>();
        enemies = new ArrayList<Enemy>();
            
        tileMap.loadMap("/Maps/level1_2.map", items, enemies);        
        //tileMap.loadMap("/Maps/forrestlevel1.map", items, enemies);
        
        //tileMap.loadTileset("/Tilesets/forresttileset_40.png");
        tileMap.loadTileset("/Tilesets/forresttileset.png");
        // load NORMAL tileset        
        tileMap.loadTilesetNORMAL("/Tilesets/forresttileset2_40.png");        
        tileMap.setPosition(0, 0);
        tileMap.setTween(1);
        
        //set object Y max from tile map
        
        //load objects from tileMap        
        worm = new Worm(tileMap);
        worm.setStartPoint(200, 200);
        //worm.setStartPoint(1120, 200);
        
        hud = new HUD(worm);   
        
        /*
        items = new ArrayList<Item>();
        Branch branch = new Branch(tileMap, 1);
        branch.setPosition(1200, 200);              // 30 * 40
        items.add(branch);
        branch = new Branch(tileMap, 2);
        branch.setPosition(1480, 200);              // 37 * 40
        items.add(branch);
        
        branch = new Branch(tileMap, 2);
        branch.setPosition(920, 100);              // 37 * 40
        items.add(branch);
        
        Apple apple = new Apple(tileMap, 3);
        apple.setPosition(900, 100);
        items.add(apple);
        
        Rock rock = new Rock(tileMap, 4);
        rock.setPosition(1020, 100);
        items.add(rock);
        
        CheckPoint cp = new CheckPoint(tileMap, 4, 1340, 200);
        items.add(cp);
        
        enemies = new ArrayList<Enemy>();
        Hedgehog hedgehog = new Hedgehog(tileMap);
        hedgehog.setPosition(700, 100);
        enemies.add(hedgehog);
        */
        //inventory.addItemToInventory(branch.getID(), branch.getInventoryIcon());        
    }
    
    public void deleteItem(Item item){
        if(item != null)
            items.remove(item);
    }
    
    public void addItem(Item item) {
        if(item != null)
            items.add(item);
    }
    
    private void takeItem() {
        deleteItem(worm.addItemToInventory());
    }
    
    private void useItem() {
        //Item usedItem = worm.getActiveItem();
        addItem(worm.dropSelectedItem());
        worm.deleteUsedItem(worm.dropSelectedItem());  
    }
    
    private void checkCollisionWithEnemy(Enemy enemy) {
        if(worm.intersects(enemy)) {
            worm.getHit(enemy.getDamage());
        }
    }
    
    private void restartProcedure() {
        Object[] options = {"Restart", "Exit to Menu"};       
        int answer = JOptionPane.showOptionDialog( null,
                "Would you like to retart this level",
                "Game Over",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[ 0 ] 
        );
        if(answer == 0)
            init();
        else if(answer == 1)
            setReturn(true);
    }

    @Override
    public void update() {
        if(worm.isDead()){
            restartProcedure();            
        }
        worm.checkCollisionWithGameItems(items);
        
        // update player
        worm.update();
        
        tileMap.setPosition(
                GamePanel.WIDTH / 2 - worm.getx(),
                GamePanel.HEIGHT / 2 - worm.gety()
        );
        
        // set Background
        bg.setPosition(tileMap.getX(), tileMap.getY());
        
        // items update
        for (Item item : items) {
            if(!item.isDeleted()){
                item.update();
            } else {
                items.remove(item);
            }
        }
        
        // enemies update
        for (Enemy enemy : enemies) {
            enemy.update();
            checkCollisionWithEnemy(enemy);
        }
    }

    @Override
    public void draw(Graphics2D g) {
        // draw gackground
        bg.draw(g);
        
        // draw tileMap
        tileMap.draw(g);
        
        // draw player worm
        worm.draw(g);
        
        // items draw           treeBranch 
        for (Item item : items) {
            item.draw(g);
        }
        
        // enemies draw
        for (Enemy enemy : enemies) {
            enemy.draw(g);
        }
        
        // draw HUD
        hud.draw(g);
    }

    @Override
    public void keyPressed(int k) {
        if(k == KeyEvent.VK_LEFT) worm.setLeft(true);
        if(k == KeyEvent.VK_RIGHT) worm.setRight(true);
        if(k == KeyEvent.VK_UP) worm.setUp(true);
        if(k == KeyEvent.VK_DOWN) worm.setDown(true);
        if(k == KeyEvent.VK_SPACE) worm.setJumping(true);
        if(k == KeyEvent.VK_SHIFT) worm.setShift(true);        
        if(k == KeyEvent.VK_E) worm.setAction(true);
        if(k == KeyEvent.VK_TAB) worm.setInventoryVisibality();
        if(k == KeyEvent.VK_I) worm.setInventoryVisibality();
        if(k == KeyEvent.VK_T) takeItem();
        if(k == KeyEvent.VK_F) useItem();
        if(k == KeyEvent.VK_A) worm.inventoryPrev();
        if(k == KeyEvent.VK_D) worm.inventoryNext();
    }

    @Override
    public void keyReleased(int k) {
        if(k == KeyEvent.VK_LEFT) worm.setLeft(false);
        if(k == KeyEvent.VK_RIGHT) worm.setRight(false);
        if(k == KeyEvent.VK_UP) worm.setUp(false);
        if(k == KeyEvent.VK_DOWN) worm.setDown(false);        
        if(k == KeyEvent.VK_SPACE) {
            worm.setJumping(false);
            worm.bigJumpTimerStop();
        }    
        if(k == KeyEvent.VK_SHIFT) worm.setShift(false);        
        if(k == KeyEvent.VK_E) worm.setAction(false);
    }
}
