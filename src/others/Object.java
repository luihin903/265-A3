package others;
import java.awt.Dimension;
import java.awt.Graphics2D;

import processing.core.PVector;

public abstract class Object {
    
    protected PVector pos, dim;

    public Object(PVector pos, PVector dim) {
        this.pos = pos;
        this.dim = dim;
    }

    public Object() {}

    public Object(Dimension s) {
        pos = Util.random(s);
    }

    public Object(Dimension s, PVector default_dim) {
        dim = default_dim;
        pos = Util.random(s, dim);
    }

    protected abstract void draw(Graphics2D g2);

    public PVector getPos() {return pos.copy();};

}
