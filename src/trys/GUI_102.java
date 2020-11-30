package trys;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

//package javaTutorial.net;
import java.awt.FlowLayout;
import javax.swing.JTextField;

import javax.swing.*;

import static com.sun.java.accessibility.util.AWTEventMonitor.addActionListener;

/**
 * Taken from: https://www.tutorialspoint.com/javaexamples/gui_polygon.htm
 */

public class GUI_102 extends JPanel {
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Polygon p = new Polygon();
        for (int i = 0; i < 5; i++) p.addPoint((int) (
                100 + 50 * Math.cos(i * 2 * Math.PI / 5)),(int) (
                100 + 50 * Math.sin(i * 2 * Math.PI / 5)));

        g.drawPolygon(p);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.getContentPane().setBackground(Color.BLACK);
        frame.setTitle("DrawPoly");
        frame.setSize(350, 250);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.out.println("done");
                System.exit(0);
            }
        });
        JFrame f = new JFrame("Text Field Examples");
        f.getContentPane().setLayout(new FlowLayout());
        JTextField textfield1 = new JTextField("Text field 1", 10);
        JTextField textfield2 = new JTextField("Text field 2", 10);
        JTextField textfield3 = new JTextField("Text field 3", 10);
        f.getContentPane().add(textfield1);
        f.getContentPane().add(textfield2);
        f.getContentPane().add(textfield3);
//        textfield1.addActionListener(new actionLisiner a);
//        addActionListener(a);
        f.pack();
        f.setVisible(true);
    }
//        Container contentPane = frame.getContentPane();
//        contentPane.add(new GUI_102());
//        frame.show();
    }
//}