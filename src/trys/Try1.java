package trys;

import java.awt.event.*;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.BevelBorder;

class Try1 extends JFrame {


    // JButton
//    static JButton b, b1, b2;

    // label to display text
//    static JLabel l;

    Try1() {

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
            menu.add(i);
        }
        JLabel l1 = new JLabel();
        l1.setOpaque(true);
        l1.setBackground(Color.red);
        l1.setBounds(50,100,200,200);

        JLabel l2 = new JLabel();
        l2.setOpaque(true);
        l2.setBackground(Color.green);
        l2.setBounds(50,50,200,200);

        JLayeredPane jlp = new JLayeredPane();
        jlp.setBounds(150, 0, 300, 100);

        jlp.add(l1);
        jlp.add(l2);

//        jlp.setBackground(Color.black);
        add(jlp);
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