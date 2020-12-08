
/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * GameCourt
 * 
 * This class holds the primary game logic for how different objects interact
 * with one another. Take time to understand how the timer interacts with the
 * different methods and how it repaints the GUI on every tick().
 */
@SuppressWarnings("serial")
public class GameCourt extends JPanel {

    // the state of the game logic
    private Ship playerShip; // the Black Square, keyboard control

    // background elements

    private GameState state = GameState.NORMAL; // whether the game is running
    private JLabel status; // Current status text, i.e. "Running..."
    private JFrame frame; // Top-level frame component

    // Game constants
    public static final int COURT_WIDTH = 1024;
    public static final int COURT_HEIGHT = 576;
    public static final float ACCELERATION = 0.05f;

    // Update interval for timer, in milliseconds
    public static final int INTERVAL = 16;
    private final Set<Integer> currentKeys = new TreeSet<Integer>();

    private PlanetarySystem[][] galaxy;
    private Economy galacticEconomy;
    private int systemCoordX;
    private int systemCoordY;

    private TextBox notificationBox;
    private GalaxyMap map;
    private PlayerInfo playerInfo;

    private BufferedImage winScreen;
    private static final String WIN_SCREEN_PATH = "files/dubscreen.png";

    public GameCourt(JFrame frame, JLabel status) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        try {
            if (winScreen == null) {
                winScreen = ImageIO.read(new File(WIN_SCREEN_PATH));
            }

        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }

        // The timer is an object which triggers an action periodically with the given
        // INTERVAL. We
        // register an ActionListener with this timer, whose actionPerformed() method is
        // called each
        // time the timer triggers. We define a helper method called tick() that
        // actually does
        // everything that should be done in a single timestep.

