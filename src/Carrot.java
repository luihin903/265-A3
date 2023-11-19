import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.event.MouseEvent;

import java.util.ArrayList;

import processing.core.PVector;

public class Carrot extends Object {
    
    private int size = 100;
    private int originalSize = size;
    private Area area;
    private static ArrayList<Carrot> carrots = new ArrayList<Carrot>();
    private static boolean adding;
    private static final PVector default_dim = new PVector(100, 100);

    // Shapes
    Ellipse2D.Double ellipse;
    Polygon polygon;

    // a constructor that initializes each of the fields with some parameter
    public Carrot(PVector pos, PVector dim, int size, ArrayList<Carrot> carrots) {
        super();
        this.pos = pos;
        this.dim = dim;
        this.size = size;
        this.originalSize = size;
        Carrot.carrots = carrots;
    }

    private Carrot(MouseEvent e) {
        super(new PVector(e.getX(), e.getY()), default_dim);

        setShape();
    }

    private Carrot(PVector pos, int size) {
        super(pos, default_dim.copy().mult(size/default_dim.x));

        setShape();
    }

    public static void init(int n) {
        for (int i = 0; i < n; i ++) {
            spawn();
        }
    }

    private static void spawn() {
        int size = Util.random(50, 150);
    
        PVector pos = Util.random(RabbitPanel.size, default_dim.copy().mult(size/default_dim.x));
        carrots.add(new Carrot(pos, size));
    }

    private void setShape() {
        ellipse = new Ellipse2D.Double((int) (-dim.x/4), (int) (-dim.y/2), (int) (dim.x/2), (int) (dim.y/4));
        int[] xPoints = {(int) (-dim.x/4), 0, (int) (dim.x/4)};
        int[] yPoints = {(int) (-dim.y/8*3), (int) (dim.y/2), (int) (-dim.y/8*3)};
        polygon = new Polygon(xPoints, yPoints, 3);

        area = new Area(ellipse);
        area.add(new Area(polygon));
    }

    public static ArrayList<Carrot> get() {
        return carrots;
    }

    public static void drawAll(Graphics2D g2) {
        for (Carrot c : carrots) {
            if (c != null) {
                c.draw(g2);
            }
        }
        // System.out.println(carrots);
    }

    @Override
    public void draw(Graphics2D g2) {

        if (Setting.drawBoundingBox) {
            g2.setColor(Color.PINK);
            g2.draw(getBoundary().getBounds2D());
        }

        AffineTransform af = g2.getTransform();

        g2.translate(pos.x, pos.y);
        g2.rotate(Math.toRadians(45));
        g2.scale((double) (size / default_dim.x), (double) (size / default_dim.y));

        g2.setColor(new Color(255, 127, 39));
        g2.fill(ellipse);
        g2.fill(polygon);

        g2.setTransform(af);
    }

    public void eat(int i, Rabbit rabbit) {
        this.size --;
        if (this.size == 0) {
            rabbit.addEnergy(this.originalSize / 2);
            carrots.remove(i);
            rabbit.start();
            Carrot.spawn();
        }
    }

    public static void start(MouseEvent e, Dimension s) {
        carrots.add(new Carrot(e));
        adding = true;
    }

    public static void grow(MouseEvent e) {
        if (adding) {
            Carrot carrot = carrots.get(carrots.size() - 1);
            if (carrot.size < 150) carrot.size ++;
            carrot.pos = new PVector(e.getX(), e.getY());
        }
    }

    public static void grow() {
        if (adding) {
            Carrot carrot = carrots.get(carrots.size() - 1);
            if (carrot.size < 150) carrot.size ++;
        }
    }

    public static void stop() {
        adding = false;
    }

    public boolean hit(MouseEvent e) {
        return PVector.dist(new PVector(e.getX(), e.getY()), this.pos) < size/2;
    }

    public int getSize() {
        return size;
    }

    public int getOriginalSize() {
        return originalSize;
    }

    public Shape getBoundary() {
        AffineTransform at = new AffineTransform();
        at.translate(pos.x, pos.y);
        at.rotate(Math.toRadians(45));
        at.scale((double) (size / default_dim.x), (double) (size / default_dim.y));
        return at.createTransformedShape(area);
    }
}
