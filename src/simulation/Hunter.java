package simulation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import others.Setting;
import processing.core.PVector;

public class Hunter extends Animal {
    
    private static final PVector default_dim = new PVector(50, 150);
    private ArrayList<Missile> missiles;
    protected double hp;

    Ellipse2D.Double head;
    Rectangle2D.Double body;
    Ellipse2D.Double leftEye;
    Ellipse2D.Double rightEye;
    Rectangle2D.Double leftFeet;
    Rectangle2D.Double rightFeet;

    public Hunter() {
        super(new PVector(Setting.margin + default_dim.x/2, Setting.margin + default_dim.y/2), default_dim, 5, 1);
        type = "Hunter";
        vel = new PVector(0, (float) speed);
        missiles = new ArrayList<Missile>();
        hp = 100;
    }

    @Override
    protected void setShape() {
        super.setShape();

        head = new Ellipse2D.Double(-dim.x/2, -dim.y/2, dim.x, dim.y/3);
        body = new Rectangle2D.Double(-dim.x/2, -dim.y/6, dim.x, dim.y/3);
        leftEye = new Ellipse2D.Double(-dim.x/10*2, -dim.y/2.5, dim.x/10, dim.y/30);
        rightEye = new Ellipse2D.Double(dim.x/10, -dim.y/2.5, dim.x/10, dim.y/30);
        leftFeet = new Rectangle2D.Double(-dim.x/5*1.5, dim.y/6, dim.x/5, dim.y/3);
        rightFeet = new Rectangle2D.Double(dim.x/5*0.5, dim.y/6, dim.x/5, dim.y/3);

        area = new Area(head);
        area.add(new Area(body));
        area.add(new Area(leftFeet));
        area.add(new Area(rightFeet));
    }

    public void draw(Graphics2D g) {
        if (Rabbit.amount > Setting.rabbits/2 || Lion.amount < Setting.lions/2) {
            return;
        }

        if (Setting.drawBoundingBox) {
            g.setColor(Color.PINK);
            g.draw(getBoundary().getBounds2D());
        }

        AffineTransform at = g.getTransform();
        g.translate(pos.x, pos.y);

        g.setColor(new Color(207, 157, 137));
        g.fill(head);
        g.setColor(new Color(28, 69, 25));
        g.fill(body);
        g.fill(leftFeet);
        g.fill(rightFeet);
        g.setColor(Color.BLACK);
        g.fill(leftEye);
        g.fill(rightEye);

        g.setTransform(at);

        drawInfo(g);

        for (Missile m : missiles) {
            m.draw(g);
        }
    }
    
    // Overload
    public void update(Dimension s, ArrayList<Animal> animals) {
        super.move(vel, s);
        for (int i = 0; i < missiles.size(); i ++) {
            if (missiles.get(i).update(animals)) {
                missiles.remove(i);
            }
        }
    }

    protected PVector checkCollision(PVector accel, Dimension s) {
        int margin = Setting.margin;

        Rectangle2D.Double top = new Rectangle2D.Double(0, 0, s.width, margin);
        Rectangle2D.Double bottom = new Rectangle2D.Double(0, s.height-margin, s.width, margin);

        if (this.getBoundary().intersects(top.getBounds2D())) accel.add(0, 10);
        if (this.getBoundary().intersects(bottom.getBounds2D())) accel.add(0, -10);

        return accel.copy();
    }

    public void fire() {
        System.out.println("fire");
        missiles.add(new Missile(pos.copy()));
    }
}
