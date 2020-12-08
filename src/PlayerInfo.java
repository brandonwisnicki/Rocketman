import java.awt.Color;
import java.awt.Graphics;
import java.util.Set;

public class PlayerInfo {

    int px;
    int py;
    Color bgColor;
    Color borderColor;
    Color textColor;

    Ship player;

    public PlayerInfo(int px, int py, Ship player) {

        bgColor = Color.DARK_GRAY;
        borderColor = Color.WHITE;
        textColor = Color.WHITE;
        this.px = px;
        this.py = py;

        this.player = player;
    }

    public void drawIndicator(String label, Graphics g, int x, int y, float current, float max,
            float divider, int yellowIndicator, int redWarning) {
        float percentage = current / max;

        int boxCount = (int) (max / divider);

        int fullHealthBoxes = (int) (percentage * boxCount);
        int boxWidth = 175 / boxCount - 5;

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
                g.setColor(Color.WHITE);

                g.drawRect(currentX, y, boxWidth, 10);

            }
            currentX += boxWidth + 5;
        }
    }

    public void drawInventory(Graphics g, Set<Item> inventory, int x, int y) {

        g.drawRect(x, y + 5, 225, 100);
        int yOffset = y + 20;
        int xOffset = x + 10;

        int i = 1;
        for (Item item : inventory) {
            if (xOffset > 200) {
                yOffset += 100;
                xOffset = x + 10;
            }

            item.draw(xOffset, yOffset, g);

            xOffset += 70;
            i++;

        }
    }

    public void draw(Graphics g) {

        g.setColor(borderColor);
        g.drawRect(px, py, 250, 235);
        g.drawRect(px + 5, py + 5, 240, 225);

        g.drawString("Ship", px + 10, py + 20);

        drawIndicator("Health", g, px + 10, py + 35, player.getCurrentHealth(),
                player.getMaxHealth(), 5.0f, 4, 2);

        drawIndicator("Fuel", g, px + 10, py + 60, player.getFuel(), player.getMaxFuel(), 1.0f, 5,
                3);

        drawIndicator("Power", g, px + 10, py + 85, player.getPower(), player.getMaxPower(), 2f, 5,
                2);

        g.setColor(Color.WHITE);
        g.drawString("> Credits: " + this.player.getCredits() + " ¤", px + 10, py + 115);

        drawInventory(g, this.player.getInventory(), px + 10, py + 120);

    }

}
