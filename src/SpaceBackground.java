
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;

public class SpaceBackground extends BackgroundObject {

    List<Star> stars;

    public SpaceBackground(int x, int y, int width, int height) {
        super(x, y, width, height);

        stars = Star.batchGenerate(new Rectangle(width, height), 200);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(this.getXPixel(), this.getYPixel(), this.getWidth(), this.getHeight());

        for (Star s : stars) {
            s.draw(g);
        }
    }

}
