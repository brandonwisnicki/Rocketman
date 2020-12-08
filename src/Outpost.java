import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

import javax.imageio.ImageIO;

public class Outpost extends Entity {

    public static final String STATION_SPRITE_FILE = "files/station.png";
    private static BufferedImage stationSprite;

    private Shop systemShop;

    private GameCourt court;

    private boolean canLand;

    public Outpost(int px, int py, GameCourt court) {
        super(0, 0, px, py, 200, 200, court.getWidth(), court.getHeight(), 0, .005f, 0, 0, 0, 0);
        try {
            if (stationSprite == null) {
                stationSprite = ImageIO.read(new File(STATION_SPRITE_FILE));
            }

        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
        systemShop = new Shop(court);
        this.court = court;
        canLand = false;
    }

    public Outpost(int px, int py, GameCourt court, int ironValue, int silverValue, int goldValue,
            int ironSupply, int silverSupply, int goldSupply, int credits, int costPerHP,
            int costPerGallon, int hullUpgradeCreditCost, int hullUpgradeIronCost,
            int fuelUpgradeCreditCost, int fuelUpgradeIronCost, int powerUpgradeCreditCost,
            int powerUpgradeGoldCost) {
        super(0, 0, px, py, 200, 200, court.getWidth(), court.getHeight(), 0, .005f, 0, 0, 0, 0);
        try {
            if (stationSprite == null) {
                stationSprite = ImageIO.read(new File(STATION_SPRITE_FILE));
            }

        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
        systemShop = new Shop(court, ironValue, silverValue, goldValue, ironSupply, silverSupply,
                goldSupply, credits, costPerHP, costPerGallon, hullUpgradeCreditCost,
                hullUpgradeIronCost, fuelUpgradeCreditCost, fuelUpgradeIronCost,
                powerUpgradeCreditCost, powerUpgradeGoldCost);
        this.court = court;
        canLand = false;

    }

    public Shop getShop() {
        return systemShop;
    }

    @Override
    public Entity deepCopy() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void handleHitWall() {
        // TODO Auto-generated method stub

    }

    @Override
    public void update() {
        // TODO Auto-generated method stub

    }

    @Override
    public Collection<Entity> getSelfAndChildren() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void checkCollisions() {
        for (Entity e : this.court.getAllEntities()) {
            if (e instanceof Ship) {

                if (this.intersects(e)) {
                    canLand = true;

                } else {
                    canLand = false;

                }

            }
        }

    }

    public boolean isAccessible() {
        return canLand;
    }

    @Override
    public boolean shouldDispose() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform at = AffineTransform.getRotateInstance(
                this.getRotationalAngle() + Math.PI / 2,
                (this.getXPixel() + this.getXPixel() + this.getWidth()) / 2,
                (this.getYPixel() + this.getYPixel() + this.getHeight()) / 2);
        at.concatenate(AffineTransform.getTranslateInstance(this.getXPixel(), this.getYPixel()));
        at.concatenate(AffineTransform.getScaleInstance(this.getHeight() / 100.0f,
                this.getWidth() / 100.0f));
        g2d.drawImage(stationSprite, at, null);

    }

}
