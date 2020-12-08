
/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import javax.imageio.ImageIO;

/**
 * A basic game object starting in the upper left corner of the game court. It
 * is displayed as a square of a specified color.
 */
public class Ship extends Entity {

    public static final int INIT_VEL = 0;
    public static final int INIT_ANGLE_OF_VELOCITY = 1;
    public static final int INIT_POS_X = 1024 / 2;
    public static final int INIT_POS_Y = 576 / 2;
    public static final int SIZE = 32;
    public static final int INIT_ACC = 0;
    public static final float INIT_DIRECTIONAL_ANGLE = (float) (3 * Math.PI / 2);
    public static final int INIT_SPIN_VELOCITY = 0;
    public static final int INIT_ACCELERATION_ANGLE = 0;
    public static final int MAX_VELOCITY = 4;
    public static final int MAX_ACCELERATION = 1;

    private static final String SHIP_SPRITE_FILE = "files/ship.png";
    private static final String FLAME_SPRITE_FILE = "files/flame.png";

    private static BufferedImage shipSprite;
    private static BufferedImage flameSprite;

    private ArrayList<Entity> laserEntities = new ArrayList<Entity>();
    private ArrayList<Animation> animations = new ArrayList<Animation>();

    private long lastTimeFired;

    private GameCourt court;

    private float health;
    private float maxHealth;

    private float fuel;
    private float maxFuel;

    private float power;
    private float maxPower;

    private int credits;

    private Set<Item> inventory;

//    If true, power functions are disabled until power = maxPower
    private boolean powerCooldown = false;

    private static final int MAX_COLLISION_BUFFER = 20;
    private int collisionBuffer;

