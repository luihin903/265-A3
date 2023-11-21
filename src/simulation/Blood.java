package simulation;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import main.RabbitPanel;
import others.Setting;
import processing.core.PVector;

public class Blood extends others.Object{
    
    private PVector vel;
    private double speed;
    private static final PVector default_dim = new PVector(20, 10);
    private static ArrayList<Blood> bloods = new ArrayList<Blood>();

    Rectangle2D.Double body;

    public Blood(PVector pos, PVector vel) {
        super(pos, default_dim);
        this.vel = vel;
        this.speed = 8;

        body = new Rectangle2D.Double(-dim.x/2, -dim.y/2, dim.x, dim.y);
    }

    public static void spawn(PVector pos) {
        for (int i = 0; i < 8; i ++) {
            bloods.add(new Blood(pos.copy(), PVector.fromAngle((float) (2*Math.PI/8*i))));
        }
    }

    public static void drawAll(Graphics2D g) {
        for (Blood b : bloods) {
            b.draw(g);
        }
    }

    protected void draw(Graphics2D g) {
        AffineTransform at = g.getTransform();
        g.translate(pos.x, pos.y);
        g.rotate(vel.heading());

        g.setColor(Color.RED);
        g.fill(body);

        g.setTransform(at);
    }

    public static void updateAll() {
        for (int i = 0; i < bloods.size(); i ++) {
            Blood b = bloods.get(i);
            b.move();

            if (b.pos.x < Setting.margin) bloods.remove(i);
            else if (b.pos.x > RabbitPanel.size.width - Setting.margin) bloods.remove(i);
            else if (b.pos.y < Setting.margin) bloods.remove(i);
            else if (b.pos.y > RabbitPanel.size.height - Setting.margin) bloods.remove(i);
        }
    }

    private void move() {
        vel.normalize();
        vel.mult((float) speed);
        pos.add(vel);
    }
}
