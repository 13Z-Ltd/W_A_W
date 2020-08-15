package waw_mapeditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 *
 * @author Balla
 */
public class TilesPanel extends JPanel {
    
    private int TILESIZE;
    //private int panelWidth = 1024;
    private int panelWidth = 1100;
    private int panelHeight = 165;
    private int SCALE = 1;
    private int currentRows;
    
    public BufferedImage tileset;
    public Tile[][] tiles;
    private int numTilesAcross;
    private int selectedTileNumber;
    
    public Graphics2D g;
    
    public TilesPanel(int tileSize, int scale, Tile[][] tiles, int rows) {
        this.TILESIZE = tileSize;
        this.SCALE = scale;
        this.tiles = tiles;
        this.currentRows = rows;
        
        setPreferredSize(new Dimension(panelWidth, (TILESIZE * SCALE * currentRows) + 10));
        setBackground(Color.LIGHT_GRAY);  
        
        numTilesAcross = tiles[0].length;
        selectedTileNumber = 1;
    }    
    
    public int getSelectedTileNumber() {
        return selectedTileNumber;
    }
    
    public int setSelectedTileNumber(int col, int row) {
        if(col < numTilesAcross && row < currentRows){
            selectedTileNumber = row * numTilesAcross + col + 1;
            return 1;
        }else {
            return 0;
        }
    }
    
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        setBackground(Color.LIGHT_GRAY);
        if (tiles != null) {
            for (int i = 0; i < numTilesAcross; i++) {
                g2.drawImage(tiles[0][i].getImage(), i * TILESIZE * SCALE, 0,
                    TILESIZE * SCALE,      //WIDTH * SCALE,
                    TILESIZE * SCALE,     //HEIGHT * SCALE,
                    null
                );
                if(currentRows > 1) {
                    g2.drawImage(tiles[1][i].getImage(), i * TILESIZE * SCALE, TILESIZE * SCALE,
                        TILESIZE * SCALE,      //WIDTH * SCALE,
                        TILESIZE * SCALE,     //HEIGHT * SCALE,
                        null
                    );
                }
            }
        }
        
        g2.finalize();
    }
}