    /**
     * Note that, because we don't need to do anything special when constructing a
     * Square, we simply use the superclass constructor called with the correct
     * parameters.
     */
    public Ship(GameCourt court) {
        super(INIT_VEL, INIT_ANGLE_OF_VELOCITY, INIT_POS_X, INIT_POS_Y, SIZE, SIZE,
                court.getWidth(), court.getHeight(), INIT_DIRECTIONAL_ANGLE, INIT_SPIN_VELOCITY,
                INIT_ACC, INIT_ACCELERATION_ANGLE, MAX_ACCELERATION, MAX_VELOCITY);
        try {
            if (shipSprite == null) {
                shipSprite = ImageIO.read(new File(SHIP_SPRITE_FILE));
            }
            if (flameSprite == null) {
                flameSprite = ImageIO.read(new File(FLAME_SPRITE_FILE));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }

        this.court = court;

        health = 25.0f;
        maxHealth = 25.0f;
        fuel = 10.0f;
        maxFuel = 10.0f;
        power = 6.0f;
        maxPower = 6.0f;
        collisionBuffer = 0;
        credits = 1000;
        lastTimeFired = System.nanoTime();
        inventory = new TreeSet<Item>();
    }

    public Ship(GameCourt court, float health, float maxHealth, float fuel, float maxFuel,
            float power, float maxPower, int credits, int ironSupply, int silverSupply,
            int goldSupply) {
        super(INIT_VEL, INIT_ANGLE_OF_VELOCITY, INIT_POS_X, INIT_POS_Y, SIZE, SIZE,
                court.getWidth(), court.getHeight(), INIT_DIRECTIONAL_ANGLE, INIT_SPIN_VELOCITY,
                INIT_ACC, INIT_ACCELERATION_ANGLE, MAX_ACCELERATION, MAX_VELOCITY);
        try {
            if (shipSprite == null) {
                shipSprite = ImageIO.read(new File(SHIP_SPRITE_FILE));
            }
            if (flameSprite == null) {
                flameSprite = ImageIO.read(new File(FLAME_SPRITE_FILE));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }

        this.court = court;

        this.health = health;
        this.maxHealth = maxHealth;
        this.fuel = fuel;
        this.maxFuel = maxFuel;
        this.power = power;
        this.maxPower = maxPower;
        collisionBuffer = 0;
        this.credits = credits;
        lastTimeFired = System.nanoTime();
        inventory = new TreeSet<Item>();

        addItem(new Iron(ironSupply));
        addItem(new Silver(silverSupply));
        addItem(new Gold(goldSupply));

    }

    @Override
    public void draw(Graphics g) {

        for (Entity beam : laserEntities) {
            beam.draw(g);
        }
        if (isAlive()) {
            Graphics2D g2d = (Graphics2D) g;
            AffineTransform at = AffineTransform.getRotateInstance(
                    this.getRotationalAngle() + Math.PI / 2,
                    (this.getXPixel() + this.getXPixel() + SIZE) / 2,
                    (this.getYPixel() + this.getYPixel() + SIZE) / 2);
            at.concatenate(
                    AffineTransform.getTranslateInstance(this.getXPixel(), this.getYPixel()));

            g2d.drawImage(shipSprite, at, null);

            if (this.getAcceleration() > 0) {

                at.concatenate(AffineTransform.getTranslateInstance(1, 30));

                g2d.drawImage(flameSprite, at, null);

                at.concatenate(AffineTransform.getTranslateInstance(21, 0));

                g2d.drawImage(flameSprite, at, null);
            }
        }
        for (Animation anim : animations) {
            anim.draw(g);
        }

//        drawHitbox(g);

    }

    @Override
    public void handleHitWall() {

        boolean successfulTravel = false;

        if (fuel == 0.0f) {
            court.newNotification("You're out of fuel. You can't warp.");
            this.clip();
            return;
        }

        if (power <= 3.0f) {
            court.newNotification("You need more power to warp.");
            this.clip();
            return;
        }

        if (this.getPx() < 0) {
            successfulTravel = this.court.travelToAdjacentSystem(Direction.WEST);
            if (successfulTravel) {
                this.setPx(this.getCourtWidth());
                fuel--;
                addPower(-6.0f);
            } else {
                this.clip();
            }
        } else if (this.getPx() > this.getCourtWidth()) {
            successfulTravel = this.court.travelToAdjacentSystem(Direction.EAST);
            if (successfulTravel) {
                this.setPx(0);
                fuel--;
                addPower(-6.0f);
            } else {
                this.clip();
            }
        } else if (this.getPy() < 0) {
            successfulTravel = this.court.travelToAdjacentSystem(Direction.NORTH);
            if (successfulTravel) {
                this.setPy(this.getCourtHeight());
                fuel--;
                addPower(-6.0f);

            } else {
                this.clip();
            }
        } else if (this.getPy() > this.getCourtHeight()) {
            successfulTravel = this.court.travelToAdjacentSystem(Direction.SOUTH);
            if (successfulTravel) {
                this.setPy(0);
                fuel--;
                addPower(-6.0f);
            } else {
                this.clip();
            }

        }

    }

    @Override
    public Entity deepCopy() {
        return new Ship(court);
    }

    public void attack() {
        if (System.nanoTime() - lastTimeFired < 500000000 || power <= 0.0f || powerCooldown) {
            return;
        }

        lastTimeFired = System.nanoTime();
        Laser newLaser = new Laser(new Vector(10.0f, this.getRotationalAngle()), Color.RED,
                (int) (this.getCenterX() + (SIZE * Math.cos(this.getRotationalAngle()))),
                (int) (this.getCenterY() - (SIZE / 2)
                        + (SIZE * Math.sin(this.getRotationalAngle()))),
                this.getCourtWidth(), this.getCourtHeight(), court);

        laserEntities.add(newLaser);

        addPower(-1.0f);

    }

    public float getCurrentHealth() {
        return health;
    }

    public void heal(float hp) {
        health += hp;
        health = Math.min(maxHealth, health);
    }

    public void refuel(float gallons) {
        fuel += gallons;
        fuel = Math.min(maxFuel, fuel);
    }

    public void upgradeFuelTank() {
        maxFuel += 1.0f;
    }

    public void upgradeHull() {
        maxHealth += 5.0f;
    }

    public void upgradePower() {
        maxPower += 2.0f;
    }

    public void addPower(float p) {
        power += p;

        if (power <= 0.0f) {
            powerCooldown = true;
            power = 0.0f;
        }

    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public float getFuel() {
        return fuel;
    }

    public float getMaxFuel() {
        return maxFuel;
    }

    public float getPower() {
        return power;
    }

    public float getMaxPower() {
        return maxPower;
    }

    public boolean isAlive() {
        return getCurrentHealth() > 0.0f;
    }

    public int getCredits() {
        return credits;
    }

    public void addCredits(int amt) {
        credits += amt;
        if (credits >= 50000) {
            court.updateState(GameState.WIN);
        }
    }

    public Set<Item> getInventory() {
        return inventory;
    }

    public void addItem(Item item) {

        Item inventoryItem = this.getItem(item.getName());
        if (inventoryItem == null) {
            inventoryItem = item;
            inventory.add(item);
        } else {
            inventoryItem.addItems(item.getAmount());
        }

        if (inventoryItem.getAmount() <= 0) {
            inventory.remove(getItem(item.getName()));
        }
    }

    public Item getItem(String name) {
        for (Item item : inventory) {
            if (item.getName().equals(name)) {
                return item;
            }
        }
        return null;
    }

    @Override
    public void update() {

        if (power < maxPower) {
            addPower(0.01f);
        }
        if (power >= maxPower - 0.1f) {
            powerCooldown = false;
        }

        ArrayList<Entity> entitiesToDestroy = new ArrayList<Entity>();

        for (Entity beam : laserEntities) {
            beam.move();
            if (beam.isOutOfBounds() || beam.shouldDispose()) {
                entitiesToDestroy.add(beam);
            }
        }

        laserEntities.removeAll(entitiesToDestroy);

    }

    @Override
    public void checkCollisions() {

        if (!isAlive()) {
            return;
        }

        if (collisionBuffer > MAX_COLLISION_BUFFER) {
            collisionBuffer = 0;
        }
        if (collisionBuffer > 0) {
            collisionBuffer++;
        }

        for (Entity e : court.getAllEntities()) {
            if (collisionBuffer == 0 && e instanceof Asteroid && this.intersects(e)) {
                collisionBuffer = 1;

                takeDamage(3.0f + 7.0f * (this.getVelocity() / MAX_VELOCITY));

                elasticCollision(e);

            }
        }

    }

    public void takeDamage(float hp) {

        health -= hp;

        if (health <= 0.0f) {

            this.setVelocity(0.0f);
            this.setAcceleration(0.0f);
            animations.add(new Explosion(this.getXPixel(), this.getYPixel(), SIZE, SIZE));
            court.destroyedShip();
        }

    }

    @Override
    public boolean shouldDispose() {

        return !isAlive();
    }

    public Collection<Entity> getSelfAndChildren() {
        ArrayList<Entity> allEntities = new ArrayList<Entity>();

        allEntities.add(this);
        allEntities.addAll(laserEntities);

        return allEntities;
    }
}