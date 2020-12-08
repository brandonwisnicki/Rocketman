
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Silver extends Item {

    private static BufferedImage silverSprite;
    private static final String SILVER_SPRITE_FILE = "files/items/silver.png";

    public Silver(int amount) {
        super(amount);

        try {
            if (silverSprite == null) {
                silverSprite = ImageIO.read(new File(SILVER_SPRITE_FILE));
            }

        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    }

    @Override
    public String getName() {

        return "Silver Ore";
    }

    @Override
    public BufferedImage getSprite() {
        return silverSprite;
    }

    @Override
    public String getTechnicalName() {
        return "SILVER";
    }

}
