package gameClient;

import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Iterator;
import java.util.List;

public class GameGUI extends JFrame {

    private Arena _ar;
    private int _scenario_num;
    private gameClient.util.Range2Range _w2f;

    public GameGUI(int scenario_num) {
        super("Pockemons Game " + scenario_num);
        _scenario_num = scenario_num;
        setSize(1000, 700);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.out.println("done");
                System.exit(0);
            }
        });
    }

    private void updateFrame() {
        Range rx = new Range(40, this.getWidth() - 40);
        Range ry = new Range(this.getHeight() - 40, 60);
        Range2D frame = new Range2D(rx, ry);
        directed_weighted_graph g = _ar.getGraph();
        _w2f = Arena.w2f(g, frame);
    }

    @Override
    public void paint(Graphics g) {
        updateFrame();
        int w = this.getWidth();
        int h = this.getHeight();
        g.clearRect(0, 0, w, h);
        updateFrame();
        drawPokemons(g);
        drawGraph(g);
        drawAgants(g);
        drawInfo(g);
    }

    private void drawGraph(Graphics g) {
        directed_weighted_graph graph = _ar.getGraph();
        for (node_data i : graph.getV()) {
            drawNode(i, g);
            for (edge_data e : graph.getE(i.getKey())) {
                drawEdge(e, g);
            }
        }
    }

    public void set_ar(Arena _ar) {
        this._ar = _ar;
        updateFrame();
    }

    private void drawNode(node_data n, Graphics g) {
        int radius = 5;
        geo_location pos = n.getLocation();
        geo_location fp = this._w2f.world2frame(pos);
        g.setColor(Color.green);
        g.fillOval((int) fp.x() - radius, (int) fp.y() - radius, 2 * radius, 2 * radius);
        g.setColor(Color.BLACK);
        g.drawString("" + n.getKey(), (int) fp.x(), (int) fp.y() - 4 * radius);

/*
        ** for represent node with image:
        try {
            BufferedImage image_node = ImageIO.read(new File("src/ex2/img/node.jpg"));
            g.drawImage(image_node, (int) fp.x() - r, (int) fp.y() - r, 5 * r, 5 * r, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
*/
    }

    private void drawEdge(edge_data e, Graphics g) {
        directed_weighted_graph gg = _ar.getGraph();
        geo_location s = gg.getNode(e.getSrc()).getLocation();
        geo_location d = gg.getNode(e.getDest()).getLocation();
        geo_location s0 = this._w2f.world2frame(s);
        geo_location d0 = this._w2f.world2frame(d);
        g.setColor(Color.blue);
        g.drawLine((int) s0.x(), (int) s0.y(), (int) d0.x(), (int) d0.y());
//        	g.drawString(""+n.getKey(), fp.ix(), fp.iy()-4*r);
    }

    private void drawPokemons(Graphics g) {
        List<CL_Pokemon> fs = _ar.getPokemons();
        if (fs == null)
            return;
        for (CL_Pokemon f : fs) {
            Point3D c = f.getLocation();
            int radius = 10;
            g.setColor(Color.green);
            if (f.getType() < 0) {
                g.setColor(Color.orange);
            }
            if (c != null) {
                geo_location fp = this._w2f.world2frame(c);
                g.fillOval((int) fp.x() - radius, (int) fp.y() - radius, 2 * radius, 2 * radius);
                //	g.drawString(""+n.getKey(), fp.ix(), fp.iy()-4*r);
            }
        }
    }

    private void drawAgants(Graphics g) {
        List<CL_Agent> rs = _ar.getAgents();
        g.setColor(Color.red);
        int i = 0;
        while (rs != null && i < rs.size()) {
            geo_location c = rs.get(i).getLocation();
            int r = 8;
            i++;
            if (c != null) {
                geo_location fp = this._w2f.world2frame(c);
                g.fillOval((int) fp.x() - r, (int) fp.y() - r, 2 * r, 2 * r);
            }
        }
    }

    private void drawInfo(Graphics g) {
        List<String> str = _ar.get_info();
        String dt = "none";
        for(int i=0;i<str.size();i++) {
            g.drawString(str.get(i)+" dt: "+dt,100,60+i*20);
        }

    }
}