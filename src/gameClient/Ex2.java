package gameClient;


import trys.GUI_102;
import api.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Ex2{

    public static void main(String[] args) {
//        JFrame jf = new JFrame("Pockemons Game");
//        jf.setSize(400,400);
//        jf.addWindowListener(new WindowAdapter() {
//            public void windowClosing(WindowEvent e) {
//                System.out.println("done");
//                System.exit(0);
//            }
//        });
//        ImageIcon img = new ImageIcon("src/ex2/img/pokeball.png");
//        jf.setIconImage(img.getImage());
////        jf.addWindowListe;
//        Container contentPane = jf.getContentPane();
//        jf.show();
        GameGUI gui = new GameGUI(4);
        Arena ar = new Arena();
        directed_weighted_graph g = new WDGraph_DS();
        g.addNode(new NodeData(1));
        g.addNode(new NodeData(2));
        g.addNode(new NodeData(3));
        g.addNode(new NodeData(4));
        ar.setGraph(g);
        gui.set_ar(ar);
//        gui.paint();
        gui.show();
    }

}
