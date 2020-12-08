import java.awt.Color;
import java.awt.Graphics;

public class GalaxyMap {

    int px;
    int py;
    Color bgColor;
    Color borderColor;
    Color textColor;

    private GameCourt court;

    private int step;

    public GalaxyMap(int px, int py, GameCourt court) {

        bgColor = Color.DARK_GRAY;
        borderColor = Color.WHITE;
        textColor = Color.WHITE;
        this.px = px;
        this.py = py;
        this.court = court;
        step = 0;
    }

    public void draw(Graphics g) {

        g.setColor(borderColor);
        g.drawRect(px, py, 250, 190);
        g.drawRect(px + 5, py + 5, 240, 180);

        int currX = px + 11;
        int currY = py + 30;

        g.drawString("Galaxy Map", px + 10, py + 20);

        step++;

        for (PlanetarySystem[] row : court.getGalaxy()) {
            for (PlanetarySystem sys : row) {
                if (step > 75 && sys.equals(court.getCurrentSystem())) {
                    g.fillRect(currX, currY, 18, 10);

                    if (step > 150) {
                        step = 0;
                    }

                } else {

                    if (sys.isDiscovered() && sys instanceof AsteroidBeltSystem) {
                        g.setColor(new Color(160, 82, 45));
                    } else if (sys.isDiscovered() && sys instanceof NebulaSystem) {
                        g.setColor(new Color(128, 0 ,128));
                    } else if (sys.isDiscovered() && sys instanceof OutpostSystem) {
                        g.setColor(Color.GREEN);
                    } else {
                        g.setColor(Color.WHITE);
                    }

                    g.drawRect(currX, currY, 18, 10);
                }

                if (!sys.isDiscovered()) {
                    g.drawString("?", currX + 5, currY + 9);
                }

                currX += 23;
            }
            currX = px + 11;
            currY += 15;
        }

    }

}
