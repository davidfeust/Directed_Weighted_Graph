package game;

import api.*;
import game.util.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class GameGUI extends JFrame implements ActionListener {

    private Arena _ar;
    private int _scenario_num;
    private Range2Range _w2f;

    public GameGUI(int scenario_num) {
        super("Pockemons Game " + scenario_num);
        _scenario_num = scenario_num;
        setSize(700, 400);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.out.println("done");
                System.exit(0);
            }
        });

        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Menu");
        menuBar.add(menu);
        this.setMenuBar(menuBar);
        ArrayList<MenuItem> menuItems = new ArrayList<>();
        String s = "scenario number - ";
        for(int i =0; i<24; i++){menuItems.add(new MenuItem(s+i));}
        for (MenuItem i :menuItems) { i.addActionListener(this);menu.add(i); }

//        JLabel text = new JLabel("Agent 0");
//        text.setBounds(50,100, 250,20);
//        Container contentPane = this.getContentPane();
//        contentPane.add(text);
//        this.add(text);
    }


    public void set_ar(Arena _ar) {
        this._ar = _ar;
        updateFrame();
    }

    private void updateFrame() {
        Range rx = new Range(40, this.getWidth() - 40);
        Range ry = new Range(this.getHeight() - 40, 100);
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
        drawPokemons(g);
        drawGraph(g);
        drawAgants(g);
        drawTime(g);
        infoBox(g);
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
        int radius = 5;
        geo_location pos = n.getLocation();
        geo_location fp = this._w2f.world2frame(pos);
        g.setColor(Color.BLUE);
        g.fillOval((int) fp.x() - radius, (int) fp.y() - radius, 2 * radius, 2 * radius);
        g.setColor(Color.BLACK);
        g.drawString("" + n.getKey(), (int) fp.x(), (int) fp.y() - 2 * radius);

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
        directed_weighted_graph gg = _ar.get_graph();
        geo_location s = gg.getNode(e.getSrc()).getLocation();
        geo_location d = gg.getNode(e.getDest()).getLocation();
        geo_location s0 = this._w2f.world2frame(s);
        geo_location d0 = this._w2f.world2frame(d);
        g.setColor(Color.blue);
        g.drawLine((int) s0.x(), (int) s0.y(), (int) d0.x(), (int) d0.y());
//        	g.drawString(""+n.getKey(), fp.ix(), fp.iy()-4*r);
    }

    private void drawPokemons(Graphics g) {
        List<Pokemon> fs = _ar.getPokemons();
        if (fs == null)
            return;
        for (Pokemon f : fs) {
            Point3D c = f.get_pos();
            int radius = 10;
            g.setColor(Color.green);
            if (f.get_type() < 0) {
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
        List<Agent> rs = _ar.getAgents();
        for (Agent a : rs) {
            geo_location loc = a.getPos();
            int r = 8;
            geo_location fp = this._w2f.world2frame(loc);
            g.setColor(Color.red);
            g.fillOval((int) fp.x() - r, (int) fp.y() - r, 2 * r, 2 * r);
            String v = (int) a.getValue() + "";
            g.setColor(Color.BLACK);
            g.drawString(v, (int) fp.x() + 10, (int) fp.y() + 10);
        }
    }

    private void drawTime(Graphics g) {
        g.setColor(Color.red);
        double ts  = (double)_ar.get_timeStart();
        double curT = (double)_ar.getTime();
        double dt =  ((ts-curT)/ts);
        double w = getWidth();
        g.fillRoundRect(10, 50, (int) (w*dt), 10, 10, 10);
    }

    private void infoBox(Graphics g) {
        g.setColor(Color.black);
        double w = getWidth();
        double h = getHeight();
        int tx = (int) (w*0.02);
        int ty = (int) (h*0.02);
        int th = (int) (h*0.13);
        int tw = (int) (w*0.13);
        int currTime = (int) _ar.getTime()/1000;
        g.fillRoundRect(tx, ty+60, tw, th, 10, 10);
        g.setColor(Color.white);
        g.drawString("time to end: "+currTime,tx+10,ty+80 );
        g.drawString("game level: "+this._scenario_num,tx+10,ty+100 );
    }


    /**
     * Invoked when an action occurs.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String str = e.getActionCommand();
        System.out.println("-----------------222222222222222222222------------------------" + str);
        System.out.println();
        System.out.println();
        String[] strA = str.split("\\D+");
//        Pattern p = Pattern.compile("numFound=\"([0-9]+)\"");
//        Matcher m = p.matcher(str);
//        String[] strA= {m.toString()};
//        this._scenario_num = Integer.parseInt(strA[0]);0
        System.out.println("-----------------------------------------" + strA[1]);
//        Ex2b.
//        Ex2b.main(strA);
    }
}