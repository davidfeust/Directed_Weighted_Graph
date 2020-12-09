package game;

import api.*;
import game.util.Point3D;
import game.util.Range;
import game.util.Range2D;
import game.util.Range2Range;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameGUI extends JFrame {//implements ActionListener

    private Arena _ar;
    private int _scenario_num;
    private Range2Range _w2f;
    private final Controller _ctrl;

    public GameGUI(int scenario_num, Controller ctrl) {
//        super("Pockemons Game " + scenario_num);

        _ctrl = ctrl;
        _scenario_num = scenario_num;
//        Controller ctrl = new Controller();
        addWindowListener(ctrl);
        setSize(1000, 600);

        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Level");
        menuBar.add(menu);
        this.setMenuBar(menuBar);
        ArrayList<MenuItem> menuItems = new ArrayList<>();
        String s = "scenario number - ";
        for (int i = 0; i < 24; i++) {
            menuItems.add(new MenuItem(s + i));
        }
        for (MenuItem i : menuItems) {
            i.addActionListener(ctrl);
            menu.add(i);
        }

        JButton getIdSnum = new JButton("Submit");
        getIdSnum.addActionListener(_ctrl);

        JTextField ID = new JTextField("Enter your ID");
        ID.setPreferredSize(new Dimension(250, 40));

        this.add(getIdSnum);
        this.add(ID);







    }

    public void set_ar(Arena ar) {
        _ar = ar;
        updateFrame();
    }

    private void updateFrame() {
        Range rx = new Range(40, this.getWidth() - 40);
        Range ry = new Range(this.getHeight() - 40, 180);
        Range2D frame = new Range2D(rx, ry);
        directed_weighted_graph g = _ar.get_graph();
        _w2f = Arena.w2f(g, frame);
    }

    @Override
    public void paint(Graphics g) {

        int w = this.getWidth();
        int h = this.getHeight();
        Image buffer_image;
        Graphics buffer_graphics;
        buffer_image = createImage(w, h);
        buffer_graphics = buffer_image.getGraphics();
        paintComponents(buffer_graphics);
        g.drawImage(buffer_image, 0, 0, this);
    }

    @Override
    public void paintComponents(Graphics g) {
        drawGraph(g);
        drawPokemons(g, _ar, _w2f);
        drawAgants(g);
        infoBox(g);
        drawTime(g);

        insertBox(g);
        updateFrame();

    }

    private void drawGraph(Graphics g) {
        directed_weighted_graph graph = _ar.get_graph();
        for (node_data i : graph.getV()) {
            drawNode(i, g);
            for (edge_data e : graph.getE(i.getKey())) {
                drawEdge(e, g);
            }
        }
    }

    private void drawNode(node_data n, Graphics g) {
        int radius = 6;
        geo_location pos = n.getLocation();
        geo_location fp = _w2f.world2frame(pos);
        nodeIcon(g, radius, fp);
        g.setColor(Color.BLACK);
        g.drawString("" + n.getKey(), (int) fp.x(), (int) fp.y() - 2 * radius);
    }

    private void drawEdge(edge_data e, Graphics g) {
        // get location info
        directed_weighted_graph gg = _ar.get_graph();
        geo_location s = gg.getNode(e.getSrc()).getLocation();
        geo_location d = gg.getNode(e.getDest()).getLocation();
        geo_location s0 = _w2f.world2frame(s);
        geo_location d0 = _w2f.world2frame(d);

        // draw line edge
        g.setColor(new Color(0x000099));
//        g.drawLine((int) s0.x(), (int) s0.y(), (int) d0.x(), (int) d0.y());
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(2));
        g2.draw(new Line2D.Float((int) s0.x(), (int) s0.y(), (int) d0.x(), (int) d0.y()));

        // print weight
        g.setColor(Color.black);
        g.setFont(new Font("Courier", Font.PLAIN, 13));
        String t = String.format("%.2f", e.getWeight());
        int x = (int) ((s0.x() + d0.x()) / 2);
        int y = (int) ((s0.y() + d0.y()) / 2) - 3;
        if (e.getSrc() < e.getDest()) y += 15;
//        g.drawString(t, x, y);

    }

    protected void drawPokemons(Graphics g, Arena ar, Range2Range _w2f) {
        List<Pokemon> fs = new ArrayList<>(_ar.getPokemons());
        if (fs.isEmpty())
            return;
        for (Pokemon f : fs) {
            if (f == null) continue;
            Point3D c = f.get_pos();
            int radius = 10;
            g.setColor(Color.green);
            if (f.get_type() < 0) {
                g.setColor(Color.orange);
            }
            if (c != null) {
                geo_location fp = _w2f.world2frame(c);
                pokIcon(g, radius, fp, 0);
                g.setColor(Color.BLACK);
                g.setFont(new Font(null, Font.BOLD, 12));
                g.drawString("" + (int) f.get_value(), (int) fp.x(), (int) fp.y() + 2);
            }
        }
    }

    private void drawAgants(Graphics g) {
        List<Agent> rs = _ar.getAgents();
        for (Agent a : rs) {
            geo_location loc = a.getPos();
            int r = 8;
            geo_location fp = _w2f.world2frame(loc);
//            g.setColor(Color.red);
            agentIcon(g, r, fp, a.getId());
            String v = (int) a.getValue() + "";
            g.setColor(Color.BLACK);
            g.setFont(new Font(null, Font.BOLD, 12));
            g.drawString(v, (int) fp.x() + 10, (int) fp.y() + 10);
        }
    }

    protected void agentIcon(Graphics g, int r, geo_location fp, int id) {
        g.setColor(new Color(150, 60, 90));
        g.fillOval((int) fp.x() - r, (int) fp.y() - r, 2 * r, 2 * r);
    }

    private void drawTime(Graphics g) {
//        g.setColor(Color.red);
        g.setColor(new Color(0xCD1818));
        double ts = (double) _ar.get_timeStart();
        double curT = (double) _ar.getTime();
        double dt = ((ts - curT) / ts);
        double w = getWidth();
        g.fillRoundRect(10, 50, (int) (w * dt), 10, 10, 10);
    }

