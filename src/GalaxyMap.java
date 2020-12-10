import java.awt.Color;
import java.awt.Graphics;

/**
 * A user interface that displays a map of all systems in the current game
 * Shows if the system is discovered and if it is discovered, colored
 * based on the type of system (brown for asteroid, green for outpost,
 * purple for nubula, and white for empty)
 * 
 * The current system blinks on screen
 */

public class GalaxyMap extends UserInterface {

    private GameCourt court;

    //Step helps calculate if the blinking rect should be on or off
    private int step;

    /***
     * Constructor
     */
    public GalaxyMap(int px, int py, GameCourt court) {
        super(px, py, 250, 190);

        this.court = court;
        step = 0;
    }

    
    /***
     * Draws grid of systems on screen
     */
    @Override
    public void draw(Graphics g) {

        this.drawBorder(g);

        int currX = this.getXPixel() + 11;
        int currY = this.getYPixel() + 30;

        g.drawString("Galaxy Map", this.getXPixel() + 10, this.getYPixel()  + 20);

        step++;

        for (PlanetarySystem[] row : court.getGalaxy()) {
            for (PlanetarySystem sys : row) {
                
                if (sys.isDiscovered() && sys instanceof AsteroidBeltSystem) {
                    g.setColor(new Color(160, 82, 45));
                } else if (sys.isDiscovered() && sys instanceof NebulaSystem) {
                    g.setColor(new Color(128, 0 ,128));
                } else if (sys.isDiscovered() && sys instanceof OutpostSystem) {
                    g.setColor(Color.GREEN);
                } else {
                    g.setColor(Color.WHITE);
                }
                
                if (step > 75 && sys.equals(court.getCurrentSystem())) {
                    
                    
                    
                    g.fillRect(currX, currY, 18, 10);

                    if (step > 150) {
                        step = 0;
                    }

                } else {

                    g.drawRect(currX, currY, 18, 10);
                }

                if (!sys.isDiscovered()) {
                    g.drawString("?", currX + 5, currY + 9);
                }

                currX += 23;
            }
            currX = this.getXPixel() + 11;
            currY += 15;
        }

    }

}
