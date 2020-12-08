
public abstract class ControllerObject extends GameObject {

    public ControllerObject() {
        super(0, 0, 0, 0);
    }

    public abstract void update();

    public abstract void end();
}
