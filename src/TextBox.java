import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class TextBox {

    ArrayList<String> lines;
    int px;
    int py;
    Color bgColor;
    Color borderColor;
    Color textColor;

    public TextBox(int px, int py, ArrayList<String> lines) {
        this.lines = lines;

        bgColor = Color.DARK_GRAY;
        borderColor = Color.WHITE;
        textColor = Color.WHITE;
        this.px = px;
        this.py = py;

    }

    public void addLine(String line) {
        if (lines.size() >= 7) {
            clear();
        }
        lines.add(line);
    }

    public void clear() {
        lines = new ArrayList<String>();
    }

    public void draw(Graphics g) {

        int lineHeight = 13;

        g.setColor(borderColor);
        g.drawRect(px, py, 250, 102);
        g.drawRect(px + 5, py + 5, 240, 92);

        int yOffset = py + 17;

        g.setColor(textColor);

        for (String line : lines) {
            g.drawString("> " + line, px + 15, yOffset);
            yOffset += lineHeight;
        }

    }

}
