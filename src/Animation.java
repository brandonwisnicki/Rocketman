import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public abstract class Animation extends GameObject {

    private static BufferedImage[] frames;

    // ticks per frame
    private int tpf;
    private int step;

    private boolean loop;

    private boolean finished;

    public Animation(int x, int y, int width, int height, String[] filePaths, int tpf,
            boolean loop) {
        super(x, y, width, height);

        this.tpf = tpf;
        this.step = 0;

        this.loop = loop;
        finished = false;

        try {
            for (int i = 0; i < filePaths.length; i++) {
                if (getFrames()[i] == null) {
                    getFrames()[i] = ImageIO.read(new File(filePaths[i]));
                }
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }

    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        if (finished) {
            return;
        }

        int frame = step / tpf;

        if (frame >= getFrames().length) {
            if (loop) {
                step = 0;
            } else {
                finished = true;
            }
            return;
        }

        AffineTransform at = AffineTransform.getTranslateInstance(
                this.getXPixel() - this.getWidth(), this.getYPixel() - this.getWidth());
        at.concatenate(AffineTransform.getScaleInstance(1.5f, 1.5f));

        g2d.drawImage(getFrames()[frame], at, null);
        step++;
    }

    public abstract BufferedImage[] getFrames();

}
