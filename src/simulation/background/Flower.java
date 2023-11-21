package simulation.background;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;

import others.Object;
import others.Util;
import processing.core.PVector;

public class Flower extends Object {
    
    private static Flower[] flowers;
    private static int amount;
    private static final PVector default_dim = new PVector(100, 100);
    private Color color;

    public static void init(int amount, Dimension s) {
        Flower.amount = amount;
        flowers = new Flower[Flower.amount];

        for (int i = 0; i < Flower.amount; i ++) {
            flowers[i] = new Flower(s);
        }
    }

    private Flower(Dimension s) {
        super(s, default_dim);
        color = Util.random();
    }

    public static void drawAll(Graphics2D g2) {
        for (Flower f : flowers) {
            f.draw(g2);
        }
    }

    @Override
    public void draw(Graphics2D g2) {

        // drawBoundingBox(g2);

        AffineTransform af = g2.getTransform();
        g2.translate(pos.x, pos.y);

        g2.setColor(Color.BLACK);
        Arc2D.Double stem = new Arc2D.Double(0, 0, (int) (dim.x/2), (int) (dim.y), 120, 60, Arc2D.OPEN);
        g2.draw(stem);

        AffineTransform af2 = g2.getTransform();
        g2.translate((int) (dim.x/8), (int) (dim.y/8));

        g2.setColor(color);
        for (int i = 0; i < 8; i ++) {
            Arc2D.Double arc = new Arc2D.Double((int) (-dim.x/32), (int) (-dim.y/8), (int) (dim.x/16), (int) (dim.y/8), -45, 270, Arc2D.OPEN);
            g2.fill(arc);
            g2.rotate(Math.toRadians(45));
        }

        g2.setColor(Util.opposite(color));
        Ellipse2D.Double ellipse = new Ellipse2D.Double((int) (-dim.x/16), (int) (-dim.y/16), (int) (dim.x/8), (int) (dim.y/8));
        g2.fill(ellipse);


        g2.setTransform(af2);
        g2.setTransform(af);
    }
}
