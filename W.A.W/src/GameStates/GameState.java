/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameStates;

/**
 *
 * @author Balla
 */
public abstract class GameState {
    
    protected GameStateController gsc;
    protected boolean returnToMain;
    
    public abstract void init();
    public abstract void update();
    public abstract void draw(java.awt.Graphics2D g);
    public abstract void keyPressed(int k);
    public abstract void keyReleased(int k);
    
    public void setReturn(boolean b) { returnToMain = b; }
    public boolean getReturn() { return returnToMain; }
}
