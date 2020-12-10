import java.awt.Graphics;

public class AsteroidBeltSystem extends PlanetarySystem {

    public AsteroidBeltSystem(String name, GameCourt court) {
        super(name, court);

    }

    public AsteroidBeltSystem(String name, boolean discovered, GameCourt court) {
        super(name, court);

        if (discovered) {
            silentDiscovery();
        }

    }

    public void setupScene() {
        super.setupScene();

        Asteroid asteroid = new Asteroid(1100, 250, this.getCourt().getCourtWidth(),
                this.getCourt().getCourtHeight(), .5f, 0.01f, (float) Math.PI, this.getCourt());

        this.getControllers().add(new Spawner(asteroid, 1000, this));
    }

    //This system has no overlay to draw
    @Override
    public void drawOverlay(Graphics g) {
        return;
    }

}