        Timer timer = new Timer(INTERVAL, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logicTick();
                paintTick();

            }
        });
        timer.start(); // MAKE SURE TO START THE TIMER!

        // Enable keyboard focus on the court area.
        // When this component has the keyboard focus, key events are handled by its key
        // listener.
        setFocusable(true);

        // This key listener allows the square to move as long as an arrow key is
        // pressed, by
        // changing the square's velocity accordingly. (The tick method below actually
        // moves the
        // square.)
        addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {

                currentKeys.add(e.getKeyCode());

            }

            public void keyReleased(KeyEvent e) {
                currentKeys.remove(e.getKeyCode());

                if (state == GameState.NORMAL) {
                    if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
                        playerShip.setRotationalVelocity(0);
                    } else if (e.getKeyCode() == KeyEvent.VK_RIGHT
                            || e.getKeyCode() == KeyEvent.VK_D) {
                        playerShip.setRotationalVelocity(0);
                    } else if (e.getKeyCode() == KeyEvent.VK_DOWN
                            || e.getKeyCode() == KeyEvent.VK_S) {
                        playerShip.setAcceleration(0);
                    } else if (e.getKeyCode() == KeyEvent.VK_UP
                            || e.getKeyCode() == KeyEvent.VK_W) {
                        playerShip.setAcceleration(0);
                    }

                    if (e.getKeyCode() == KeyEvent.VK_F10) {
                        updateState(GameState.WIN);
                    }

                    if (getCurrentSystem() instanceof OutpostSystem) {

                        OutpostSystem outpost = (OutpostSystem) getCurrentSystem();

                        if (e.getKeyCode() == KeyEvent.VK_E
                                && outpost.getOutpost().isAccessible()) {
                            updateState(GameState.SHOP);
                        }
                    }

                } else if (state == GameState.SHOP) {

                    Shop currentShop = ((OutpostSystem) getCurrentSystem()).getShop();

                    if (e.getKeyCode() == KeyEvent.VK_ESCAPE
                            || e.getKeyCode() == KeyEvent.VK_SPACE) {
                        updateState(GameState.NORMAL);
                    }

                    switch (e.getKeyCode()) {
                    case KeyEvent.VK_1:
                        newNotification(currentShop.repairShip());
                        break;
                    case KeyEvent.VK_2:
                        newNotification(currentShop.refuelShip());
                        break;
                    case KeyEvent.VK_3:
                        newNotification(currentShop.upgradeHull());
                        break;
                    case KeyEvent.VK_4:
                        newNotification(currentShop.upgradeFuelTank());
                        break;
                    case KeyEvent.VK_5:
                        newNotification(currentShop.upgradePowerSystem());
                        break;
                    case KeyEvent.VK_Q:
                        newNotification(currentShop.buyIron(10));
                        break;
                    case KeyEvent.VK_A:
                        newNotification(currentShop.sellIron(1));
                        break;
                    case KeyEvent.VK_Z:
                        newNotification(currentShop.sellIron(Integer.MAX_VALUE));
                        break;
                    case KeyEvent.VK_W:
                        newNotification(currentShop.buySilver(10));
                        break;
                    case KeyEvent.VK_S:
                        newNotification(currentShop.sellSilver(1));
                        break;
                    case KeyEvent.VK_X:
                        newNotification(currentShop.sellSilver(Integer.MAX_VALUE));
                        break;
                    case KeyEvent.VK_E:
                        newNotification(currentShop.buyGold(10));
                        break;
                    case KeyEvent.VK_D:
                        newNotification(currentShop.sellGold(1));
                        break;
                    case KeyEvent.VK_C:
                        newNotification(currentShop.sellGold(Integer.MAX_VALUE));
                        break;
                    case KeyEvent.VK_R:
                        newNotification(currentShop.sellGold(Integer.MAX_VALUE));
                        newNotification(currentShop.sellSilver(Integer.MAX_VALUE));
                        newNotification(currentShop.sellIron(Integer.MAX_VALUE));
                        break;
                    default:
                        break;

                    }

                }

            }

        });

        this.status = status;
        this.frame = frame;
        this.galaxy = new PlanetarySystem[10][10];
    }

    /**
     * (Re-)set the game to its initial state.
     */
    public void reset() {
        playerShip = new Ship(this);
        galacticEconomy = new Economy();
        notificationBox = new TextBox(5, 20, new ArrayList<String>());
        playerInfo = new PlayerInfo(5, 130, playerShip);
        map = new GalaxyMap(5, 375, this);

        for (int i = 0; i < galaxy.length; i++) {
            for (int j = 0; j < galaxy[i].length; j++) {

                double chance = Math.random();

                if (chance > 0.85) {
                    this.galaxy[i][j] = new OutpostSystem(
                            NameGenerator.generatePlanetarySystemName(), this);
                } else if (chance > 0.65) {
                    this.galaxy[i][j] = new NebulaSystem(
                            NameGenerator.generatePlanetarySystemName(), this);
                } else if (chance > 0.3) {

                    this.galaxy[i][j] = new AsteroidBeltSystem(
                            NameGenerator.generatePlanetarySystemName(), this);
                } else {
                    this.galaxy[i][j] = new PlanetarySystem(
                            NameGenerator.generatePlanetarySystemName(), this);
                }

            }
        }

        systemCoordY = (int) (Math.random() * 10);
        systemCoordX = (int) (Math.random() * 10);

        this.getCurrentSystem().setupScene();

        updateState(GameState.NORMAL);

        // Make sure that this component has the keyboard focus
        requestFocusInWindow();
    }

    public void load(Ship loadedPlayer, PlanetarySystem[][] loadedGalaxy, int xCoord, int yCoord) {
        updateState(GameState.LOADING);

        playerShip = loadedPlayer;
        galacticEconomy = new Economy();
        notificationBox = new TextBox(5, 20, new ArrayList<String>());
        playerInfo = new PlayerInfo(5, 130, playerShip);
        map = new GalaxyMap(5, 375, this);

        this.galaxy = loadedGalaxy;

        systemCoordY = yCoord;
        systemCoordX = xCoord;

        this.getCurrentSystem().setupScene();

        updateState(GameState.NORMAL);

        // Make sure that this component has the keyboard focus
        requestFocusInWindow();
    }

    public void updateState(GameState newState) {

        state = newState;

        switch (newState) {
            case NORMAL:
                status.setText("Exploring");
                break;
            case DEAD:
                status.setText("Your ship was destroyed.");
                break;
            case SHOP:
                this.playerShip.setRotationalVelocity(.0f);
                this.playerShip.setAcceleration(0);
                this.playerShip.setVelocity(0);
                this.currentKeys.removeAll(this.currentKeys);
                status.setText("Shop");
                break;
            case WIN:
                status.setText("You have enough money to get home!");
                break;
            default:
                status.setText("Invalid State");
        }

    }

    public GameState getState() {
        return this.state;
    }

    void destroyedShip() {

        newNotification("LOST SIGNAL");
        newNotification("LOST SIG-AL");
        newNotification("LO-T S-GNAL");
        newNotification("-O-T S--G-L");
        newNotification("---- ------");
        updateState(GameState.DEAD);

    }

    /**
     * This method is called every time the timer defined in the constructor
     * triggers.
     */
    void logicTick() {
        if (state == GameState.NORMAL) {

            for (Integer key : currentKeys) {
                if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
                    playerShip.setRotationalVelocity(-.05f);
                }
                if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
                    playerShip.setRotationalVelocity(.05f);
                }
                if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
                    playerShip.addAcceleration(
                            new Vector(-ACCELERATION / 3, playerShip.getRotationalAngle()));
                }
                if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
                    playerShip.addAcceleration(
                            new Vector(ACCELERATION, playerShip.getRotationalAngle()));
                }

                if (key == KeyEvent.VK_SHIFT) {
                    playerShip.setVelocity(0.0f);
                }

                if (key == KeyEvent.VK_SPACE) {
                    playerShip.attack();
                }

            }
            playerShip.move();
            this.galaxy[systemCoordY][systemCoordX].move();
        } else if (state == GameState.DEAD) {

            this.galaxy[systemCoordY][systemCoordX].move();
        }

    }

    public ArrayList<Entity> getAllEntities() {

        ArrayList<Entity> allEntities = new ArrayList<Entity>();

        allEntities.addAll(this.playerShip.getSelfAndChildren());

        allEntities.addAll(this.galaxy[systemCoordY][systemCoordX].getEntities());

        return allEntities;

    };

    public void newNotification(String line) {
        notificationBox.addLine(line);
    }

    public PlanetarySystem[][] getGalaxy() {
        return this.galaxy;
    }

    public Ship getPlayerShip() {
        return this.playerShip;
    }

    public Economy getEconomy() {
        return galacticEconomy;
    }

    public PlanetarySystem getCurrentSystem() {
        return this.galaxy[systemCoordY][systemCoordX];
    }

    public int getCurrentX() {
        return systemCoordX;
    }

    public int getCurrentY() {
        return systemCoordY;
    }

    public int getCourtHeight() {
        return COURT_HEIGHT;
    }

    public int getCourtWidth() {
        return COURT_WIDTH;
    }

    public void travelToSystem(int x, int y) {
        x = Math.max(0, Math.min(this.galaxy[0].length - 1, x));
        y = Math.max(0, Math.min(this.galaxy.length - 1, y));

        this.galaxy[y][x].setupScene();

        int oldX = systemCoordX;
        int oldY = systemCoordY;

        systemCoordX = x;
        systemCoordY = y;

        this.galaxy[oldY][oldX].disposeScene();

    }

    public boolean travelToAdjacentSystem(Direction dir) {
        if ((this.systemCoordX == this.galaxy[0].length - 1 && dir == Direction.EAST)
                || (this.systemCoordY == this.galaxy.length - 1 && dir == Direction.SOUTH)
                || (this.systemCoordX == 0 && dir == Direction.WEST)
                || (this.systemCoordY == 0 && dir == Direction.NORTH)) {
            return false;
        }

        switch (dir) {
        case NORTH:
            travelToSystem(systemCoordX, systemCoordY - 1);
            return true;
        case SOUTH:
            travelToSystem(systemCoordX, systemCoordY + 1);
            return true;
        case WEST:
            travelToSystem(systemCoordX - 1, systemCoordY);
            return true;
        case EAST:
            travelToSystem(systemCoordX + 1, systemCoordY);
            return true;
        default:
            return false;
        }

    }

    void paintTick() {
        // update the display
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (getState() == GameState.NORMAL || getState() == GameState.DEAD) {
            getCurrentSystem().draw(g);
            playerShip.draw(g);
            getCurrentSystem().drawOverlay(g);
            notificationBox.draw(g);
            playerInfo.draw(g);
            map.draw(g);

        } else if (getState() == GameState.SHOP) {

            if (this.getCurrentSystem() instanceof OutpostSystem) {
                OutpostSystem outpost = (OutpostSystem) this.getCurrentSystem();

                outpost.getShop().draw(g);

                notificationBox.draw(g);
                playerInfo.draw(g);
                map.draw(g);
            }

        } else if (getState() == GameState.WIN) {

            Graphics2D g2d = (Graphics2D) g;

            g2d.drawImage(winScreen, 0, 0, this.getCourtWidth(), this.getCourtHeight(),  null);
        }

    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }

}