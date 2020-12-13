import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class Asteroid extends Entity {

    
    public static final int HEIGHT = 64;
    public static final int WIDTH = 96;
    public static final float INIT_DIRECTIONAL_ANGLE = (float) (3.0 * Math.PI / 2);
    public static final int INIT_ACCELERATION = 0;
    public static final int INIT_ACCELERATION_ANGLE = 0;
    public static final int INIT_MAX_ACCELERATION = 1;
    public static final int INIT_MAX_VELOCITY = 4;
    public static final String ASTEROID_SPRITE_FILE = "files/asteroids/asteroid1.png";

    private static BufferedImage asteroidSprite;

    private GameCourt court;

    
    //These variables represent how many frames until the collision functions should recheck.
    //This is to prevent the same collision event from triggering an action multiple times
    //i.e. only one item per asteroid destruction
    private static final int MAX_DISPOSE_BUFFER = 10;
    private int disposeBuffer;

    private static final int MAX_COLLISION_BUFFER = 5;
    private int asteroidCollisionBuffer;
    private int laserCollisionBuffer;

    private float hp;

    private ArrayList<Animation> animations = new ArrayList<Animation>();

    public Asteroid(int px, int py, int courtWidth, int courtHeight, float velocity,
            float spinVelocity, float angle, GameCourt court) {
        super(velocity, angle, px, py, WIDTH, HEIGHT, courtWidth, courtHeight,
                INIT_DIRECTIONAL_ANGLE, spinVelocity, INIT_ACCELERATION, INIT_ACCELERATION_ANGLE,
                INIT_MAX_ACCELERATION, INIT_MAX_VELOCITY);
        try {
            if (asteroidSprite == null) {
                asteroidSprite = ImageIO.read(new File(ASTEROID_SPRITE_FILE));
            }

        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }

        hp = (float) (Math.random() * 3.0f + 1.0f);
        this.court = court;
        this.disposeBuffer = 0;
        this.laserCollisionBuffer = 0;
        this.asteroidCollisionBuffer = 0;
    }

    @Override
    public void draw(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;

        AffineTransform at = AffineTransform.getRotateInstance(
                this.getRotationalAngle() + Math.PI / 2,
                (this.getXPixel() + this.getXPixel() + WIDTH) / 2,
                (this.getYPixel() + this.getYPixel() + HEIGHT) / 2);
        at.concatenate(AffineTransform.getTranslateInstance(this.getXPixel(), this.getYPixel()));
        g2d.drawImage(asteroidSprite, at, null);

        for (Animation anim : animations) {
            anim.draw(g);
        }

//        this.drawHitbox(g);

    }

    // Returns whether of not the asteroid should be removed from the scenes
    public boolean shouldDispose() {
        if (disposeBuffer > 0) {
            disposeBuffer++;

        }
        return disposeBuffer > MAX_DISPOSE_BUFFER;

    }

    @Override
    public Asteroid deepCopy() {
        return new Asteroid(this.getXPixel(), this.getYPixel(), this.getCourtWidth(),
                this.getCourtHeight(), this.getVelocity(), this.getRotationalVelocity(),
                this.getVelocityAngle(), court);
    }

    //Checks every object in scene for collisions
    //If collision and
        //is a laser: decrease asteroid HP and if HP <= 0, destroy asteroid
        //is an asteroid: elastic collision between the two objects
    @Override
    public void checkCollisions() {

        if (asteroidCollisionBuffer > MAX_COLLISION_BUFFER) {
            asteroidCollisionBuffer = 0;
        }
        if (asteroidCollisionBuffer > 0) {
            asteroidCollisionBuffer++;
        }
        
        if (laserCollisionBuffer > MAX_COLLISION_BUFFER) {
            laserCollisionBuffer = 0;
        }
        if (laserCollisionBuffer > 0) {
            laserCollisionBuffer++;
        }

        for (Entity e : court.getAllEntities()) {
            if (laserCollisionBuffer == 0 && disposeBuffer == 0 && e instanceof Laser
                    && !(e.equals(this)) && this.intersects(e)) {
                laserCollisionBuffer = 1;

                hp--;

                animations.add(new Explosion(e.getXPixel(), e.getYPixel(), 32, 32));

                if (hp <= 0) {

                    double probability = Math.random();

                    if (probability > 0.7) {
                        this.court.getPlayerShip().addItem(new Iron(1));
                        this.court.newNotification("1 Iron Ore Collected");

                    } else if (probability > 0.50) {
                        this.court.getPlayerShip().addItem(new Gold(1));
                        this.court.newNotification("1 Gold Ore Collected");

                    } else if (probability > 0.40) {
                        this.court.getPlayerShip().addItem(new Silver(1));
                        this.court.newNotification("1 Silver Ore Collected");

                    }

                    disposeBuffer = 1;

                }

            } else if (asteroidCollisionBuffer == 0 && (e instanceof Asteroid) && !(e.equals(this))
                    && this.intersects(e)) {
                asteroidCollisionBuffer = 1;
                elasticCollision(e);

            }
        }

    }



}
