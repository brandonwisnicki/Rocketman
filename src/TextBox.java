import java.awt.Graphics;
import java.util.ArrayDeque;

import java.util.Deque;

public class TextBox extends UserInterface {

    Deque<String> lines;


    public TextBox(int px, int py, Deque<String> lines) {
        super(px, py, 250, 102);
        this.lines = lines;
    }
    
    public TextBox(int px, int py) {
        super(px, py, 250, 102);
        this.lines  = new ArrayDeque<String>();
    }


    public void addLine(String line) {
        if (lines.size() >= 7) {
            lines.pop();
        }
        lines.add(line);
    }

    public void clear() {
        lines.clear();
    }

    @Override
    public void draw(Graphics g) {

        int lineHeight = 13;

        this.drawBorder(g);
        
        int yOffset = this.getYPixel() + 17;

        g.setColor(this.getTextColor());

        for (String line : lines) {
            g.drawString("> " + line, this.getXPixel() + 15, yOffset);
            yOffset += lineHeight;
        }

    }

}
