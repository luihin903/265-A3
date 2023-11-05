import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public abstract class Forest {

    public static void draw(Graphics2D g2, Dimension s) {
        
        g2.setColor(Color.GREEN);
        Rectangle2D.Double rect = new Rectangle2D.Double(Setting.margin, Setting.margin, s.width - Setting.margin*2, s.height - Setting.margin*2);
        g2.fill(rect);
        
    }

}
