package waw_mapeditor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Balla
 */
public class OptionsPanel extends JFrame {    
    
    KeyListener drawPanelKeyListener;
    ActionListener buttonPressListener;
    ActionListener menuActionListener;
    MouseMotionListener motionListener;
    MouseListener mouseListener;
    MouseListener tilesPanel1_MouseListener;
    MouseListener tilesPanel2_MouseListener;
    MouseListener itemsPanel_MouseListener;
    
    MapEditor mapEditor;
    TilesPanel tilesPanel;
    TilesPanel normalTilesPanel;
    EntitiesPanel itemsEnemiesPanel;    
    
    public ArrayList<Entity> entities;
    public ArrayList<Entity> itemsList;
    public ArrayList<Entity> enemiesList;
    public BufferedImage tileset;
    public Tile[][] blockedTiles;
    public Tile[][] normalTiles;
    public int TILESIZE = 40;
    public int GLOBALSCALE = 2;
    public int numTilesAcross;
    public boolean shiftPressed;
    
    public int selectedTileNumber = 1;
    public BufferedImage currentItemImage;
    public Entity currentEntity;
        
    JButton btnNewMap;
    JTextField rowNumTextArea;
    JTextField colNumTextArea;
    
    JMenuItem newMenuItem;
    JMenuItem loadMenuItem;
    JMenuItem saveMenuItem;
    JMenuItem exitMenuItem;
    
    public BufferedImage branchImage;
    public BufferedImage appleImage;
    public BufferedImage rockImage;
    public BufferedImage checkpointImage;    
    public BufferedImage hedgehogImage;
    
    public static final int BRANCH_TYPE = 1001;
    public static final int APPLE_TYPE = 1002;
    public static final int ROCK_TYPE = 1003;    
    public static final int CHECKPOINT = 1004;
    
    public OptionsPanel() {
        super("WAW Map Editor");
        setBackground(Color.WHITE);        
        setPreferredSize(new Dimension(1200, 1300));
        
        loadTileset("/Tilesets/forresttileset_40.png");        
        tilesPanel = new TilesPanel(40, GLOBALSCALE, blockedTiles, 2);
        tilesPanel.setVisible(true);
        
        loadNormalTileset("/Tilesets/forresttileset2_40.png");
        normalTilesPanel = new TilesPanel(40, GLOBALSCALE, normalTiles, 1);
        normalTilesPanel.setVisible(true);
        
        currentItemImage = blockedTiles[0][0].getImage();
        loadEntities();
        itemsEnemiesPanel = new EntitiesPanel(entities);
        
        itemsList = new ArrayList<Entity>();
        enemiesList = new ArrayList<Entity>();
        
        mapEditor = new MapEditor(30, 9, TILESIZE, GLOBALSCALE, blockedTiles, normalTiles, itemsList, enemiesList);
        
        initPanel();        
    }
        
