import java.awt.Color;
import java.awt.Graphics;

public abstract class UserInterface extends GameObject {

    private Color borderColor;
    private Color textColor;

    public UserInterface(int px, int py, int width, int height) {
        super(px, py, width, height);
        borderColor = Color.WHITE;
        textColor = Color.WHITE;

    }

    public void drawBorder(Graphics g) {

        g.setColor(borderColor);
        g.drawRect(this.getXPixel(), this.getYPixel(), this.getWidth(), this.getHeight());
        g.drawRect(this.getXPixel() + 5, this.getYPixel() + 5, this.getWidth() - 10,
                this.getHeight() - 10);

    }

    /**
     * Getters
     */

    public Color getBorderColor() {
        return borderColor;
    }

    public Color getTextColor() {
        return textColor;
    }

}
