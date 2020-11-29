package gameClient;


import trys.GUI_102;

import javax.swing.*;
import java.awt.*;

public class Ex2{

    public static void main(String[] args) {
        JFrame jf = new JFrame();

        jf.setSize(400,400);
        JButton b = new JButton();
        b.setSize(10,20);
        Container contentPane = jf.getContentPane();
        contentPane.add(b);
        jf.show();

    }

}
