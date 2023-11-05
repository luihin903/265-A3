import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import processing.core.PVector;

public class Lion extends Animal {
    
    public static final PVector default_dim = new PVector(200, 100);

    private Ellipse2D.Double around;
    private Ellipse2D.Double head;
    private Ellipse2D.Double body;
    private Ellipse2D.Double leftHand;
    private Ellipse2D.Double rightHand;
    private Ellipse2D.Double leftFoot;
    private Ellipse2D.Double rightFoot;
    private Ellipse2D.Double leftEye;
    private Ellipse2D.Double rightEye;
    private Arc2D.Double tail;
    private Polygon nose;
    private Line2D.Double[] face ;


    public Lion() {
        super();
    }
    public Lion(PVector pos, PVector dim) {
        super(pos, dim);
        vel = new PVector(Util.random(-100, 100), Util.random(-100, 100));
    }

    protected void setShape() {

        around = new Ellipse2D.Double(-dim.x/2, -dim.y/2, dim.x/3, dim.y/1.5);
        head = new Ellipse2D.Double(-dim.x/2 + dim.x/24, -dim.y/2 + dim.y/12, dim.x/4, dim.y/2);
        body = new Ellipse2D.Double(-dim.x/4, -dim.y/4, dim.x/2, dim.y/2);

        leftHand = new Ellipse2D.Double(-dim.x/16 * 3, 0, dim.x/16, dim.y/2);
        rightHand = new Ellipse2D.Double(-dim.x/16 * 2, -dim.y/8, dim.x/16, dim.y/2);
        leftFoot = new Ellipse2D.Double(dim.x/16 * 2, 0, dim.x/16, dim.y/2);
        rightFoot = new Ellipse2D.Double(dim.x/16 * 3, -dim.y/8, dim.x/16, dim.y/2);

        // center of head = (-dim.x/3, -dim.y/6)
        leftEye = new Ellipse2D.Double(-dim.x/3 - dim.x/64 - dim.x/24, -dim.y/6 - dim.y/32 - dim.y/16, dim.x/32, dim.y/16);
        rightEye = new Ellipse2D.Double(-dim.x/3 - dim.x/64 + dim.x/24, -dim.y/6 - dim.y/32 - dim.y/16, dim.x/32, dim.y/16);

        int[] xPoints = {(int) (-dim.x/3 - dim.x/32), (int) (-dim.x/3 + dim.x/32), (int) (-dim.x/3)};
        int[] yPoints = {(int) (-dim.y/6), (int) (-dim.y/6), (int) (-dim.y/6 + dim.y/16)};
        nose = new Polygon(xPoints, yPoints, 3);

        face = new Line2D.Double[3];
        face[0] = new Line2D.Double((double) (-dim.x/3), (double) (-dim.y/6 + dim.y/16), (double) (-dim.x/3), (double) (-dim.y/16));
        face[1] = new Line2D.Double(-dim.x/3, -dim.y/16, -dim.x/3 - dim.x/16, -dim.y/16 + dim.y/32);
        face[2] = new Line2D.Double(-dim.x/3, -dim.y/16, -dim.x/3 + dim.x/16, -dim.y/16 + dim.y/32);

        area = new Area(around);
        area.add(new Area(body));
        area.add(new Area(leftHand));
        area.add(new Area(rightHand));
        area.add(new Area(leftFoot));
        area.add(new Area(rightFoot));
    }

    @Override
    public void draw(Graphics2D g) {
        super.draw(g);
        AffineTransform at = g.getTransform();
        g.translate(pos.x, pos.y);
        g.rotate(vel.heading());
        if (vel.x < 0) g.rotate(Math.PI);
        if (vel.x > 0) g.scale(-1, 1);

        g.setColor(Color.RED);
        g.drawRect((int) (-dim.x/2), (int) (-dim.y/2), (int) dim.x, (int) dim.y);

        g.setColor(Color.BLACK);
        g.draw(body);
        g.draw(around);
        g.draw(leftHand);
        g.draw(rightHand);
        g.draw(leftFoot);
        g.draw(rightFoot);

        g.setColor(Color.ORANGE);
        g.fill(body);
        g.fill(leftHand);
        g.fill(rightHand);
        g.fill(leftFoot);
        g.fill(rightFoot);

        g.setColor(new Color(135, 67, 18));
        g.fill(around);
        g.setColor(Color.ORANGE);
        g.fill(head);

        g.setColor(Color.BLACK);
        g.fill(leftEye);
        g.fill(rightEye);
        g.fill(nose);
        for (int i = 0; i < face.length; i ++) g.draw(face[i]);

        g.setTransform(at);
    }

    public void move(ArrayList<Carrot> carrots, Dimension s, ArrayList<Animal> animals) {

    }

    public void eat(ArrayList<Carrot> carrots) {

    }

}
