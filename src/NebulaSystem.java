import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class NebulaSystem extends PlanetarySystem {

    private static final String NEBULA_OVERLAY_FILE = "files/nebula.png";
    private static BufferedImage nebulaOverlay;

    public NebulaSystem(String name, GameCourt court) {
        super(name, court);

        try {
            if (nebulaOverlay == null) {
                nebulaOverlay = ImageIO.read(new File(NEBULA_OVERLAY_FILE));
            }

        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    }

    public NebulaSystem(String name, boolean discovered, GameCourt court) {
        super(name, court);

        try {
            if (nebulaOverlay == null) {
                nebulaOverlay = ImageIO.read(new File(NEBULA_OVERLAY_FILE));
            }

        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }

        if (discovered) {
            silentDiscovery();
        }

    }

    public void setupScene() {
        super.setupScene();

        Asteroid asteroid = new Asteroid(1100, 250, this.getCourt().getCourtWidth(),
                this.getCourt().getCourtHeight(), 1f, 0.02f, (float) Math.PI, this.getCourt());

        this.getControllers().add(new Spawner(asteroid, 1000, this));
    }

    public void drawOverlay(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(nebulaOverlay, 0, 0, (int) (this.getCourt().getCourtWidth()),
                (int) (this.getCourt().getCourtHeight()), null);

    }

}
