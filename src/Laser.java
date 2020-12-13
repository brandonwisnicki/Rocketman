import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

public class Laser extends Entity {

    private static final int LASER_WIDTH = 3;
    private static final int LASER_HEIGHT = 30;

    private Color color;
    private GameCourt court;

    private static final int MAX_DISPOSE_BUFFER = 3;
    private int disposeBuffer;

    public Laser(Vector velocity, Color color, int px, int py, GameCourt court) {
        super(velocity.getMagnitude(), velocity.getAngle(), px, py, LASER_WIDTH, LASER_HEIGHT,
                court.getWidth(), court.getCourtHeight(), velocity.getAngle(), 0.0f, 0.0f, 0.0f,
                0.0f, 100.0f);
        this.color = color;
        this.court = court;
        this.disposeBuffer = 0;
    }

    @Override
    public Entity deepCopy() {
        return new Laser(new Vector(this.getVelocity(), this.getVelocityAngle()), this.color,
                this.getXPixel(), this.getYPixel(), this.court);
    }

    @Override
    public void draw(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(this.color);

        AffineTransform at = AffineTransform.getRotateInstance(
                this.getRotationalAngle() + Math.PI / 2, this.getCenterX(), this.getCenterY());

        Rectangle shape = new Rectangle(this.getXPixel(), this.getYPixel(), this.getWidth(),
                this.getHeight());
        Shape newShape = at.createTransformedShape(shape);

        g2d.fill(newShape);

//        drawHitbox(g);

    }

    @Override
    public void checkCollisions() {
        for (Entity e : court.getAllEntities()) {
            if (disposeBuffer == 0 && e instanceof Asteroid && !(e.equals(this))
                    && this.intersects(e)) {
                disposeBuffer = 1;
            }
        }

    }

    @Override
    public boolean shouldDispose() {
        if (disposeBuffer > 0) {
            disposeBuffer++;

        }

        return disposeBuffer > MAX_DISPOSE_BUFFER;
    }

}
