package simulation;
/*
 * This is a prey
 * It includes some fields which are not in common with the predator
 * Some methods are overrided if they have some shared code with the predator,
 * some methods are added if not
 */


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import others.Setting;
import others.Util;
import processing.core.PVector;

public class Rabbit extends Animal {

    private Color color;
    private int escaping = 0;
    public static final PVector default_dim = new PVector(50, 100);
    public static int amount;

    // Shapes
    private Ellipse2D.Double bottomFoot;
    private Ellipse2D.Double topFoot;
    private Ellipse2D.Double bottomHand;
    private Ellipse2D.Double topHand;
    private Ellipse2D.Double leftEar;
    private Ellipse2D.Double rightEar;
    private Ellipse2D.Double body;
    private Ellipse2D.Double head;
    private Ellipse2D.Double tail;
    private Ellipse2D.Double leftEye;
    private Ellipse2D.Double rightEye;
    private Line2D.Double[] face;

    public Rabbit(PVector pos, PVector dim, int speed, float scale) {
        super(pos, dim, speed, scale);
        this.color = Color.WHITE;
        this.type = "Rabbit";
        amount ++;
    }

    @Override
    protected void setShape() {
        super.setShape();

        // feet
        bottomFoot = new Ellipse2D.Double((int) (-dim.x/2), (int) (dim.y/12*5), (int) (dim.x/6*4), (int) (dim.y/12));
        topFoot = new Ellipse2D.Double((int) (-dim.x/6*2.5), (int) (dim.y/12*4), (int) (dim.x/6*4), (int) (dim.y/12));

        // hands
        bottomHand = new Ellipse2D.Double((int) (-dim.x/2), (int) (dim.y/12*1), (int) (dim.x/6*3), (int) (dim.y/12));
        topHand = new Ellipse2D.Double((int) (-dim.x/6*2), 0, (int) (dim.x/6*3), (int) (dim.y/12));

        // ears
        leftEar = new Ellipse2D.Double((int) (-dim.x/6*1.5), (int) (-dim.y/2), (int) (dim.x/6), (int) (dim.y/12*4));
        rightEar = new Ellipse2D.Double((int) (dim.x/6*0.5), (int) (-dim.y/2), (int) (dim.x/6), (int) (dim.y/12*4));

        // body
        body = new Ellipse2D.Double((int) (-dim.x/6*2), 0, (int) (dim.x/6*4), (int) (dim.y/2));
        
        // head
        head = new Ellipse2D.Double((int) (-dim.x/6*2), (int) (-dim.y/12*4), (int) (dim.x/6*4), (int) (dim.y/12*4));

        // tail
        tail = new Ellipse2D.Double((int) (dim.x/6*2), (int) (dim.y/12*3), (int) (dim.x/6), (int) (dim.y/12));

        // eyes
        leftEye = new Ellipse2D.Double((int) (-dim.x/6), (int) (-dim.y/12*3), (int) (dim.x/6*0.5), (int) (dim.y/12*0.75));
        rightEye = new Ellipse2D.Double((int) (dim.x/6*0.5), (int) (-dim.y/12*3), (int) (dim.x/6*0.5), (int) (dim.y/12*0.75));
        
        // face
        face = new Line2D.Double[5];
        face[0] = new Line2D.Double((int) (-dim.x/6*0.5), (int) (-dim.y/12*2), 0, (int) (-dim.y/12*1.5));
        face[1] = new Line2D.Double((int) (dim.x/6*0.5), (int) (-dim.y/12*2), 0, (int) (-dim.y/12*1.5));
        face[2] = new Line2D.Double(0, (int) (-dim.y/12*1.5), 0, (int) (-dim.y/12*1));
        face[3] = new Line2D.Double(0, (int) (-dim.y/12*1), (int) (-dim.x/6*0.5), (int) (-dim.y/12*0.75));
        face[4] = new Line2D.Double(0, (int) (-dim.y/12*1), (int) (dim.x/6*0.5), (int) (-dim.y/12*0.75));
    
        area = new Area(body);
        area.add(new Area(head));
        area.add(new Area(leftEar));
        area.add(new Area(rightEar));
        area.add(new Area(topHand));
        area.add(new Area(bottomHand));
        area.add(new Area(topFoot));
        area.add(new Area(bottomFoot));
        area.add(new Area(tail));
    }

