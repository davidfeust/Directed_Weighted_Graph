package trys;

import gameClient.GameView;
import gameClient.Panel;

import javax.swing.*;
import java.awt.*;

public class MyFrame extends JFrame {

    Panel _panel;
    GameView _view;

    public MyFrame() {
        setSize(new Dimension(1200, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//        _panel = new Panel(this);
//        add(_panel);

        _view = new GameView();
        add(_view);


        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
//        _panel.paint(g);
//        repaint();
    }

    public static void main(String[] args) {
        new MyFrame();
    }
}
