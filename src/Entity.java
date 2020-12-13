import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collection;

/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

/**
 * An entity in the game.
 *
 * Entities exist in the game court. They have a position, velocity vector,
 * acceleration vector, size and bounds. Their velocity controls how they move;
 * their position should always be within their bounds.
 */
public abstract class Entity extends GameObject {

    /*
     * Bounds Tolerance: number of pixels from edge of screen before outOfBounds()
     * is true This is important for Spawners to be placed in out of bounds.
     */
    private static int boundsTolerance = 250;

    /*
     * Velocity: number of pixels to move every time move() is called.
     */
    private Vector velocity;

    /* Max Velocity: max possible velocity in meters/tick. */
    private float maxVelocity;

    /* Acceleration: number of pixels/tick to increase velocity by every tick. */
    private Vector acceleration;

    /* Max Acceleration: max possible acceleration in pixels/tick. */
    private float maxAcceleration;

    /* Directional Angle: number of degrees under the horizontal facing left */
    private float directionalAngle;

    /* Spin Velocity: number of degrees to move every time move() is called */
    private float spinVelocity;

    /*
     * Upper bounds of the area in which the object can be positioned. Maximum
     * permissible x, y positions for the upper-left hand corner of the object.
     */
    private int maxX;
    private int maxY;

    private int courtHeight;
    private int courtWidth;

    // Collision Box: The polygon that defines the space that an Entity takes up in the scene
    private CollisionBox cb;

    /**
     * Constructor
     */
    public Entity(float velocity, float velocityAngle, int px, int py, int width, int height,
            int courtWidth, int courtHeight, float directionAngle, float spinVelocity,
            float acceleration, float accelerationAngle, float maxAcceleration, float maxVelocity) {
        super(px, py, width, height);

        this.velocity = new Vector(velocity, velocityAngle);
        this.directionalAngle = directionAngle;
        this.spinVelocity = spinVelocity;
        this.acceleration = new Vector(acceleration, accelerationAngle);

        this.maxAcceleration = maxAcceleration;
        this.maxVelocity = maxVelocity;

        // take the width and height into account when setting the bounds for the upper
        // left corner
        // of the object.
        this.maxX = courtWidth;
        this.maxY = courtHeight;
        this.courtHeight = courtHeight;
        this.courtWidth = courtWidth;

        cb = new CollisionBox(px, py, directionAngle, width, height);
    }

    /***
     * GETTERS
     **********************************************************************************/

    public float getVelocity() {
        return this.velocity.getMagnitude();
    }

    public float getVy() {
        return this.velocity.getY();

    }

    public float getVx() {
        return this.velocity.getX();
    }

    public float getVelocityAngle() {
        return this.velocity.getAngle();
    }

    public float getRotationalAngle() {
        return this.directionalAngle;
    }

    public float getRotationalVelocity() {
        return this.spinVelocity;
    }

    public float getAcceleration() {
        return this.acceleration.getMagnitude();
    }

    public float getAy() {
        return this.acceleration.getY();

    }

    public float getAx() {
        return this.acceleration.getX();
    }

    public float getAccelerationAngle() {
        return this.acceleration.getAngle();
    }

    public int getCourtWidth() {
        return this.courtWidth;
    }

    public int getCourtHeight() {
        return this.courtHeight;
    }

    public CollisionBox getCollisionBox() {
        return this.cb;
    }
    
    /*  Entities generally do not need to be disposed
     *  Unless there is special behavior
     */
    public boolean shouldDispose() {
        return false;
    };
    

    /***
     * SETTERS
     **********************************************************************************/

    public void setVelocity(float v) {
        this.velocity = new Vector(v, this.velocity.getAngle());
    }

    public void setVelocity(Vector v) {
        this.velocity = new Vector(v.getMagnitude(), v.getAngle());
    }

    public void setAcceleration(float a) {
        this.acceleration = new Vector(a, this.acceleration.getAngle());
    }

    public void setRotationalAngle(int theta) {
        this.directionalAngle = theta;
    }

    public void setRotationalVelocity(float d) {
        this.spinVelocity = d;
    }

    @Override
    public void setPx(float px) {
        super.setPx(px);
    }

