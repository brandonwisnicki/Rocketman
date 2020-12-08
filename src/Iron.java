
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Iron extends Item {

    private static BufferedImage ironSprite;
    private static final String IRON_SPRITE_FILE = "files/items/iron.png";

    public Iron(int amount) {
        super(amount);

        try {
            if (ironSprite == null) {
                ironSprite = ImageIO.read(new File(IRON_SPRITE_FILE));
            }

        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    }

    @Override
    public String getName() {

        return "Iron Ore";
    }

    
    
    @Override
    public BufferedImage getSprite() {
        return ironSprite;
    }

    @Override
    public String getTechnicalName() {
        return "IRON";
    }

}
