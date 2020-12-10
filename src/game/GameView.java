package game;

import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;
import game.Agent;
import game.Arena;
import game.Controller;
import game.Pokemon;
import game.util.Point3D;
import game.util.Range;
import game.util.Range2D;
import game.util.Range2Range;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

public class GameView extends JPanel {

    JFrame _frame;
    private Arena _ar;
    private int _scenario_num;
    private Range2Range _w2f;

    public GameView(JFrame frame, int level) {
        _frame = frame;
        _scenario_num = level;
//        setSize(1200, 600);
    }

    public void set_ar(Arena ar) {
        _ar = ar;
        updateFrame();
    }

    private void updateFrame() {
        Range rx = new Range(40, this.getWidth() - 40);
        Range ry = new Range(this.getHeight() - 40, 40);
        Range2D frame = new Range2D(rx, ry);
        directed_weighted_graph g = _ar.get_graph();
        _w2f = Arena.w2f(g, frame);
    }


    @Override
    public void paint(Graphics g) {
        super.paint(g);
        int w = getWidth();
        int h = getHeight();
//        setPreferredSize(new Dimension(w, (int) (h * 0.8)));

        Image buffer_image = createImage(w, h);
        Graphics buffer_graphics = buffer_image.getGraphics();
        if (_ar != null) {
            paintComponents(buffer_graphics);
            g.drawImage(buffer_image, 0, 0, null);
        }
    }

    @Override
    public void paintComponents(Graphics g) {
//        g.fillRect(0,0,getWidth(), getHeight());
        drawGraph(g);
        drawPokemons(g, _ar, _w2f);
        drawAgants(g);
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

    protected void nodeIcon(Graphics g, int radius, geo_location fp) {
//        g.setColor(Color.BLUE);
        g.setColor(new Color(0x000099));
        g.fillOval((int) fp.x() - radius, (int) fp.y() - radius, 2 * radius, 2 * radius);
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

    protected void pokIcon(Graphics g, int radius, geo_location fp, int flag) {
        g.fillOval((int) fp.x() - radius, (int) fp.y() - radius, 2 * radius, 2 * radius);

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
        g.setColor(new Color(0xCD1818));
        double ts = (double) _ar.get_timeStart();
        double curT = (double) _ar.getTime();
        double dt = ((ts - curT) / ts);
        double w = getWidth();
        g.fillRoundRect(0, 0, (int) (w * dt), 10, 10, 10);
    }

}
