package simulation;
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

import others.Object;
import others.Setting;
import others.Util;
import processing.core.PVector;

public abstract class Animal extends Object {
    
    protected PVector vel;
    protected double speed;
    protected double scale;
    protected boolean moving = true;
    protected Area area;
    protected Arc2D.Double fov;
    protected int state = 2;
    protected double energy;
    protected String type = "Animal"; // Class Name

    // FSM
    protected final double FULL_ENERGY = 100;
    
    protected final int FULL = 2;
    protected final int SICK = 1;
    protected final int DEAD = 0;

    protected Animal() {
        super();
        setShape();
    }
    protected Animal(PVector pos, PVector dim, double speed, double scale) {
        super(pos, dim);
        this.vel = new PVector(Util.random(-100, 100), Util.random(-100, 100));
        this.speed = speed;
        this.scale = scale;
        this.energy = FULL_ENERGY;
        setShape();
    }
    
    protected void setShape() {
        double sight = 100 + dim.x * dim.y * speed / 512;
        fov = new Arc2D.Double(-sight, -sight, sight*2, sight*2, -55, 110, Arc2D.PIE);
    }

    public abstract void draw(Graphics2D g);

    protected void drawInfo(Graphics2D g) {
        AffineTransform at = g.getTransform();
        g.translate(pos.x - 50, pos.y - dim.y/2 - 64);
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, 100, 64);

        g.setColor(Color.BLACK);
        g.setFont(Setting.font);

        String text = String.format(type);
        int sw = Util.getStringWidth(g, type);
        g.drawString(type, (100-sw) / 2, 16);
        
        g.setColor(Color.WHITE);
        if (type == "Hunter") {
            text = String.format("HP: %.0f/100", ((Hunter) this).hp);
        }
        else {
            text = String.format("Energy: %.1f", energy);
        }
        sw = Util.getStringWidth(g, text);
        g.drawString(text, (100-sw) / 2, 32);

        text = String.format("Speed: %.1f", speed);
        sw = Util.getStringWidth(g, text);
        g.drawString(text, (100-sw)/2, 48);

        text = String.format("Size: %.1f", scale);
        sw = Util.getStringWidth(g, text);
        g.drawString(text, (100-sw) / 2, 64);

        g.setTransform(at);
    }

    protected void update() {
        updateEnergy();
        updateState();
    };

    protected void move(PVector accel, Dimension s) {
        
        accel = checkCollision(accel, s);
        vel.add(accel.mult(0.5f));
        vel.normalize();
        vel.mult((float) speed);
        pos.add(vel);

        if (type != "Hunter") energy -= (scale * speed) / 20;
        
    }

    protected PVector checkCollision(PVector accel, Dimension s) {
        int margin = Setting.margin;

        Rectangle2D.Double top = new Rectangle2D.Double(margin, 0, s.width-margin*2, margin);
        Rectangle2D.Double bottom = new Rectangle2D.Double(margin, s.height-margin, s.width-margin*2, margin);
        Rectangle2D.Double left = new Rectangle2D.Double(0, margin, margin, s.height-margin*2);
        Rectangle2D.Double right = new Rectangle2D.Double(s.width-margin, margin, margin, s.height-margin*2);

        if (getFOV().intersects(top)) accel.add(0, 1);
        if (getFOV().intersects(bottom)) accel.add(0, -1);
        if (getFOV().intersects(left)) accel.add(1, 0);
        if (getFOV().intersects(right)) accel.add(-1, 0);

        return accel.copy();
    }

    protected Shape getBoundary() {
        AffineTransform at = new AffineTransform();
        at.translate(pos.x, pos.y);
        if (type != "Hunter") at.rotate(vel.heading());
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

    protected double getScale() {
        return scale;
    }

    protected double getEnergy() {
        return energy;
    }

    public int getState() {
        return state;
    }

    protected void updateEnergy() {

        if (energy > FULL_ENERGY) {
            scale *= (energy / FULL_ENERGY);
            energy = FULL_ENERGY;
        }
    }

    protected void updateState() {

        if (energy < 0) {
            state = DEAD;
        }
        else if (energy < FULL_ENERGY/10) {
            state = SICK;
        }
        else {
            state = FULL;
        }

    }

    protected void addEnergy(double amount) {
        energy += amount;
    }
}
