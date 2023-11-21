package simulation.background;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import others.Object;

import java.awt.Polygon;

import processing.core.PVector;

public class Tree extends Object {
    
    private static Tree[] trees;
    private static int capacity;
    private static final PVector default_dim = new PVector(100, 200);

    public static void init(int amount, Dimension s) {
        capacity = amount;
        trees = new Tree[capacity];

        for (int i = 0; i < capacity; i ++) {
            trees[i] = new Tree(s);
        }
    }

    public Tree(PVector pos, PVector dim) {
        super(pos, dim);
    }

    public Tree(Dimension s) {
        super(s, default_dim);
    }

    public static void drawAll(Graphics2D g2) {
        for (Tree t : trees) {
            t.draw(g2);
        }
    }

    @Override
    public void draw(Graphics2D g2) {

        AffineTransform af = g2.getTransform();

        g2.translate(pos.x, pos.y);

        g2.setColor(new Color(147, 94, 40));
        Rectangle2D.Double rect = new Rectangle2D.Double((int) (-dim.x/4), 0, (int) (dim.x/2), (int) (dim.y/2));
        g2.fill(rect);
        
        g2.setColor(new Color(0, 155, 0));
        int[] xPoints = {(int) (-dim.x/2), 0, (int) (dim.x/2)};
        int[] yPoints = {0, (int) (-dim.y/2), 0};
        Polygon polygon = new Polygon(xPoints, yPoints, 3);
        g2.fill(polygon);

        g2.setColor(Color.BLACK);
        g2.draw(polygon);

        g2.setTransform(af);
    }
}
