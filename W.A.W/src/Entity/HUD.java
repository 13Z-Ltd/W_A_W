package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/**
 *
 * @author Balla
 */
public class HUD {
    
    private Worm worm;
    BufferedImage appleImage;
    BufferedImage grayAppleImage;
    
    public HUD(Worm w) {
        worm = w;
        try {
            appleImage = ImageIO.read(
                    getClass().getResourceAsStream("/Sprites/apple.png")
            );
            grayAppleImage = ImageIO.read(
                    getClass().getResourceAsStream("/Sprites/grayapple.png")
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void draw(Graphics2D g) {
        int j = worm.getHealth();
        for (int i = 0; i < worm.getMaxHealth(); i++) {
            if(i < j){
                g.drawImage(appleImage, 10 + i * 20, 10, null);
            } else {
                g.drawImage(grayAppleImage, 10 + i * 20, 10, null);
            }
        }
    }
}
