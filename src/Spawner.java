import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;

public class Spawner extends ControllerObject {

    private Entity blueprint;

    Timer timer;

    private PlanetarySystem parent;

    private Direction dir;

    public Spawner(Entity object, int frequency, PlanetarySystem parent) {
        super();
        this.blueprint = object;
        this.parent = parent;

        dir = Direction.values()[(int) (Math.random() * 4)];

        timer = new Timer(frequency, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                spawn();

            }
        });
        timer.start();
    }

    public void spawn() {
        Entity toSpawn = this.blueprint.deepCopy();

        switch (this.dir) {

            case NORTH:
                toSpawn.setPx((float) (Math.random() * toSpawn.getCourtWidth()));
                toSpawn.setPy(toSpawn.getCourtHeight() + 75);
                toSpawn.setVelocity(new Vector(toSpawn.getVelocity(), (float) (3 * Math.PI / 2)));
                break;
            case EAST:
                toSpawn.setPx(- 75);
                toSpawn.setPy((float) (Math.random() * toSpawn.getCourtHeight()));
                toSpawn.setVelocity(new Vector(toSpawn.getVelocity(), 0));
                break;
            case SOUTH:
                toSpawn.setPx((float) (Math.random() * toSpawn.getCourtWidth()));
                toSpawn.setPy(-75);
                toSpawn.setVelocity(new Vector(toSpawn.getVelocity(), (float) (Math.PI / 2)));
                break;
            case WEST:
                toSpawn.setPx(toSpawn.getCourtWidth() + 75);
                toSpawn.setPy((float) (Math.random() * toSpawn.getCourtHeight()));
                toSpawn.setVelocity(new Vector(toSpawn.getVelocity(), (float) Math.PI));
                break;
            default:
                break;

        }

        toSpawn.setVelocity((float) (toSpawn.getVelocity() + (Math.random() * 0.5f)));
        parent.getEntities().add(toSpawn);
    }

    @Override
    public void end() {
        timer.stop();
    }

    @Override
    public void draw(Graphics g) {

        for (Entity e : parent.getEntities()) {
            e.draw(g);
        }

    }

    @Override
    public void update() {
        ArrayList<Entity> entitiesToDestroy = new ArrayList<Entity>();

        for (Entity e : parent.getEntities()) {
            if (e.isOutOfBounds() || e.shouldDispose()) {
                entitiesToDestroy.add(e);
            }
        }

        parent.getEntities().removeAll(entitiesToDestroy);
    }

}
