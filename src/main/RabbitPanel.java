package main;



import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.Timer;

import others.*;
import processing.core.PVector;
import simulation.*;
import simulation.background.*;

public class RabbitPanel extends JPanel implements ActionListener {
    
    private Timer t;
    public static Dimension size;
    private ArrayList<Animal> animals = new ArrayList<Animal>();
    private boolean[] ready = new boolean[100];
    private int respawn = Setting.FPS * 5;

    public RabbitPanel(Dimension initialSize) {
        super();

        size = initialSize;
        ready[KeyEvent.VK_SPACE] = true;
        ready[KeyEvent.VK_D] = true;

        for (int i = 0; i < Setting.rabbits; i ++) {
            float scale = Util.random(0.5f, 1.5f);
            PVector dim = Rabbit.default_dim.copy().mult(scale);
            animals.add(new Rabbit(Util.random(size, dim), dim, 2, scale));
        }
        for (int i = 0; i < Setting.lions; i ++) {
            float scale = Util.random(0.5f, 1.5f);
            PVector dim = Lion.default_dim.copy().mult(scale);
            animals.add(new Lion(Util.random(size, dim), dim, 2, scale));
        }

        animals.add(new Hunter());
        
        Tree.init(6, initialSize);
        Flower.init(6, initialSize);
        Carrot.init(24); // greater or equal to 3 times the creatures are too many

        t = new Timer((int) (1000/Setting.FPS), this);
        t.start();

        addKeyListener(new MyKeyAdapter());
        addMouseListener(new MyMouseAdapter());
        addMouseMotionListener(new MyMouseMotionAdapter());
        setFocusable(true);
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
        Blood.drawAll(g2);

        drawInfo(g2);

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

        for (int i = 0; i < animals.size(); i ++) {
            Animal a = animals.get(i);
            if (a instanceof Rabbit) {
                Rabbit r = (Rabbit) a;
                r.update(Carrot.get(), getSize(), animals);
                r.eat(Carrot.get());
            }
            else if (a instanceof Lion) {
                Lion l = (Lion) a;
                l.update(getSize(), animals);
                l.eat(animals);
            }
            else if (a instanceof Hunter) {
                Hunter h = (Hunter) a;
                h.update(getSize(), animals);
            }

            if (a.getState() == 0) {
                if (a instanceof Rabbit) Rabbit.amount --;
                else Lion.amount --;
                animals.remove(i);
            }
        }

        Carrot.grow();
        Blood.updateAll();

        if (respawn == 0) {
            for (int i = Rabbit.amount; i < Setting.rabbits; i ++) {
                float scale = Util.random(0.5f, 1.5f);
                PVector dim = Rabbit.default_dim.copy().mult(scale);
                animals.add(new Rabbit(Util.random(size, dim), dim, 2, scale));
            }
            for (int i = Lion.amount; i < Setting.lions; i ++) {
                float scale = Util.random(0.5f, 1.5f);
                PVector dim = Lion.default_dim.copy().mult(scale);
                animals.add(new Lion(Util.random(size, dim), dim, 2, scale));
            }

            respawn = Setting.FPS * 5;
        }
        else if (Lion.amount <= Setting.lions/2) {
            respawn --;
        }

        repaint();
    }

    private void drawInfo(Graphics2D g) {
        AffineTransform at = g.getTransform();
        g.translate(Setting.margin, getSize().height - Setting.margin);

        g.setColor(Color.WHITE);
        g.fillRect(0, -20, getSize().width - Setting.margin*2, 20);
        g.setFont(Setting.font);
        g.setColor(Color.BLACK);

        if (Rabbit.amount <= Setting.rabbits/2 && Lion.amount > Setting.lions/2) {
            g.drawString("Hunter has appeared", 0, 0);
        }
        else {
            g.drawString("Hunter has disappeared", 0, 0);
        }

        g.setTransform(at);
    }

    private class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                if (ready[KeyEvent.VK_SPACE] && Rabbit.amount <= Setting.rabbits /2 && Lion.amount > Setting.lions/2) {
                    for (Animal a : animals) {
                        if (a instanceof Hunter) {
                            Hunter h = (Hunter) a;
                            h.fire();
                            ready[KeyEvent.VK_SPACE] = false;
                        }
                    }
                }
            }
            
            if (e.getKeyCode() == KeyEvent.VK_D) {
                if (ready[KeyEvent.VK_D]) {
                    Setting.drawInfo = !Setting.drawInfo;
                    ready[KeyEvent.VK_D] = false;
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                ready[KeyEvent.VK_SPACE] = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_D) {
                ready[KeyEvent.VK_D] = true;
            }
        }
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
