import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.*;

public class Star extends BackgroundObject {

    float brightness;
    Color color;
    int step;
    boolean shouldFlicker;

    public Star(int x, int y, int size, float brightness) {
        super(x, y, size, size);
        this.brightness = Math.min(Math.max(brightness, 0f), 1.0f);

        color = new Color(1.0f, 1.0f, 1.0f, brightness);
        step = (int) (Math.random() * 35);

        if (Math.random() > .25) {
            shouldFlicker = true;
        } else {
            shouldFlicker = false;
        }
    }

    public static Star generate(Rectangle bounds) {

        int px = (int) (Math.random() * bounds.getWidth());
        int py = (int) (Math.random() * bounds.getHeight());
        int size = (int) (Math.random() * 5);
        float brightness = (float) Math.random();

        return new Star(px, py, size, brightness);
    }

    public static List<Star> batchGenerate(Rectangle bounds, int count) {
        List<Star> stars = new ArrayList<Star>();
        for (int i = 0; i < count; i++) {
            stars.add(Star.generate(bounds));
        }
        return stars;
    }

    @Override
    public void draw(Graphics g) {

        int size;
        if (shouldFlicker) {
            step++;
            float alpha = (float) (brightness + Math.cos(step / 10) * brightness / 4);
            alpha = Math.min(Math.max(alpha, 0.0f), 1.0f);
            color = new Color(1.0f, 1.0f, 1.0f, alpha);
            size = (int) Math.round(this.getWidth() + Math.cos(step / 10) * this.getWidth() / 6);
        } else {
            size = this.getWidth();
        }
        g.setColor(color);
        g.fillOval(this.getXPixel(), this.getYPixel(), size, size);
    }

}
