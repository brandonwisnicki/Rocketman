import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public abstract class Item implements Comparable<Item> {

    private int amount;

    public Item(int amount) {
        this.amount = amount;
    }

    public void addItems(int amt) {
        this.amount += amt;
    }

    public int getAmount() {
        return amount;
    }

    public abstract String getName();
    
    public abstract String getTechnicalName();

    public int compareTo(Item o) {

        return this.getName().compareTo(o.getName());

    }

    public void draw(int x, int y, Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g.drawString("" + this.getAmount(), x, y);
        AffineTransform at = AffineTransform.getTranslateInstance(x + 12, y);
        at.concatenate(AffineTransform.getScaleInstance(16 / 32.0f, 16 / 32.0f));
        g2d.drawImage(getSprite(), at, null);

        g.drawString(this.getName(), x, y + 24);

    }

    public abstract BufferedImage getSprite();

}
