package game;

import api.*;
import game.util.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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

        MenuItem item1 = new MenuItem("scenario number - 1");
        item1.addActionListener(this);

        MenuItem item2 = new MenuItem("scenario number - 2");
        item2.addActionListener(this);

        MenuItem item3 = new MenuItem("scenario number - 3");
        item3.addActionListener(this);

        MenuItem item4 = new MenuItem("scenario number - 4");
        item4.addActionListener(this);

        MenuItem item5 = new MenuItem("scenario number - 5");
        item5.addActionListener(this);

        MenuItem item6 = new MenuItem("scenario number - 6");
        item6.addActionListener(this);

        MenuItem item7 = new MenuItem("scenario number - 7");
        item7.addActionListener(this);

        MenuItem item8 = new MenuItem("scenario number - 8");
        item8.addActionListener(this);

        MenuItem item9 = new MenuItem("scenario number - 9");
        item9.addActionListener(this);

        MenuItem item10 = new MenuItem("scenario number - 10");
        item10.addActionListener(this);

        MenuItem item11 = new MenuItem("scenario number - 11");
        item11.addActionListener(this);

        MenuItem item12 = new MenuItem("scenario number - 12");
        item12.addActionListener(this);

        MenuItem item13 = new MenuItem("scenario number - 13");
        item13.addActionListener(this);

        MenuItem item14 = new MenuItem("scenario number - 14");
        item14.addActionListener(this);

        MenuItem item15 = new MenuItem("scenario number - 15");
        item15.addActionListener(this);

        MenuItem item16 = new MenuItem("scenario number - 16");
        item16.addActionListener(this);

        MenuItem item17 = new MenuItem("scenario number - 17");
        item17.addActionListener(this);

        MenuItem item18 = new MenuItem("scenario number - 18");
        item18.addActionListener(this);

        MenuItem item19 = new MenuItem("scenario number - 19");
        item19.addActionListener(this);

        MenuItem item20 = new MenuItem("scenario number - 20");
        item20.addActionListener(this);

        MenuItem item21 = new MenuItem("scenario number - 21");
        item21.addActionListener(this);

        MenuItem item22 = new MenuItem("scenario number - 22");
        item22.addActionListener(this);

        MenuItem item23 = new MenuItem("scenario number - 23");
        item23.addActionListener(this);


        menu.add(item1);
        menu.add(item2);
        menu.add(item3);
        menu.add(item4);
        menu.add(item5);
        menu.add(item6);
        menu.add(item7);
        menu.add(item8);
        menu.add(item9);
        menu.add(item10);
        menu.add(item11);
        menu.add(item12);
        menu.add(item13);
        menu.add(item14);
        menu.add(item15);
        menu.add(item16);
        menu.add(item17);
        menu.add(item18);
        menu.add(item19);
        menu.add(item22);
        menu.add(item21);
        menu.add(item22);
        menu.add(item23);


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
        Range ry = new Range(this.getHeight() - 40, 80);
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
//        drawTime(g);
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
//        long t = _ar.getTime();
        g.drawString("" + _ar.getTime(), 100, 100);
        g.fillRoundRect(0, 50, (int) (_ar.getTime()), 10, 10, 10);
        g.fillRoundRect(0, 50, (int) (_ar.getTime()), 10, 10, 10);
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