import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.util.ArrayList;

import processing.core.PVector;

public abstract class Animal extends Object {
    
    protected PVector vel;
    protected Area area;
    protected int scale;

    protected Animal() {
        super();
        setShape();
    }
    protected Animal(PVector pos, PVector dim) {
        super(pos, dim);
        setShape();
    }

    protected abstract void move(ArrayList<Carrot> carrots, Dimension s, ArrayList<Animal> animals);
    protected abstract void eat(ArrayList<Carrot> carrots);
    protected abstract void setShape();

    protected void draw(Graphics2D g) {
        if (Setting.drawBoundingBox) {
            g.setColor(Color.PINK);
            g.draw(getBoundary().getBounds2D());
        }
    }

    protected Shape getBoundary() {
        AffineTransform at = new AffineTransform();
        at.translate(pos.x, pos.y);
        at.rotate(vel.heading());
        if (vel.x < 0) at.rotate(Math.PI);
        if (vel.x > 0) at.scale(-1, 1);
        return at.createTransformedShape(area);
    }
}