    @Override
    public void draw(Graphics2D g2) {
        if (Setting.drawBoundingBox) {
            g2.setColor(Color.PINK);
            g2.draw(getBoundary().getBounds2D());
        }

        AffineTransform af = g2.getTransform();

        g2.translate(pos.x, pos.y);

        // I don't know if this what you mean by "facing its moving direction", but the rabbits look weird.
        g2.rotate(vel.heading());
        g2.setColor(Color.RED); g2.draw(fov);
        if (vel.x < 0) g2.rotate(Math.PI);
        if (vel.x > 0) g2.scale(-1, 1);

        // feet
        g2.setColor(color);
        if (state == SICK) g2.setColor(Color.LIGHT_GRAY);
        g2.fill(bottomFoot);
        g2.fill(topFoot);
        g2.setColor(Color.BLACK);
        g2.draw(bottomFoot);
        g2.draw(topFoot);
        
        // hands
        g2.setColor(color);
        if (state == SICK) g2.setColor(Color.LIGHT_GRAY);
        g2.fill(bottomHand);
        g2.fill(topHand);
        g2.setColor(Color.BLACK);
        g2.draw(bottomHand);
        g2.draw(topHand);

        // ears
        g2.setColor(color);
        if (state == SICK) g2.setColor(Color.LIGHT_GRAY);
        g2.fill(leftEar);
        g2.fill(rightEar);
        g2.setColor(Color.BLACK);
        g2.draw(leftEar);
        g2.draw(rightEar);

        // body
        g2.setColor(color);
        if (state == SICK) g2.setColor(Color.LIGHT_GRAY);
        g2.fill(body);
        g2.setColor(Color.BLACK);
        g2.draw(body);

        // head
        g2.setColor(color);
        if (state == SICK) g2.setColor(Color.LIGHT_GRAY);
        g2.fill(head);
        g2.setColor(Color.BLACK);
        g2.draw(head);

        // tail
        g2.setColor(color);
        if (state == SICK) g2.setColor(Color.LIGHT_GRAY);
        g2.fill(tail);
        g2.setColor(Color.BLACK);
        g2.draw(tail);

        // eyes
        g2.setColor(Color.RED);
        g2.fill(leftEye);
        g2.fill(rightEye);

        // face
        g2.setColor(Color.BLACK);

        for (int i = 0; i < 5; i ++) g2.draw(face[i]);

        g2.setTransform(af);

        if (Setting.drawInfo) drawInfo(g2);
    }
    
    // Overload
    public void update(ArrayList<Carrot> carrots, Dimension s, ArrayList<Animal> animals) {
        super.update();
        move(carrots, s, animals);
    }

    // Overload
    public void move(ArrayList<Carrot> carrots, Dimension s, ArrayList<Animal> animals) {
        PVector accel = observe(animals);
        if (moving) {
            accel = seek(accel, carrots);
            super.move(accel, s, Util.getHunter(animals));
        }
        if (escaping > 0) {
            pos.add(vel); // speed up
            escaping --;
        }
    }

    // @Override
    // protected PVector checkCollision(PVector accel, Dimension s, ArrayList<Animal> animals) {
    //     accel = super.checkCollision(accel, s, animals);

    //     // for (Animal r : animals) {
    //     //     if (this.scale < r.scale && getFOV().intersects(r.getBoundary().getBounds2D())) {
    //     //         if (r.pos.x < pos.x) accel.add(1, 0);
    //     //         if (r.pos.x > pos.x) accel.add(-1, 0);
    //     //         if (r.pos.y < pos.y) accel.add(0, 1);
    //     //         if (r.pos.y > pos.y) accel.add(0, -1);
    //     //         escaping = 30;
    //     //     }
    //     // }
    //     // if (escaping != 0) escaping --;

    //     return accel;
    // }

    protected PVector seek(PVector accel, ArrayList<Carrot> carrots) {
        
        if (carrots.size() != 0) {
            // Carrot[] targets = new Carrot[2];
            
            // // load 1 or 2 carrot(s) into the array
            // targets[0] = carrots.get(0);
            // if (carrots.size() > 1) {
            //     if (getAFC(carrots.get(1)) > getAFC(targets[0])) {
            //         targets[1] = targets[0];
            //         targets[0] = carrots.get(1);
            //     }
            //     else {
            //         targets[1] = carrots.get(1);
            //     }
            // }

            // for (Carrot c : carrots) {
            //     float newAFC = getAFC(c);
            //     if (newAFC > getAFC(targets[0])) {
            //         if (carrots.size() > 1) targets[1] = targets[0];
            //         targets[0] = c;
            //     }
            //     else if (carrots.size() > 1) {
            //         if (newAFC > getAFC(targets[1])) targets[1] = c;
            //     }
            // }

            // vel = targets[escaping > 0 ? 1 : 0].pos.copy();

            Carrot target = carrots.get(0);
            for (Carrot c : carrots) {
                if (getAFC(c) > getAFC(target)) {
                    target = c;
                }
            }

            accel.add(PVector.sub(target.getPos().copy(), this.pos).normalize());
        }

        return accel.copy();
    }

    private PVector observe(ArrayList<Animal> animals) {
        PVector accel = new PVector(0, 0);
        for (Animal a : animals) {
            if (a instanceof Lion) {
                if (this.getFOV().intersects(a.getBoundary().getBounds2D())) {
                    accel.add(PVector.sub(this.pos, a.getPos()).normalize().mult(3));
                    escaping = 60;
                    moving = true;
                }
            }
        }
        return accel;
    }

    public void eat(ArrayList<Carrot> carrots) {
        start();
        
        for (int i = 0; i < carrots.size(); i ++) {
            if (carrots.get(i) != null) {
                if (this.getBoundary().intersects(carrots.get(i).getBoundary().getBounds2D())) {
                    stop();
                    carrots.get(i).eat(i, this);
                }
            }
        }
    }

    public void start() {
        moving = true;
    }

    public void stop() {
        moving = false;
    }

    private float getAFC(Carrot c) {
        return c.getSize()*10 / PVector.dist(this.pos, c.getPos());
    }

}
