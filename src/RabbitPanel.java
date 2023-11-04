import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.Timer;

import processing.core.PVector;

public class RabbitPanel extends JPanel implements ActionListener {
    
    private Timer t;
    public static Dimension size;
    private ArrayList<Animal> animals = new ArrayList<Animal>();

    public RabbitPanel(Dimension initialSize) {
        super();

        size = initialSize;

        for (int i = 0; i < 3; i ++) {
            float scale = Util.random(0.5f, 1.5f);
            PVector dim = Rabbit.default_dim.copy().mult(scale);
            animals.add(new Rabbit(Util.random(size, dim), dim, 2, Util.random(), scale));
        }
        for (int i = 0; i < 3; i ++) {
            float scale = Util.random(0.5f, 1.5f);
            PVector dim = Rabbit.default_dim.copy().mult(scale);
            animals.add(new Lion(Util.random(size, dim), dim));
        }
        
        Tree.init(6, initialSize);
        Flower.init(6, initialSize);
        Carrot.init(24); // greater or equal to 3 times the creatures are too many

        t = new Timer((int) (1000/RabbitApp.FPS), this);
        t.start();

        addMouseListener(new MyMouseAdapter());
        addMouseMotionListener(new MyMouseMotionAdapter());
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        size = getSize();
        setBackground(Color.GRAY);

        Graphics2D g2 = (Graphics2D) g;

        Forest.draw(g2, size);
        Tree.drawAll(g2);
        Carrot.drawAll(g2);
        Flower.drawAll(g2);
        for (Animal a : animals) a.draw(g2);

        // g2.drawOval(0, 0, 100, 100);
        // g2.drawOval(size.width-20-100, size.height-20-100, 100, 100);
        // for (int i = 0; i < 20; i ++) {
        //     g2.drawLine(i*100, 0, i*100, size.height);
        //     g2.drawLine(0, i*100, size.width, i*100);
        // }
        // System.out.println(size.width);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (Animal a : animals) {
            a.move(Carrot.get(), getSize(), animals);
            a.eat(Carrot.get());
        }
        Carrot.grow();

        repaint();
    }



    private class MyMouseAdapter extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            if (e.getClickCount() % 2 == 0) {
                Carrot.start(e, getSize());
            }
            // 3) remove when Ctrl-click
            if (e.isControlDown()) {
                for (Carrot c : Carrot.get()) {
                    if (c.hit(e)) Carrot.get().remove(c);
                }
            }
        }

        public void mouseReleased(MouseEvent e) {
            Carrot.stop();
        }
    }

    private class MyMouseMotionAdapter extends MouseAdapter{
        public void mouseDragged(MouseEvent e) {
            Carrot.grow(e);
        }
    }
}
