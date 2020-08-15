/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package waw_mapeditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 *
 * @author Balla
 */
public class MapEditor extends JPanel { //implements Runnable{
    
    public static final int BRANCH_TYPE = 1001;
    public static final int APPLE_TYPE = 1002;
    public static final int ROCK_TYPE = 1003;    
    public static final int CHECKPOINT = 1004;
    
    public int numberOfRows;
    public int numberOfCols;
    public int TILESIZE;
    public int SCALE = 1;
    public int WIDTH;
    public int HEIGHT;
    
    private Tile[][] blockedTiles;
    private Tile[][] normalTiles;
    private ArrayList<Entity> items;
    private ArrayList<Entity> enemies;
    private int blockedTilesColNumber;
    private int normalTilesColNumber;
    
    private BufferedImage image;
    private BufferedImage currentImage;
    private int currentImageX;
    private int currentImageY;
    
    private Graphics2D g;
    
    // map
    private int[][] map;
    
    public MapEditor(int col, int row, int tileSize, int scale, 
            Tile[][] tiles, Tile[][] normalTiles, ArrayList<Entity> items, ArrayList<Entity> enemies) {
        this.numberOfCols = col;
        this.numberOfRows = row;
        this.TILESIZE = tileSize;
        this.SCALE = scale;
        this.blockedTiles = tiles;
        this.normalTiles = normalTiles;
        this.items = items;
        this.enemies = enemies;
        
        this.currentImage = null;
        
        this.blockedTilesColNumber = tiles[0].length;
        this.normalTilesColNumber = normalTiles[0].length;
        
        this.setBackground(Color.DARK_GRAY);
        /*
        WIDTH = numberOfCols * TILESIZE;
        HEIGHT = numberOfRows * TILESIZE;        
        */
        init();
    }
    
