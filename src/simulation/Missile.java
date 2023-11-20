package simulation;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import main.RabbitPanel;
import others.Setting;
import processing.core.PVector;

public class Missile extends others.Object {

    public static final PVector default_dim = new PVector(20, 10);
    private double speed;
    private Area area;
    private PVector vel = new PVector(10, 0);

    Rectangle2D.Double body;

    public Missile(PVector pos) {
        super(pos, default_dim);
        speed = 5;
        setShape();
    }

    private void setShape() {
        body = new Rectangle2D.Double(-dim.x/2, -dim.y/2, dim.x, dim.y);

        area = new Area(body);
    }

    @Override
    public void draw(Graphics2D g) {
        
        AffineTransform at = g.getTransform();
        g.translate(pos.x, pos.y);
        g.rotate(vel.heading());

        g.setColor(Color.BLACK);
        g.fill(body);

        g.setTransform(at);
    }
    
    public boolean update(ArrayList<Animal> animals) {
        move(animals);

        if (pos.x < Setting.margin) return true;
        else if (pos.x > RabbitPanel.size.width-Setting.margin) return true;
        else if (pos.y < Setting.margin) return true;
        else if (pos.y > RabbitPanel.size.height-Setting.margin) return true;

        for (int i = 0; i < animals.size(); i ++) {
            if (this.collides(animals.get(i))) {
                if (! (animals.get(i) instanceof Hunter)) {
                    if (animals.get(i) instanceof Lion) Lion.amount --;
                    else Rabbit.amount --;
                    animals.remove(i);
                    return true;
                }
            }
        }

        return false;
    }

    private void move(ArrayList<Animal> animals) {
        Lion target = null;

        for (Animal a : animals) {
            if (a instanceof Lion) {
                if (target == null) {
                    target = (Lion) a;
                }
                else {
                    if (a.getEnergy() > target.getEnergy()) {
                        target = (Lion) a;
                    }
                }
            }
        }
    
        if (target != null) {
            vel = PVector.sub(target.getPos(), this.pos).normalize().mult((float) speed);
        }
        pos.add(vel);
        
    }

    private boolean collides(Animal a) {
        return getBoundary().intersects(a.getBoundary().getBounds2D());
    }

    private Shape getBoundary() {
        AffineTransform at = new AffineTransform();
        at.translate(pos.x, pos.y);
        at.rotate(vel.heading());
        return at.createTransformedShape(area);
    }

}
