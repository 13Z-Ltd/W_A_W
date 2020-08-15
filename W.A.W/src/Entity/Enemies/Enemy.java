package Entity.Enemies;

import Entity.GameObject;
import PanelGraphics.TileMap;

/**
 *
 * @author Balla
 */
public class Enemy extends GameObject {
    
    protected int health;
    protected int maxHealth;
    protected boolean dead;
    protected int damage;
    
    
    public Enemy(TileMap tm) {
        super(tm);
    }
    
    public boolean isDead() { return dead; }
    
    public int getDamage() { return damage; }
    
    public void hit(int damage) {    }
    
    public void update() {
        
    }
}
