package trys;

import game.Controller;
import game.util.Point3D;
import game.util.Range2Range;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.StringJoiner;

public class Try1 extends JFrame {

    Range2Range _w2f;
//    static String level = "";
//    static String id = "";
    static JTextField id_field;
    static JTextField s_n;

    public static String getLevel() {
        return s_n.getText();
    }

    public static String getId() {
        return id_field.getText();
    }
// JButton
//    static JButton b, b1, b2;

    // label to display text
//    static JLabel l;



    public Try1() {
//
//        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
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
            menu.add(i);
        }


        setSize(1200, 600);
        Point3D f = Point3D.ORIGIN;

        id_field = new JTextField();//"Enter your ID");
        id_field.setBounds((int) (getWidth()-(getWidth() / 6)), (int) (getHeight() * 0.01), 150, 20);
        s_n = new JTextField();//"Enter scenario number");
        s_n.setBounds((int) (getWidth() - getWidth() / 6), (int) (getHeight() * 0.05), 150, 20);
        JButton b = new JButton("Submit");
        b.setBounds((getWidth() - getWidth() / 7) , (int) (getHeight() * 0.10), 95, 30);
        b.addActionListener(new Controller());

        add(b);
        add(id_field);
        add(s_n);
//        setSize(1000, 600);
        repaint();
        setLayout(null);
        setVisible(true);

    }


//    @Override
//    public void paint(Graphics g) {
//        super.paint(g);
//        g.fillRoundRect(100,100,010,100,20,20);
//    }

    // main class

    public static void main(String[] args) {
        new Try1();
    }

}