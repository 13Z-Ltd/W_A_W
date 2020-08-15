/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PanelGraphics;

import Entity.Enemies.Enemy;
import Entity.Enemies.Hedgehog;
import Entity.Items.Apple;
import Entity.Items.Branch;
import Entity.Items.Item;
import Entity.Items.Rock;
import Main.GamePanel;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 *
 * @author Balla
 */
public class TileMap {
    
    //position
    private double x;
    private double y;
    
    // bounds -- Határok
    private int xmin;
    private int ymin;
    private int xmax;
    private int ymax;
    
    private double tween;
    
    // map
    private int[][] map;
    private int tileSize;
    private int numRows;
    private int numCols;
    private int width;
    private int height;
    
    // tileset
    private BufferedImage tileset;
    private int numTilesAcross; //tilsetben vizszintesen lévő csempék száma
    private Tile[][] tiles;
    private Tile[] tilesNORMAL;
    
    // drawing
    private int rowOffset;
    private int colOffset;
    private int numRowsToDraw;
    private int numColsToDraw;
    
    
    public TileMap(int tileSize) {
        this.tileSize = tileSize;
        numRowsToDraw = GamePanel.HEIGHT / tileSize + 2;
        numColsToDraw = GamePanel.WIDTH / tileSize + 2;
        tween = 0.07;
    }
    
    public void loadTileset(String s) {
        try {
            tileset = ImageIO.read(getClass().getResourceAsStream(s));
            
            numTilesAcross = tileset.getWidth() / tileSize;
            tiles = new Tile[2][numTilesAcross];
            
            BufferedImage subimage;
            for (int col = 0; col < numTilesAcross; col++) {
                subimage = tileset.getSubimage(col * tileSize,
                        0,
                        tileSize,
                        tileSize
                );
                tiles[0][col] = new Tile(subimage, Tile.BLOCKED);
                subimage = tileset.getSubimage(col * tileSize,
                        tileSize,
                        tileSize,
                        tileSize
                );
                //Water tile setting
                if (col > 6) {
                    tiles[1][col] = new Tile(subimage, Tile.WATER);
                } else {
                    tiles[1][col] = new Tile(subimage, Tile.BLOCKED);
                }
                //tiles[1][col] = new Tile(subimage, Tile.Normal);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void loadTilesetNORMAL(String s) {
        try {
            tileset = ImageIO.read(getClass().getResourceAsStream(s));
            
            numTilesAcross = tileset.getWidth() / tileSize;
            tilesNORMAL = new Tile[numTilesAcross];
            
            BufferedImage subimage;
            for (int col = 0; col < numTilesAcross; col++) {
                subimage = tileset.getSubimage(col * tileSize,
                        0,
                        tileSize,
                        tileSize
                );
                tilesNORMAL[col] = new Tile(subimage, Tile.NORMAL);

            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void loadMap(String s,  ArrayList<Item> items, ArrayList<Enemy> enemies) {
        try {
            InputStream in = getClass().getResourceAsStream(s);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            
            numCols = Integer.parseInt(br.readLine());
            numRows = Integer.parseInt(br.readLine());
            map = new int[numRows][numCols];
            width = numCols * tileSize;
            height = numRows * tileSize;
            
            xmin = GamePanel.WIDTH - width;     //a képernyő szélessége - a térkép szélessége ennyire lehet balra tolni
            xmax = 0;
            ymin = GamePanel.HEIGHT - height;
            ymax = 0;
            
            String delims = "\\s+";
            for (int row = 0; row < numRows; row++) {
                String line = br.readLine();
                String[] tokens = line.split(delims);
                for (int col = 0; col < numCols; col++) {
                    map[row][col] = Integer.parseInt(tokens[col]);
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
                    case Item.BRANCH_TYPE:
                        Branch branch = new Branch(this, i);
                        branch.setPosition(datas[1], datas[2]);              // 30 * 40
                        items.add(branch);
                        break;
                    case Item.APPLE_TYPE:
                        Apple apple = new Apple(this, i);
                        apple.setPosition(datas[1], datas[2]);              // 30 * 40
                        items.add(apple);
                        break;
                    case Item.ROCK_TYPE:
                        Rock rock = new Rock(this, i);
                        rock.setPosition(datas[1], datas[2]);              // 30 * 40
                        items.add(rock);
                        break;
                    case Item.CHECKPOINT:
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
                        Hedgehog hedgehog = new Hedgehog(this);
                        hedgehog.setPosition(datas[1], datas[2]);              // 30 * 40
                        enemies.add(hedgehog);
                        break;
                    case Item.APPLE_TYPE:
                        break;
                    default:
                        break;
                }                
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public int getTileSize() { return tileSize; }
    public double getX() { return x; }
    public double getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; } 
    
    public int getType(int row, int col) {
        int rc = map[row][col];
        
        //if(rc == -1) return Tile.NORMAL;
        if(rc == 0) return Tile.NORMAL;
        
        // NORMAL exception
        if(rc < 0) return Tile.NORMAL;
        
        //int r = rc / 10;
        //int c = (rc % 10) ;
        int r = rc / 9;
        int c = (rc % 9) ;
        return tiles[r][c].getType();
    }
    
    public void setTween(double tween) {
        this.tween = tween;
    }
    
    public void setPosition(double x, double y){
        this.x += (x - this.x) * tween;
        this.y += (y - this.y) * tween;
        
        fixBounds();
        
        colOffset = (int)-this.x / tileSize;
        rowOffset = (int)-this.y / tileSize;
    }
    
    private void fixBounds() {
        if(x < xmin) x = xmin;
        if(y < ymin) y = ymin;
        if(x > xmax) x = xmax;
        if(y > ymax) y = ymax;
    }
    
    
    public void draw(Graphics2D g) {
        for (int row = rowOffset; row < rowOffset + numRowsToDraw; row++) {
            if (row >= numRows) break;            
            for (int col = colOffset; col < colOffset + numColsToDraw; col++) {
                if(col >= numCols) break;
                
                if(map[row][col] == 0) continue;
                //if(map[row][col] == -1) continue;
                
                int rc = map[row][col] - 1;
                //int r = rc / 10;
                //int c = (rc % 10) ;
                int r = rc / 9;
                int c = (rc % 9) ;
                
                //if(rc > -1) {
                if(rc >= 0) {
                    g.drawImage(
                        tiles[r][c].getImage(),
                        (int)x + col * tileSize,
                        (int)y + row * tileSize,
                        null
                    );
                } else {
                    g.drawImage(
                            //int rc = (-map[i][j]) - 1;
                            //tilesNORMAL[-rc].getImage(),
                        tilesNORMAL[-rc].getImage(),
                        (int)x + col * tileSize,
                        (int)y + row * tileSize,
                        null
                    );
                }
                /*
                if(rc >= 0) {
                //if(tiles[r][c].getType() == Tile.BLOCKED) {
                    g.setColor(Color.RED);
                    //g.drawRect(col * tileSize, row * tileSize, tileSize, tileSize);
                    g.drawRect(
                            (int)x + col * tileSize,
                            (int)y + row * tileSize,
                            tileSize, tileSize);
                    
                }
                */
            }
        }        
    }
}
