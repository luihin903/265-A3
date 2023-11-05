/*
 * This is the superclass
 * It includes all the common fields and methods
 */


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import processing.core.PVector;

public abstract class Animal extends Object {
    
    protected PVector vel;
    protected double speed;
    protected double scale;
    protected boolean moving = true;
    protected Area area;
    protected Arc2D.Double fov;

    protected Animal() {
        super();
        setShape();
    }
    protected Animal(PVector pos, PVector dim, double speed, double scale) {
        super(pos, dim);
        this.vel = new PVector(Util.random(-100, 100), Util.random(-100, 100));
        this.speed = speed;
        this.scale = scale;
        setShape();
    }

    protected abstract void eat(ArrayList<Carrot> carrots);
    
    protected void setShape() {
        double sight = 50 + dim.x * speed * 0.5f;
        fov = new Arc2D.Double(-sight, -sight, sight*2, sight*2, -55, 110, Arc2D.PIE);
    }

    protected void draw(Graphics2D g) {
        if (Setting.drawBoundingBox) {
            g.setColor(Color.PINK);
            g.draw(getBoundary().getBounds2D());
        }
    }

    protected void move(ArrayList<Carrot> carrots, Dimension s, ArrayList<Animal> animals) {

        if (moving) {
            PVector accel = checkCollision(s, animals);

            vel.add(accel.mult(0.5f));
            seek(carrots);
            vel.normalize();
            vel.mult((float) speed);
            pos.add(vel);
        }
    }

    protected PVector checkCollision(Dimension s, ArrayList<Animal> animals) {
        int margin = Setting.margin;

        Rectangle2D.Double top = new Rectangle2D.Double(margin, 0, s.width-margin*2, margin);
        Rectangle2D.Double bottom = new Rectangle2D.Double(margin, s.height-margin, s.width-margin*2, margin);
        Rectangle2D.Double left = new Rectangle2D.Double(0, margin, margin, s.height-margin*2);
        Rectangle2D.Double right = new Rectangle2D.Double(s.width-margin, margin, margin, s.height-margin*2);

        PVector accel = new PVector(0, 0);

        if (getFOV().intersects(top)) accel.add(0, 1);
        if (getFOV().intersects(bottom)) accel.add(0, -1);
        if (getFOV().intersects(left)) accel.add(1, 0);
        if (getFOV().intersects(right)) accel.add(-1, 0);

        return accel.copy();
    }

    protected void seek(ArrayList<Carrot> carrots) {}

    protected Shape getBoundary() {
        AffineTransform at = new AffineTransform();
        at.translate(pos.x, pos.y);
        at.rotate(vel.heading());
        if (vel.x < 0) at.rotate(Math.PI);
        if (vel.x > 0) at.scale(-1, 1);
        return at.createTransformedShape(area);
    }

    protected Shape getFOV() {
        AffineTransform at = new AffineTransform();
        at.translate(pos.x, pos.y);
        at.rotate(vel.heading());
        return at.createTransformedShape(fov);
    }
}
