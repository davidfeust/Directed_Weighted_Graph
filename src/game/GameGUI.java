package game;

import api.*;
import game.util.Point3D;
import game.util.Range;
import game.util.Range2D;
import game.util.Range2Range;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

public class GameGUI extends JFrame {//implements ActionListener

    private static Arena _ar;
    private static int _scenario_num;
    private static Range2Range _w2f;

    public GameGUI(int scenario_num, game_service game) {
        super("Pockemons Game " + scenario_num);
        _scenario_num = scenario_num;
        Ex2 ctrl = new Ex2();
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
    }

    public void set_ar(Arena _ar) {
        GameGUI._ar = _ar;
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
        drawPokemons(g);
        drawAgants(g);
        infoBox(g);
        drawTime(g);
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

    private void drawPokemons(Graphics g) {
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
                pokIcon(g, radius, fp);
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

    public void agentIcon(Graphics g, int r, geo_location fp, int id) {
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
//        g.setColor(Color.white);
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
        g.fillRoundRect(0, 0, w, 50 + h / 6, 10, 10);
        g.setColor(Color.white);
        g.setFont(new Font(null, Font.PLAIN, 13));
        g.drawString("Time to end: " + currTime, tx + 10, ty + 80);
        g.drawString("Game level: " + _scenario_num, tx + 10, ty + 100);
        g.drawString("Score: " + _ar.getGrade(), tx + 10, ty + 120);
    }

    public void nodeIcon(Graphics g, int radius, geo_location fp) {
//        g.setColor(Color.BLUE);
        g.setColor(new Color(0x000099));
        g.fillOval((int) fp.x() - radius, (int) fp.y() - radius, 2 * radius, 2 * radius);
    }

    public void pokIcon(Graphics g, int radius, geo_location fp) {
        g.fillOval((int) fp.x() - radius, (int) fp.y() - radius, 2 * radius, 2 * radius);

    }

}