    public void init() {
        WIDTH = numberOfCols * TILESIZE;
        HEIGHT = numberOfRows * TILESIZE;        
        
        setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));          //(WIDTH * SCALE, HEIGHT * SCALE));
        setFocusable(true);
        requestFocus();
        
        map = new int[numberOfCols][numberOfRows];
        items.clear();
        enemies.clear();
        //items = new ArrayList<Entity>();
        //enemies = new ArrayList<Entity>();
        
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        g = (Graphics2D) image.getGraphics();
    }
    
    public void addTileToMap(int col, int row, int tileNumber){
        map[col][row] = tileNumber;
    }
    
    public void addLineToMap(int row, int tileNumber) {
        for (int i = 0; i < numberOfCols; i++) {
            map[i][row] = tileNumber;
        }
    }
    
    public void setCurrentImage(BufferedImage ci) {
        currentImage = ci;
    }
    
    public void setCurrentImagePosition(int x, int y) {
        currentImageX = (x / 2) - TILESIZE / 2;
        currentImageY = (y / 2) - TILESIZE / 2;
    }
    
    public void paintMap() {
        draw();
        drawToScreen();
    }
    
    public void newMap(int col, int row){
        this.numberOfCols = col;
        this.numberOfRows = row;
        init();
        paintMap();
    }
    
    public void changeMapSize(int newCol, int newRow) {
        int[][] oldMap = map;
        int oldCols = numberOfCols;
        int oldRows = numberOfRows;
        
        numberOfCols = newCol;
        numberOfRows = newRow;
        init();
        
        // copy old datas
        int rowDifferents = newRow - oldRows;
        
        for (int row = 0; row < oldRows; row++) {
            for (int col = 0; col < oldCols; col++) {
                map[col][rowDifferents + row] = oldMap[col][row];
            }
        }        
        paintMap();
    }
    
    public void loadMapFromFile(File file) {
        try {
            InputStream in = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            
            numberOfCols = Integer.parseInt(br.readLine());
            numberOfRows = Integer.parseInt(br.readLine());
            
            init();
            
            // Load map
            //map = new int[numberOfCols][numberOfRows];
            
            String delims = "\\s+";
            for (int row = 0; row < numberOfRows; row++) {
                String line = br.readLine();
                String[] tokens = line.split(delims);
                for (int col = 0; col < numberOfCols; col++) {
                    map[col][row] = Integer.parseInt(tokens[col]);
                }
            }
            
            // items reading
            int numItems = Integer.parseInt(br.readLine());
            for (int i = 0; i < numItems; i++) {
                String line = br.readLine();
                String[] tokens = line.split(delims);
                int[] datas = new int[5];
                for (int j = 0; j < 5; j++) {
                    datas[j] = Integer.parseInt(tokens[j]);                    
                }
                switch (datas[0]) {
                    case BRANCH_TYPE:
                        Entity newEntity = new Entity(BRANCH_TYPE, 30, 7, null);
                        newEntity.setEntityPosition(datas[1], datas[2]);
                        items.add(newEntity);
                        break;
                    case APPLE_TYPE:
                        newEntity = new Entity(APPLE_TYPE, 40, 40, null);
                        newEntity.setEntityPosition(datas[1], datas[2]);
                        items.add(newEntity);
                        break;
                    case ROCK_TYPE:
                        newEntity = new Entity(ROCK_TYPE, 40, 40, null);
                        newEntity.setEntityPosition(datas[1], datas[2]);
                        items.add(newEntity);
                        break;
                    case CHECKPOINT:
                        newEntity = new Entity(CHECKPOINT, 10, 80, null);
                        newEntity.setEntityPosition(datas[1], datas[2]);
                        items.add(newEntity);
                        break;
                    default:
                        break;
                }                
            }

            // enemies reading
            int numEnemies = Integer.parseInt(br.readLine());
            for (int i = 0; i < numEnemies; i++) {
                String line = br.readLine();
                String[] tokens = line.split(delims);
                int[] datas = new int[5];
                for (int j = 0; j < 5; j++) {
                    datas[j] = Integer.parseInt(tokens[j]);                    
                }
                switch (datas[0]) {
                    case 2001:
                        Entity newEntity = new Entity(2001, 40, 40, null);
                        newEntity.setEntityPosition(datas[1], datas[2]);
                        enemies.add(newEntity);
                        break;
                    case APPLE_TYPE:
                        break;
                    default:
                        break;
                }                
            }
            
            paintMap();            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void saveMapToFile(File file) {
        try {
            FileWriter fw = new FileWriter(file + ".map");
            BufferedWriter writer = new BufferedWriter(fw);
            
            String line = numberOfCols + "\n";
            writer.write(line);
            
            line = numberOfRows + "\n";
            writer.write(line);
            
            for (int row = 0; row < numberOfRows; row++) {
                line = "";
                for (int col = 0; col < numberOfCols; col++) {
                    line += map[col][row] + " ";
                }
                writer.write(line + "\n");
            }
            
            // write items datas
            writer.write(items.size() + "\n");
            for (Entity item : items) {
                line = "" + item.id + " " + item.x + " " + item.y + " " + item.width + " " + item.height;
                writer.write(line + "\n");
            }
            // write enemies data
            writer.write(enemies.size() + "\n");
            for (Entity enemy : enemies) {
                line = "" + enemy.id + " " + enemy.x + " " + enemy.y + " " + enemy.width + " " + enemy.height;
                writer.write(line + "\n");
            }
            
            writer.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void draw() {
        //g.dispose();
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.setColor(Color.yellow);
        g.setFont(new Font("Serif", Font.ITALIC, 9));
        for (int i = 0; i < numberOfCols; i++) {
            for (int j = 0; j < numberOfRows; j++) {
                if(map[i][j] == 0){
                    g.drawRect(i * TILESIZE, j * TILESIZE, TILESIZE, TILESIZE);
                    g.drawString(j +", " + i, i * TILESIZE + 2, j * TILESIZE + 10);
                }
                if(map[i][j] > 0){
                    int rc = map[i][j] - 1;
                    int r = rc / blockedTilesColNumber;     //int r = rc / 9;
                    int c = rc % blockedTilesColNumber;
                    
                    // betöltött file
                    //if(rc >= 0)
                    g.drawImage(blockedTiles[r][c].getImage(),
                            i * TILESIZE,
                            j * TILESIZE,
                            TILESIZE,      //WIDTH * SCALE,
                            TILESIZE,     //HEIGHT * SCALE,
                            null
                    );
                }
                if(map[i][j] < 0){
                    int rc = (-map[i][j]) - 1;
                    int r = rc / normalTilesColNumber;            //int r = rc / 13;
                    int c = rc % normalTilesColNumber;
                    
                    // betöltött file
                    g.drawImage(normalTiles[r][c].getImage(),
                            i * TILESIZE,
                            j * TILESIZE,
                            TILESIZE,      //WIDTH * SCALE,
                            TILESIZE,     //HEIGHT * SCALE,
                            null
                    );
                }
            }
        }
        for (Entity item : items) {            
            g.drawImage(item.getImage(),
                    item.x - item.width / 2,
                    item.y - item.height / 2,
                    item.width,      //WIDTH * SCALE,
                    item.height,     //HEIGHT * SCALE,
                    null
            );            
        }
        
        for (Entity enemy : enemies) {
            g.drawImage(enemy.getImage(),
                    enemy.x - enemy.width / 2,
                    enemy.y - enemy.height / 2,
                    enemy.width,      //WIDTH * SCALE,
                    enemy.height,     //HEIGHT * SCALE,
                    null
            );
        }
        
        if(currentImage != null){
            g.drawImage(currentImage,
                    currentImageX,
                    currentImageY,
                    TILESIZE,      //WIDTH * SCALE,
                    TILESIZE,     //HEIGHT * SCALE,
                    null
            );
        }
    }    
    
    private void drawToScreen(){
        Graphics2D g2d = (Graphics2D) getGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.drawImage(image, 0, 0,
                WIDTH * SCALE,      //WIDTH * SCALE,
                HEIGHT * SCALE,     //HEIGHT * SCALE,
                null
        );
        g2d.dispose();
    }
}
