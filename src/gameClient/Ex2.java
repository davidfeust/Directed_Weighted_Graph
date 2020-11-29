package gameClient;


import trys.GUI_102;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Ex2{

    public static void main(String[] args) {
        JFrame jf = new JFrame();

        jf.setSize(400,400);
        JButton b = new JButton();
        jf.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.out.println("done");
                System.exit(0);
            }
        });
        b.setSize(10,20);
        Container contentPane = jf.getContentPane();
        contentPane.add(b);
        jf.show();

    }

}
