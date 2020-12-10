

/***
 * Controller objects are objects that are not visible, but serve a functional
 * purpose to the scene. Controllers can include things like spawners
 * All controllers are at 0,0 with a width and height of 0 as they need not exist
 * in the scene visibly.
 */
public abstract class ControllerObject extends GameObject {

    /**
     * Constructor
     */
    public ControllerObject() {
        super(0, 0, 0, 0);
    }

    public abstract void update();

    public abstract void end();
}
