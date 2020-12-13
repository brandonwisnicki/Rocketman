import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class PlanetarySystem {

    private String name;

    private ArrayList<Entity> entities;
    private ArrayList<ControllerObject> controllers;
    private ArrayList<BackgroundObject> backgroundObjects;
    private boolean isDiscovered;
    private CelestialBody backgroundCenterpiece;

    private int courtWidth;
    private int courtHeight;

    private GameCourt court;

    public PlanetarySystem(String name, GameCourt court) {
        this.name = name;

        this.courtHeight = court.getHeight();
        this.courtWidth = court.getWidth();
        this.court = court;

        if (Math.random() > .7) {
            this.backgroundCenterpiece = new CelestialBody(600, 100, CelestialBody.randomBody());
        } else {
            this.backgroundCenterpiece = null;
        }

        isDiscovered = false;

    }

    public PlanetarySystem(String name, boolean discovered, GameCourt court) {
        this.name = name;

        this.courtHeight = court.getHeight();
        this.courtWidth = court.getWidth();
        this.court = court;

        if (Math.random() > .7) {
            this.backgroundCenterpiece = new CelestialBody(600, 100, CelestialBody.randomBody());
        } else {
            this.backgroundCenterpiece = null;
        }

        isDiscovered = discovered;

    }

    
    public boolean isDiscovered() {
        return isDiscovered;
    }
    
    public String getName() {
        return this.name;
    }

    public void discoverSystem() {
        isDiscovered = true;
        court.newNotification("SYSTEM DISCOVERED: " + this.name);
    }
    
    public void silentDiscovery() {
        isDiscovered = true;
    }

    public void setupScene() {

        entities = new ArrayList<Entity>();
        controllers = new ArrayList<ControllerObject>();
        backgroundObjects = new ArrayList<BackgroundObject>();

        backgroundObjects.add(new SpaceBackground(0, 0, courtWidth, courtHeight));

        if (backgroundCenterpiece != null) {
            backgroundObjects.add(backgroundCenterpiece);
        }

        discoverSystem();
    }

    public void disposeScene() {

        for (ControllerObject c : controllers) {
            c.end();
        }


        controllers = null;
        entities = null;
        backgroundObjects = null;

    }

    public void draw(Graphics g) {

        for (BackgroundObject object : backgroundObjects) {
            object.draw(g);
        }
        for (Entity entity : entities) {
            entity.draw(g);
        }
        for (ControllerObject controller : controllers) {
            controller.draw(g);
        }
        g.setColor(Color.WHITE);
        g.drawString("Current System: " + name, 10, 10);
    }

    public void move() {

        for (Entity entity : entities) {
            entity.move();
        }
        for (ControllerObject controller : controllers) {
            controller.update();
        }
    }

    public ArrayList<ControllerObject> getControllers() {
        return controllers;
    }

    public ArrayList<Entity> getEntities() {
        return entities;
    }

    public ArrayList<BackgroundObject> getBackgroundObjects() {
        return backgroundObjects;
    }

    public GameCourt getCourt() {
        return court;
    }
    
    public void drawOverlay(Graphics g) {
        return;
    };

}
