import java.awt.image.BufferedImage;

/**
 * Explosion Animation
 * Creating this will create an
 * explosion effect on screen at a
 * specific x,y location
 *
 */

public class Explosion extends Animation {

    /**
     * Constructor
     */
    private static String[] filePaths = { "files/explosion/explosion1.png",
                                          "files/explosion/explosion2.png", 
                                          "files/explosion/explosion3.png",
                                          "files/explosion/explosion4.png", 
                                          "files/explosion/explosion5.png",
                                          "files/explosion/explosion6.png", 
                                          "files/explosion/explosion7.png", };
    
    private static BufferedImage[] frames = new BufferedImage[filePaths.length];

    public Explosion(int x, int y, int width, int height) {
        super(x, y, width, height, filePaths, 10, false);
        
    }

    @Override
    public BufferedImage[] getFrames() {
        return frames;
    }

}