    public void setPy(float py) {
        super.setPy(py);
    }

    /***
     * PHYSICS METHODS
     **************************************************************************/

    public void addAcceleration(Vector acceleration) {
        this.acceleration.add(acceleration, this.maxAcceleration);
    }

    public void addVelocity(Vector velocity) {
        this.velocity.add(velocity, this.maxVelocity);
    }
    
    // Entities generally have no action attached to
    // hitting the scene walls
    public void handleHitWall() {
        return;
    }


    /***
     * UPDATES AND OTHER METHODS
     ****************************************************************/

    /**
     * Prevents the object from going outside of the bounds of the area designated
     * for the object. (i.e. Object cannot go outside of the active area the user
     * defines for it).
     */
    public void clip() {
        this.setPx(Math.min(Math.max(this.getPx(), 0), this.maxX));
        this.setPy(Math.min(Math.max(this.getPy(), 0), this.maxY));
    }

    public void elasticCollision(Entity that) {
        Vector oldVel = new Vector(this.getVelocity(), this.getVelocityAngle());
        this.setVelocity(new Vector(that.getVelocity(), that.getVelocityAngle()));
        that.setVelocity(oldVel);
    }

    //Any extra updates needed for an entity not called in move()
    //Most entities don't have extra update() needs
    public void update() {
        return;
    };

    
    /**
     * Moves the object by its velocity. Ensures that the object does not go outside
     * its bounds by clipping.
     */
    public void move() {
        this.addVelocity(this.acceleration);
        this.setPy(this.getPy() + this.getVy());
        this.setPx(this.getPx() + this.getVx());

        this.directionalAngle += this.spinVelocity;

        this.cb.update(this.getXPixel(), this.getYPixel(), this.getRotationalAngle());
        checkCollisions();
        hitWall();
        update();

    }

    /**
     * Determine whether this game object is currently intersecting another object.
     * 
     * Intersection is determined by comparing bounding boxes. If the bounding boxes
     * overlap, then an intersection is considered to occur.
     * 
     * @param that The other object
     * @return Whether this object intersects the other object.
     */
    public boolean intersects(Entity that) {
        return cb.isIntersecting(that.getCollisionBox());
    }

    /**
     * Determine whether the game object will hit a wall in the next time step. If
     * so, return stops the object
     */
    public void hitWall() {

        if (this.getXPixel() + this.velocity.getX() < 0) {
            handleHitWall();
        } else if (this.getXPixel() + this.velocity.getX() > this.maxX) {
            handleHitWall();
        }

        if (this.getYPixel() + this.velocity.getY() < 0) {
            handleHitWall();
        } else if (this.getYPixel() + this.velocity.getY() > this.maxY) {
            handleHitWall();
        }
    }

    /**
     * Returns a boolean regarding if the entity is outside
     * of bounds of the scene.
     * There exists a boundsTolerance to make sure that the
     * entire object is out of bounds when this returns true
     */
    public boolean isOutOfBounds() {
        if (this.getXPixel() + this.getWidth() + boundsTolerance < 0) {
            return true;
        } else if (this.getXPixel() - this.getWidth() - boundsTolerance > this.maxX) {
            return true;
        }

        if (this.getYPixel() + this.getHeight() + boundsTolerance < 0) {
            return true;
        } else if (this.getYPixel() - this.getHeight() - boundsTolerance > this.maxY) {
            return true;
        }
        return false;

    }

    /**
     * This is for debugging purposes.
     * Draws the four corners of a hitbox for entities
     */
    public void drawHitbox(Graphics g) {
        int[][] positions = this.getCollisionBox().getCornerLocations();

        g.setColor(Color.CYAN);

        g.drawRect(positions[0][0], positions[0][1], 2, 2);
        g.drawRect(positions[1][0], positions[1][1], 2, 2);
        g.drawRect(positions[2][0], positions[2][1], 2, 2);
        g.drawRect(positions[3][0], positions[3][1], 2, 2);
    }

    public abstract Entity deepCopy();

    public Collection<Entity> getSelfAndChildren() {
        ArrayList<Entity> allEntities = new ArrayList<Entity>();
        allEntities.add(this);

        return allEntities;
    }

    public abstract void checkCollisions();



}