    public void loadTileset(String s) {
        try {
            tileset = ImageIO.read(getClass().getResourceAsStream(s));
            
            numTilesAcross = tileset.getWidth() / TILESIZE;
            blockedTiles = new Tile[2][numTilesAcross];
            
            BufferedImage subimage;
            for (int col = 0; col < numTilesAcross; col++) {
                subimage = tileset.getSubimage(
                        col * TILESIZE,
                        0,
                        TILESIZE,
                        TILESIZE
                );
                blockedTiles[0][col] = new Tile(subimage, Tile.BLOCKED);
                subimage = tileset.getSubimage(
                        col * TILESIZE,
                        TILESIZE,
                        TILESIZE,
                        TILESIZE
                );
                //Water tile setting
                if (col > 6) {
                    blockedTiles[1][col] = new Tile(subimage, Tile.WATER);
                } else {
                    blockedTiles[1][col] = new Tile(subimage, Tile.BLOCKED);
                }
            }            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void loadNormalTileset(String s) {
        try {
            tileset = ImageIO.read(getClass().getResourceAsStream(s));
            
            numTilesAcross = tileset.getWidth() / TILESIZE;
            normalTiles = new Tile[1][numTilesAcross];
            
            BufferedImage subimage;
            for (int col = 0; col < numTilesAcross; col++) {
                subimage = tileset.getSubimage(
                        col * TILESIZE,
                        0,
                        TILESIZE,
                        TILESIZE
                );
                normalTiles[0][col] = new Tile(subimage, Tile.NORMAL);
            }            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void loadEntities(){
        entities = new ArrayList<Entity>();
        try {            
            appleImage = ImageIO.read(
                    getClass().getResourceAsStream("/EntitesIcons/apple.png")
            );
            Entity newEntity = new Entity(APPLE_TYPE, 40, 40, appleImage);
            entities.add(newEntity);
            
            branchImage = ImageIO.read(getClass().getResourceAsStream("/EntitesIcons/treebranch30.png"));
            newEntity = new Entity(BRANCH_TYPE, 30, 7, branchImage);
            entities.add(newEntity);
            
            rockImage = ImageIO.read(getClass().getResourceAsStream("/EntitesIcons/rock.png"));
            newEntity = new Entity(ROCK_TYPE, 40, 40, rockImage);
            entities.add(newEntity);
            
            //enemies
            hedgehogImage = ImageIO.read(getClass().getResourceAsStream("/EntitesIcons/hedgehog.png"));
            newEntity = new Entity(2001, 40, 40, hedgehogImage);
            entities.add(newEntity);         
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void loadMapsFromFile() {
        JFileChooser fileChooser = new JFileChooser("Resources/Maps");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("WAW Maps", "map");
        fileChooser.addChoosableFileFilter(filter);
        fileChooser.setFileFilter(filter);
        int returnValue = fileChooser.showOpenDialog(null);
        
        if(returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            mapEditor.loadMapFromFile(file);
            // load images
            for (Entity item : itemsList) {
                switch (item.id) {
                    case BRANCH_TYPE:
                        item.image = branchImage;
                        break;
                    case APPLE_TYPE:
                        item.image = appleImage;
                        break;
                    case ROCK_TYPE:
                        item.image = rockImage;
                        break;
                    case CHECKPOINT:
                        item.image = checkpointImage;
                        break;
                    default:
                        break;
                }
            }
            for (Entity enemy : enemiesList) {
                switch (enemy.id) {
                    case 2001:
                        enemy.image = hedgehogImage;
                        break;                    
                    default:
                        break;
                }
            }
        }
        mapEditor.repaint();
    }
    
    public void saveMapToFile() {
        JFileChooser fileChooser = new JFileChooser("Resources/Maps"); //Win
        FileNameExtensionFilter filter = new FileNameExtensionFilter("WAW Maps", "map");
        fileChooser.addChoosableFileFilter(filter);
        fileChooser.setFileFilter(filter);
        int returnValue = fileChooser.showSaveDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            mapEditor.saveMapToFile(file);
        } 
    }
    
    public int tryParse(String text) {
        try {
            int insertNumber = Integer.parseInt(text);            
                return insertNumber;
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    private void initPanel() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Color.white);        
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        try {
            ImageIcon icon = new ImageIcon("Resources/MenuIcons/new.png");
            newMenuItem = new JMenuItem("New", icon);
            icon = new ImageIcon("Resources/MenuIcons/load.png");
            loadMenuItem = new JMenuItem("Load", icon);
            icon = new ImageIcon("Resources/MenuIcons/save.png");
            saveMenuItem = new JMenuItem("Save", icon);
            icon = new ImageIcon("Resources/MenuIcons/exit.png");
            exitMenuItem = new JMenuItem("Exit", icon);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        newMenuItem.setName("New");
        newMenuItem.setMnemonic(KeyEvent.VK_N);
        newMenuItem.setToolTipText("Create a new empty map");
        
        loadMenuItem.setName("Load");
        loadMenuItem.setMnemonic(KeyEvent.VK_L);
        loadMenuItem.setToolTipText("Load a map");
        
        saveMenuItem.setName("Save");
        saveMenuItem.setMnemonic(KeyEvent.VK_S);
        saveMenuItem.setToolTipText("Save this map");
        
        exitMenuItem.setName("Exit");
        exitMenuItem.setMnemonic(KeyEvent.VK_E);
        exitMenuItem.setToolTipText("Exit application");
        
        fileMenu.add(newMenuItem);
        fileMenu.add(loadMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
        
        rowNumTextArea = new JTextField();
        rowNumTextArea.setText(Integer.toString(mapEditor.numberOfRows));
        rowNumTextArea.setPreferredSize(new Dimension(50,30));
        rowNumTextArea.setHorizontalAlignment(JTextField.RIGHT);
        //rowNumTextArea.setFont(new Font("SansSerif", Font.BOLD, 16));
        colNumTextArea = new JTextField();
        colNumTextArea.setText(Integer.toString(mapEditor.numberOfCols));
        colNumTextArea.setPreferredSize(new Dimension(50,30));
        colNumTextArea.setHorizontalAlignment(JTextField.RIGHT);
        
        JPanel controllersPanel = new JPanel();
        controllersPanel.setPreferredSize(new Dimension(1200, 60));
        controllersPanel.add(new JLabel("Number of raws: "));
        controllersPanel.add(rowNumTextArea);
        controllersPanel.add(new JLabel("Number of columns: "));
        controllersPanel.add(colNumTextArea);
        btnNewMap = new JButton("Change Map Size");
        btnNewMap.setName("Change Map Size");
        btnNewMap.setFocusable(false);
        controllersPanel.add(btnNewMap);
        
        JScrollPane mapEditorScrollPane = new JScrollPane(mapEditor);
        mapEditorScrollPane.setPreferredSize(new Dimension(1024, 760));
        mapEditorScrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent ae) {
                mapEditor.paintMap();
            }
        });
        mapEditorScrollPane.getHorizontalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent ae) {
                mapEditor.paintMap();
            }
        });
        
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));
        JPanel borderedPanel = new JPanel();
        borderedPanel.setBorder(new TitledBorder(BorderFactory.createLoweredSoftBevelBorder(), "Items/Enemies"));
        borderedPanel.add(itemsEnemiesPanel);
        southPanel.add(borderedPanel);
        
