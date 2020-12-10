import java.awt.Color;
import java.awt.Graphics;
import java.util.Set;

/**
 * User Interface for all player information such as health, fuel, power,
 * credits, and current inventory.
 */

public class PlayerInfo extends UserInterface {

    Ship player;

    public PlayerInfo(int px, int py, Ship player) {
        super(px, py, 250, 235);
        this.player = player;
    }

    /**
     * Draws an a segemented progress bar on screen for fuel, health, and power
     * 
     * @param label:           Text to display next to progress bar
     * @param g:               Graphics object
     * @param x:               x location
     * @param y:               y location
     * @param current:         current value of progress bar
     * @param max:             maximum value of progress bar
     * @param divider:         number of units each bar should be
     * @param yellowIndicator: threshold for when bar should be colored yellow
     * @param redWarning:      threshold for when bar should be colored red
     */
    public void drawIndicator(String label, Graphics g, int x, int y, float current, float max,
            float divider, int yellowIndicator, int redWarning) {
        float percentage = current / max;

        int boxCount = (int) (max / divider);

        int fullHealthBoxes = (int) (percentage * boxCount);
        int boxWidth = 175 / boxCount - 5;
        g.setColor(this.getTextColor());
        g.drawString("> " + label, x, y + 9);
        int currentX = x + 55;
        for (int i = 0; i < boxCount; i++) {
            if (i < fullHealthBoxes) {

                if (fullHealthBoxes < redWarning) {
                    g.setColor(Color.RED);
                } else if (fullHealthBoxes < yellowIndicator) {
                    g.setColor(Color.YELLOW);

                }
                g.fillRect(currentX, y, boxWidth, 10);

            } else if (i == fullHealthBoxes) {

                if (fullHealthBoxes < redWarning) {
                    g.setColor(Color.RED);
                } else if (fullHealthBoxes < yellowIndicator) {
                    g.setColor(Color.YELLOW);

                }

                g.drawRect(currentX, y, boxWidth, 10);
                g.fillRect(currentX, y, (int) ((current % divider) / divider * boxWidth), 10);

            } else {
                g.setColor(this.getBorderColor());

                g.drawRect(currentX, y, boxWidth, 10);

            }
            currentX += boxWidth + 5;
        }
    }

    /**
     * Draws inventory to screen
     * 
     * @param g:         graphics object
     * @param inventory: a set of inventory items
     * @param x:         x location
     * @param y:         y location
     */
    public void drawInventory(Graphics g, Set<Item> inventory, int x, int y) {

        g.drawRect(x, y + 5, 225, 100);
        int yOffset = y + 20;
        int xOffset = x + 10;

        for (Item item : inventory) {
            if (xOffset > 200) {
                yOffset += 100;
                xOffset = x + 10;
            }

            item.draw(xOffset, yOffset, g);

            xOffset += 70;

        }
    }

    /**
     * Draws player info to screen
     */
    @Override
    public void draw(Graphics g) {

        this.drawBorder(g);

        g.drawString("Ship", this.getXPixel() + 10, this.getYPixel() + 20);

        drawIndicator("Health", g, this.getXPixel() + 10, this.getYPixel() + 35,
                player.getCurrentHealth(), player.getMaxHealth(), 5.0f, 4, 2);

        drawIndicator("Fuel", g, this.getXPixel() + 10, this.getYPixel() + 60, player.getFuel(),
                player.getMaxFuel(), 1.0f, 5, 3);

        drawIndicator("Power", g, this.getXPixel() + 10, this.getYPixel() + 85, player.getPower(),
                player.getMaxPower(), 2f, 5, 2);

        g.setColor(this.getTextColor());
        g.drawString("> Credits: " + this.player.getCredits() + " ¤", this.getXPixel() + 10,
                this.getYPixel() + 115);

        drawInventory(g, this.player.getInventory(), this.getXPixel() + 10, this.getYPixel() + 120);

    }

}
