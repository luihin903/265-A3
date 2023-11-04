import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

import processing.core.PVector;

public class Lion extends Animal {
    
    public static final PVector default_dim = new PVector(200, 100);

    Ellipse2D.Double head;


    public Lion() {
        super();
    }
    public Lion(PVector pos, PVector dim) {
        super(pos, dim);
        vel = new PVector(Util.random(-100, 100), Util.random(-100, 100));
    }

    protected void setShape() {

        head = new Ellipse2D.Double(0, 0, 50, 50);

        area = new Area(head);
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.ORANGE);
        g.fill(head);
    }

    public void move(ArrayList<Carrot> carrots, Dimension s, ArrayList<Animal> animals) {

    }

    public void eat(ArrayList<Carrot> carrots) {

    }

}
