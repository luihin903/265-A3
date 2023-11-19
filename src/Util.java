import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

import processing.core.PVector;

public abstract class Util {
    
    // return a double within the given interval
    public static double random(double a, double b) {
        return Math.random() * (b-a) + a;
    }
    
    // return a float within the given interval
    public static float random(float a, float b) {
        return (float) (Math.random() * (b-a) + a);
    }

    // return an integer within the given interval
    public static int random(int a, int b) {
        return (int) random((double) a, (double) b);
    }

    // return a PVector within the window size
    public static PVector random(Dimension s) {
        float x = random(0, s.width);
        float y = random(0, s.height);
        return new PVector(x, y);
    }

    // return a PVector within the environment
    public static PVector random(Dimension s, PVector dim) {
        float x, y;
        
        if (s.width == Setting.default_size.x && s.height == Setting.default_size.y) {
            x = random(Setting.margin + dim.x/2 + 14, s.width - Setting.margin - dim.x/2 - 14); // my panel is 14px thinner than initial size
            y = random(Setting.margin + dim.y/2 + 100, s.height - Setting.margin - dim.y/2 - 100); // my panel is 100px shorter than initial size
        }
        else {
            x = random(Setting.margin + dim.x/2, s.width - Setting.margin - dim.x/2); // my panel is 14px thinner than initial size
            y = random(Setting.margin + dim.y/2, s.height - Setting.margin - dim.y/2); // my panel is 100px shorter than initial size
        }
        
        return new PVector(x, y);
    }

    // return a random color
    public static Color random() {
        return new Color(random(0, 255), random(0, 255), random(0, 255));
    }

    // return the opposite color
    public static Color opposite(Color color) {
        return new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue());
    }

    // return the width of a given string under the default font
    public static int getStringWidth(Graphics2D g, String text) {
        return g.getFontMetrics(Setting.font).stringWidth(text);
    }

}
