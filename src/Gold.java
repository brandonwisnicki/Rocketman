
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Gold extends Item {

    private static BufferedImage goldSprite;
    private static final String GOLD_SPRITE_FILE = "files/items/gold.png";

    public Gold(int amount) {
        super(amount);

        try {
            if (goldSprite == null) {
                goldSprite = ImageIO.read(new File(GOLD_SPRITE_FILE));
            }

        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    }

    @Override
    public String getName() {

        return "Gold Ore";
    }

    @Override
    public BufferedImage getSprite() {
        return goldSprite;
    }

    @Override
    public String getTechnicalName() {
        return "GOLD";
    }

}
