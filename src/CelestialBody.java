import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class CelestialBody extends BackgroundObject {

    public static final String RED_PLANET_FILE = "files/redplanet.png";
    public static final String BLUE_PLANET_FILE = "files/blueplanet.png";
    public static final String SUN_FILE = "files/sun.png";

    private static BufferedImage redPlanetSprite;
    private static BufferedImage bluePlanetSprite;
    private static BufferedImage sunSprite;
    private BufferedImage activeSprite;

    public CelestialBody(int x, int y, CelestialBodyType type) {
        super(x, y, 300, 300);
        try {
            if (redPlanetSprite == null) {
                redPlanetSprite = ImageIO.read(new File(RED_PLANET_FILE));
            }
            if (bluePlanetSprite == null) {
                bluePlanetSprite = ImageIO.read(new File(BLUE_PLANET_FILE));
            }
            if (sunSprite == null) {
                sunSprite = ImageIO.read(new File(SUN_FILE));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }

        switch (type) {
            case RED_PLANET:
                activeSprite = redPlanetSprite;
                break;
            case BLUE_PLANET:
                activeSprite = bluePlanetSprite;
                break;
            case SUN:
                activeSprite = sunSprite;
                break;
            default:
                activeSprite = bluePlanetSprite;
                break;
        }

    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(activeSprite, this.getXPixel(), this.getYPixel(), this.getWidth(),
                this.getHeight(), null);

    }

    //Returns a random celestial body type
    public static CelestialBodyType randomBody() {

        int choice = (int) (Math.random() * CelestialBodyType.values().length);

        return CelestialBodyType.values()[choice];

    }

}