//    private void infoBox(Graphics g) {
//        g.setColor(Color.black);
//        double w = getWidth();
//        double h = getHeight();
//        int tx = (int) (w * 0.02);
//        int ty = (int) (h * 0.02);
//        int th = (int) (h * 0.16);
//        int tw = (int) (w * 0.13);
//        int currTime = (int) _ar.getTime() / 1000;
//        g.fillRoundRect(tx, ty + 60, tw, th, 10, 10);
//        g.setFont(new Font(null, Font.PLAIN, 13));
//        g.drawString("Time to end: " + currTime, tx + 10, ty + 80);
//        g.drawString("Game level: " + this._scenario_num, tx + 10, ty + 100);
//        g.drawString("Score: " + _ar.getGrade(), tx + 10, ty + 120);
//    }

    private void infoBox(Graphics g) {
        g.setColor(Color.black);
        int w = getWidth();
        int h = getHeight();
        int tx = (int) (w * 0.02);
        int ty = (int) (h * 0.02);
        int th = (int) (h * 0.255);
        int currTime = (int) _ar.getTime() / 1000;
//        g.fillRoundRect(0, 0, w, 50 + h / 6, 10, 10);
        g.setColor(Color.white);
        g.setFont(new Font(null, Font.PLAIN, 13));
        g.drawString("Time to end: " + currTime, tx + 10, ty + 80);
        g.drawString("Game level: " + _scenario_num, tx + 10, ty + 100);
        g.drawString("Score: " + _ar.getGrade(), tx + 10, ty + 120);
    }

    private void insertBox(Graphics g) {
        int h = getHeight();
        int w = getWidth();

        JTextField tf;
        JLabel l;
        JButton b;
//        g.fillOval(100,100,100,100);
        tf = new JTextField("Enter scenario number");
        tf.setCaretColor(new Color(152, 124, 80));
        tf.setBounds(50 + w, 50 + h, 200, 200);
        l = new JLabel("Enter ID");
        l.setBounds(50, 100, 250, 20);
        b = new JButton("Login");
        b.setBounds(50, 150, 95, 30);
        b.addActionListener(_ctrl);

        add(b);
        add(tf);
        add(l);

        l.setVisible(true);
        tf.setVisible(true);
        b.setVisible(true);


//        setLayout(null);
    }

    protected void nodeIcon(Graphics g, int radius, geo_location fp) {
//        g.setColor(Color.BLUE);
        g.setColor(new Color(0x000099));
        g.fillOval((int) fp.x() - radius, (int) fp.y() - radius, 2 * radius, 2 * radius);
    }

    protected void pokIcon(Graphics g, int radius, geo_location fp, int flag) {
        g.fillOval((int) fp.x() - radius, (int) fp.y() - radius, 2 * radius, 2 * radius);

    }

    public void set_scenario_num(int _scenario_num) {
        this._scenario_num = _scenario_num;
    }
}