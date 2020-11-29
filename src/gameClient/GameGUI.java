package gameClient;

import api.*;
import gameClient.util.Range;
import gameClient.util.Range2D;
import gameClient.util.Range2Range;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GameGUI extends JFrame {

    private Arena _ar;
    private int _scenario_num;
    private gameClient.util.Range2Range _w2f;

    public GameGUI(int scenario_num) {
        super("Pockemons Game" + scenario_num);
        _scenario_num = scenario_num;
        setSize(800,400);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.out.println("done");
                System.exit(0);
            }
        });
    }

    private void updateFrame() {
        Range rx = new Range(20,this.getWidth()-20);
        Range ry = new Range(this.getHeight()-10,150);
        Range2D frame = new Range2D(rx,ry);
        directed_weighted_graph g = _ar.getGraph();
        _w2f = Arena.w2f(g,frame);
    }

    public void paint(Graphics g) {
        updateFrame();
        int w = this.getWidth();
        int h = this.getHeight();
        g.clearRect(0, 0, w, h);

        //	updateFrame();
//        drawPokemons(g);
        drawGraph(g);
//        drawAgants(g);
//        drawInfo(g);
    }

    private void drawGraph(Graphics g) {
        directed_weighted_graph graph = _ar.getGraph();
        for (node_data i : graph.getV()) {
            drawNode(i, 3, g);
        }
    }

    public void set_ar(Arena _ar) {
        this._ar = _ar;
    }

    private void drawNode(node_data n, int r, Graphics g) {
        geo_location pos = n.getLocation();
        geo_location fp = this._w2f.world2frame(pos);
        g.setColor(Color.green);
        g.fillOval((int)fp.x()-r, (int)fp.y()-r, 2*r, 2*r);
        g.setColor(Color.BLACK);
        g.drawString(""+n.getKey(), (int)fp.x(), (int)fp.y()-4*r);
    }


}
