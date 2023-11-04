import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import processing.core.PVector;

public class Rabbit extends Animal {

    private int speed;
    private Color color;
    private boolean moving = true;
    private int escaping = 0;
    private float scale;
    public static final PVector default_dim = new PVector(50, 100);

    // Shapes
    Ellipse2D.Double bottomFoot;
    Ellipse2D.Double topFoot;
    Ellipse2D.Double bottomHand;
    Ellipse2D.Double topHand;
    Ellipse2D.Double leftEar;
    Ellipse2D.Double rightEar;
    Ellipse2D.Double body;
    Ellipse2D.Double head;
    Ellipse2D.Double tail;
    Ellipse2D.Double leftEye;
    Ellipse2D.Double rightEye;
    Line2D.Double[] face;
    Arc2D.Double fov;

    // a constructor that initializes each of the fields with some parameter
    public Rabbit(PVector pos, PVector vel, PVector dim, int speed, Color color, boolean moving) {
        super();
        this.pos = pos;
        this.vel = vel;
        this.dim = dim;
        this.speed = speed;
        this.color = color;
        this.moving = moving;
    }

    public Rabbit(PVector pos, PVector dim, int speed, Color color, float scale) {
        super(pos, dim);
        this.speed = speed;
        this.vel = new PVector(Util.random(-100, 100), Util.random(-100, 100));
        this.color = color;
        this.scale = scale;

        setShape();

    }

    protected void setShape() {
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

        float sight = 50 + dim.x * speed * 0.5f;
        fov = new Arc2D.Double(-sight, -sight, sight*2, sight*2, -55, 110, Arc2D.PIE);
    }

    public void draw(Graphics2D g2) {
        if (RabbitApp.drawBoundingBox) {
            g2.setColor(Color.PINK);
            g2.draw(getBoundary().getBounds2D());
            g2.draw(getFOV().getBounds2D());
        }

        AffineTransform af = g2.getTransform();

        g2.translate(pos.x, pos.y);

        // I don't know if this what you mean by "facing its moving direction", but the rabbits look weird.
        g2.rotate(vel.heading());
        g2.draw(fov);
        if (vel.x < 0) g2.rotate(Math.PI);
        if (vel.x > 0) g2.scale(-1, 1);

        // feet
        g2.setColor(color);
        g2.fill(bottomFoot);
        g2.fill(topFoot);
        g2.setColor(Color.BLACK);
        g2.draw(bottomFoot);
        g2.draw(topFoot);
        
        // hands
        g2.setColor(color);
        g2.fill(bottomHand);
        g2.fill(topHand);
        g2.setColor(Color.BLACK);
        g2.draw(bottomHand);
        g2.draw(topHand);

        // ears
        g2.setColor(color);
        g2.fill(leftEar);
        g2.fill(rightEar);
        g2.setColor(Color.BLACK);
        g2.draw(leftEar);
        g2.draw(rightEar);

        // body
        g2.setColor(color);
        g2.fill(body);
        g2.setColor(Color.BLACK);
        g2.draw(body);

        // head
        g2.setColor(color);
        g2.fill(head);
        g2.setColor(Color.BLACK);
        g2.draw(head);

        // tail
        g2.setColor(color);
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
    }

    public void move(ArrayList<Carrot> carrots, Dimension s, ArrayList<Animal> animals) {
        if (moving) {
            checkCollision(s, animals);

            seek(carrots);
            vel.normalize();
            vel.mult(speed);

            pos.add(vel);
        }
    }
    
    private void checkCollision(Dimension s, ArrayList<Animal> animals) {
        int margin = RabbitApp.margin;

        Rectangle2D.Double top = new Rectangle2D.Double(margin, 0, s.width-margin*2, margin);
        Rectangle2D.Double bottom = new Rectangle2D.Double(margin, s.height-margin, s.width-margin*2, margin);
        Rectangle2D.Double left = new Rectangle2D.Double(0, margin, margin, s.height-margin*2);
        Rectangle2D.Double right = new Rectangle2D.Double(s.width-margin, margin, margin, s.height-margin*2);

        PVector accel = new PVector(0, 0);

        if (getFOV().intersects(top)) accel.add(0, 1);
        if (getFOV().intersects(bottom)) accel.add(0, -1);
        if (getFOV().intersects(left)) accel.add(1, 0);
        if (getFOV().intersects(right)) accel.add(-1, 0);

        for (Animal r : animals) {
            if (this.scale < r.scale && getFOV().intersects(r.getBoundary().getBounds2D())) {
                if (r.pos.x < pos.x) accel.add(1, 0);
                if (r.pos.x > pos.x) accel.add(-1, 0);
                if (r.pos.y < pos.y) accel.add(0, 1);
                if (r.pos.y > pos.y) accel.add(0, -1);
                escaping = 30;
            }
        }
        if (escaping != 0) escaping --;

        vel.add(accel.mult(0.5f));
        vel.normalize();
        vel.mult(speed);
    }

    /*
     * 1. check if the arraylist is empty
     * 2. get the first carrot
     * 3. minus this.pos from carrot.pos to get this.vel
     * 4. the rabbit will stop and eat the food on the way
     */
    private void seek(ArrayList<Carrot> carrots) {
        if (carrots.size() != 0) {
            Carrot[] targets = new Carrot[2];
            
            // load 1 or 2 carrot(s) into the array
            targets[0] = carrots.get(0);
            if (carrots.size() > 1) {
                if (getAFC(carrots.get(1)) > getAFC(targets[0])) {
                    targets[1] = targets[0];
                    targets[0] = carrots.get(1);
                }
                else {
                    targets[1] = carrots.get(1);
                }
            }

            for (Carrot c : carrots) {
                float newAFC = getAFC(c);
                if (newAFC > getAFC(targets[0])) {
                    if (carrots.size() > 1) targets[1] = targets[0];
                    targets[0] = c;
                }
                else if (carrots.size() > 1) {
                    if (newAFC > getAFC(targets[1])) targets[1] = c;
                }
            }

            vel = targets[escaping > 0 ? 1 : 0].pos.copy();
            vel.sub(this.pos);
        }
    }

    public void eat(ArrayList<Carrot> carrots) {
        start();
        
        for (int i = 0; i < carrots.size(); i ++) {
            if (carrots.get(i) != null) {
                if (this.getBoundary().intersects(carrots.get(i).getBoundary().getBounds2D())) {
                    stop();
                    Carrot.eat(i, this);
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

    private Shape getFOV() {
        AffineTransform at = new AffineTransform();
        at.translate(pos.x, pos.y);
        at.rotate(vel.heading());
        return at.createTransformedShape(fov);
    }

    private float getAFC(Carrot c) {
        return c.getSize()*10 / PVector.dist(this.pos, c.getPos());
    }

}
