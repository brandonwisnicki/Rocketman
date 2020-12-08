import java.awt.Graphics;

public abstract class GameObject {
    /*
     * Current position of the object (in terms of graphics coordinates)
     * 
     * Coordinates are given by the upper-left hand corner of the object. This
     * position should always be within bounds. 0 <= px <= maxX 0 <= py <= maxY
     */
    private float px;
    private float py;

    /* Size of object, in pixels. */
    private int width;
    private int height;

    public GameObject(int x, int y, int width, int height) {
        this.px = x;
        this.py = y;
        this.width = width;
        this.height = height;

    }

    public int getXPixel() {
        return (int) Math.round(this.px);
    }

    public int getYPixel() {
        return (int) Math.round(this.py);
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public float getPy() {
        return this.py;
    }

    public float getPx() {
        return this.px;
    }

    public int getCenterX() {
        return (int) (2 * this.getPx() + this.getWidth()) / 2;
    }

    public int getCenterY() {
        return (int) (2 * this.getPy() + this.getHeight()) / 2;
    }

    public void setPx(float px) {
        this.px = px;
    }

    public void setPy(float py) {
        this.py = py;
    }

    /**
     * Default draw method that provides how the object should be drawn in the GUI.
     * This method does not draw anything. Subclass should override this method
     * based on how their object should appear.
     * 
     * @param g The <code>Graphics</code> context used for drawing the object.
     *          Remember graphics contexts that we used in OCaml, it gives the
     *          context in which the object should be drawn (a canvas, a frame,
     *          etc.)
     */
    public abstract void draw(Graphics g);
}