        borderedPanel = new JPanel();
        borderedPanel.setBorder(new TitledBorder(BorderFactory.createLoweredSoftBevelBorder(), "Blocked Tiles"));
        borderedPanel.add(tilesPanel);        
        southPanel.add(borderedPanel);
        borderedPanel = new JPanel();
        borderedPanel.setBorder(new TitledBorder(BorderFactory.createLoweredSoftBevelBorder(), "Normal Tiles"));
        borderedPanel.add(normalTilesPanel);        
        southPanel.add(borderedPanel);
        southPanel.add(controllersPanel);
        
        JScrollPane southScrollPane = new JScrollPane(southPanel);
        mapEditorScrollPane.setPreferredSize(new Dimension(1024, 400));
        mapEditorScrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent ae) { }
        });
        
        drawPanelKeyListener = new KeyListener() {           
            @Override
            public void keyTyped(KeyEvent ke) {

            }

            @Override
            public void keyPressed(KeyEvent ke) {
                if(ke.getKeyCode() == KeyEvent.VK_SHIFT) {
                    shiftPressed = true;
                }
            }

            @Override
            public void keyReleased(KeyEvent ke) {
                if(ke.getKeyCode() == KeyEvent.VK_SHIFT) {
                    shiftPressed = false;
                }
            }
        };
        
        
        add(mapEditorScrollPane, BorderLayout.CENTER);
        add(southScrollPane, BorderLayout.SOUTH);
        //add(southPanel, BorderLayout.SOUTH);
        pack();
        
        buttonPressListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                JButton srcButton = (JButton)ae.getSource();
                
                 switch (srcButton.getName()){
                    case "Change Map Size":
                        //System.out.println("New Map");
                        int col = tryParse(colNumTextArea.getText());
                        int row = tryParse(rowNumTextArea.getText());                        
                        //mapEditor.newMap(col, row);
                        mapEditor.changeMapSize(col, row);
                        break;
                    default:
                        break;
                 }
            }
        };
        
        menuActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JMenuItem menuItem = (JMenuItem) e.getSource();
                
                switch (menuItem.getName()) {
                    case "New":
                        System.out.println("New Map");
                        int col = tryParse(colNumTextArea.getText());
                        int row = tryParse(rowNumTextArea.getText());                        
                        mapEditor.newMap(col, row);
                        break;
                    case "Load":
                        loadMapsFromFile();
                        break;
                    case "Save":
                        saveMapToFile();
                        break;
                    case "Exit":
                        System.exit(0);
                        break;
                    default:
                        break;
                }
            }
        };
        
        mouseListener = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {}
            
            @Override
            public void mousePressed(MouseEvent me) {
                if(me.getButton() == MouseEvent.BUTTON1){
                    int currentCol = me.getX() / (TILESIZE * GLOBALSCALE);
                    int currentRow = me.getY() / (TILESIZE * GLOBALSCALE);
                    // add entites
                    if(selectedTileNumber > 2000) {
                        System.out.println(".selectedTileNumber: " + selectedTileNumber);
                        Entity enemy = new Entity(currentEntity);
                        //enemy.setEntityPosition((me.getX() - enemy.width) / 2, (me.getY() - enemy.height) / 2);
                        enemy.setEntityPosition((me.getX()) / 2, (me.getY()) / 2);
                        enemiesList.add(enemy);
                    }else if(selectedTileNumber > 1000) {
                        Entity item = new Entity(currentEntity);
                        item.setEntityPosition((me.getX()) / 2, (me.getY()) / 2);
                        
                        //delete item if on it
                        Rectangle r = new Rectangle(me.getX() / 2, me.getY() / 2, 5 ,5);
                        Entity testedItem = null;
                        for (Entity ti : itemsList) {
                            if(ti.getShowedRectangle().intersects(r)){
                                System.out.println("deleted!!");
                                testedItem = ti;
                            }
                        }
                        if(testedItem != null)
                            itemsList.remove(testedItem);
                        else
                            itemsList.add(item);
                    }else {
                        if(shiftPressed){
                            mapEditor.addLineToMap(currentRow, selectedTileNumber);
                        } else {
                            mapEditor.addTileToMap(currentCol, currentRow, selectedTileNumber);
                        }
                    }
                    mapEditor.paintMap();
                }
                if(me.getButton() == MouseEvent.BUTTON3){
                    int currentCol = me.getX() / (TILESIZE * GLOBALSCALE);
                    int currentRow = me.getY() / (TILESIZE * GLOBALSCALE);
                    System.out.println("delete col: " + currentCol + ", row: " + currentRow);
                    mapEditor.addTileToMap(currentCol, currentRow, 0);
                    mapEditor.paintMap();
                }
            }
            @Override
            public void mouseReleased(MouseEvent me) {}
            @Override
            public void mouseEntered(MouseEvent me) {
                mapEditor.setCurrentImage(currentItemImage);
            }
            @Override
            public void mouseExited(MouseEvent me) {
                mapEditor.setCurrentImage(null);
                mapEditor.paintMap();
            }
        };
        
        motionListener = new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent me) {}

            @Override
            public void mouseMoved(MouseEvent me) {
                mapEditor.setCurrentImagePosition(me.getX(), me.getY());
                mapEditor.paintMap();
            }
        };
        
        tilesPanel1_MouseListener = new MouseListener() {
            @Override
            public void mousePressed(MouseEvent me) {
                int currentCol = me.getX() / (TILESIZE * GLOBALSCALE);
                int currentRow = me.getY() / (TILESIZE * GLOBALSCALE);
                if(tilesPanel.setSelectedTileNumber(currentCol, currentRow) == 1){
                    selectedTileNumber = tilesPanel.getSelectedTileNumber();                
                    currentItemImage = blockedTiles[currentRow][currentCol].getImage();
                    // entity to null
                    currentEntity = null;
                }
            }

            @Override
            public void mouseReleased(MouseEvent me) {}
            @Override
            public void mouseClicked(MouseEvent me) {}
            @Override
            public void mouseEntered(MouseEvent me) {}                
            @Override
            public void mouseExited(MouseEvent me) {}
        };
        
        tilesPanel2_MouseListener = new MouseListener() {
           @Override
            public void mousePressed(MouseEvent me) {
                int currentCol = me.getX() / (TILESIZE * GLOBALSCALE);
                int currentRow = me.getY() / (TILESIZE * GLOBALSCALE);
                if(normalTilesPanel.setSelectedTileNumber(currentCol, currentRow) == 1) {
                    selectedTileNumber = -normalTilesPanel.getSelectedTileNumber();
                    currentItemImage = normalTiles[currentRow][currentCol].getImage();
                    // entity to null
                    currentEntity = null;
                }            
            }

            @Override
            public void mouseReleased(MouseEvent me) {}
            @Override
            public void mouseClicked(MouseEvent me) {}
            @Override
            public void mouseEntered(MouseEvent me) {}                
            @Override
            public void mouseExited(MouseEvent me) {}
        };
        
        itemsPanel_MouseListener = new MouseListener() {            
            @Override
            public void mousePressed(MouseEvent me) {
                int index = itemsEnemiesPanel.getItemNumber(me.getX(), me.getY());
                if(index >= 0) {
                    currentEntity = entities.get(index);
                    currentItemImage = currentEntity.getImage();
                    selectedTileNumber = currentEntity.getID();
                }
            }
            
            @Override
            public void mouseClicked(MouseEvent me) {}
            @Override
            public void mouseReleased(MouseEvent me) {}
            @Override
            public void mouseEntered(MouseEvent me) {}
            @Override
            public void mouseExited(MouseEvent me) {}
        };        
        
        mapEditor.addKeyListener(drawPanelKeyListener);
        newMenuItem.addActionListener(menuActionListener);
        loadMenuItem.addActionListener(menuActionListener);
        saveMenuItem.addActionListener(menuActionListener);
        exitMenuItem.addActionListener(menuActionListener);
        btnNewMap.addActionListener(buttonPressListener);
        mapEditor.addMouseMotionListener(motionListener);
        mapEditor.addMouseListener(mouseListener);
        tilesPanel.addMouseListener(tilesPanel1_MouseListener);
        normalTilesPanel.addMouseListener(tilesPanel2_MouseListener);
        itemsEnemiesPanel.addMouseListener(itemsPanel_MouseListener);
    }
}
