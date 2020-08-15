package waw_mapeditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 *
 * @author Balla
 */
public class EntitiesPanel extends JPanel {
    private int panelWidth = 1150;
    private int panelHeight = 105;
    private int SCALE = 1;
    
    public ArrayList<Entity> entities;
        
    public EntitiesPanel(ArrayList<Entity> entities) {
        this.entities = entities;
        setPreferredSize(new Dimension(panelWidth, panelHeight));
        setBackground(Color.BLUE);        
        init();
    }
    
    private void init() {
        /*
        for (Entity entity : entities) {
            
        }
        */
        this.isFocusable();
    }
    
    public int getItemNumber(int mouseX, int mouseY) {
        int col = (mouseX - 100) / 80;
        int row = (mouseY - 20) / 80;
        
        if(mouseX > 99 && col < entities.size() && row == 0){
            return col;
        } else {
            return -1;
        }
    }
    
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;     
        
        int i = 0;
        for (Entity entity : entities) {
            g2.drawImage(entity.image,
                    100 + i * 80,
                    20,
                    (int)(entity.width * 1.5),
                    (int)(entity.height * 1.5),
                    null
            );
            i++;
        }        
    }
}
