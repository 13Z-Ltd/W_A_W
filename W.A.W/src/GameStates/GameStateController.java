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
public class GameStateController {
    
    
    private GameState[] gameStates;
    private int currentState;
    
    private boolean notDraw;
    
    public static final int STATESNUMBER = 2;
    public static final int HOMESTATE = 0;
    public static final int FORRESTLEVEL1 = 1;
    
    public GameStateController() {
        
        gameStates = new GameState[STATESNUMBER];        
        currentState = HOMESTATE;
        loadState(currentState);
        
        notDraw = false;
    }

    private void loadState(int state) {
        if(state == HOMESTATE)
            gameStates[HOMESTATE] = new HomeState(this);
        if(state == FORRESTLEVEL1)
            gameStates[state] = new ForrestLevel1(this);
        //if(state == LEVEL1STATE)
    }
    
    private void unloadState(int state) {
        gameStates[state] = null;
    }
    
    public void setSate(int state) {
        unloadState(currentState);
        currentState = state;
        loadState(currentState);
    }
    
    public void  update() {
        try {
            gameStates[currentState].update();
            if(gameStates[currentState].getReturn()){
                setSate(HOMESTATE);
                System.out.println("GameStates.GameStateController.update()");
            }
                
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }
    
    public void draw(java.awt.Graphics2D g) {
        try {
            if(!notDraw)
                gameStates[currentState].draw(g);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void keyPressed(int key) {
        gameStates[currentState].keyPressed(key);
    }

    public void keyReleased(int key) {
        gameStates[currentState].keyReleased(key);
    }
    
    public boolean getNotDraw() { return notDraw; }
    public void setNotDraw(boolean bool) { this.notDraw = bool; }
}
