package game;

import api.game_service;
import api.geo_location;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class GameGUIPlus extends GameGUI {

    private BufferedImage _name;
    private BufferedImage _image_node;
    private BufferedImage _image_pok;

    public GameGUIPlus(int scenario_num, game_service game) {
        super(scenario_num, game);
        loadImg();
//        setBackground(Color.BLACK);
    }

    @Override
    public void paintComponents(Graphics g) {
        super.paintComponents(g);
//        g.drawImage(_name, 300, 30, null);
    }

    private void loadImg() {
        try {
            _image_node = ImageIO.read(new File("src/ex2/img/node.jpg"));
            _image_pok = ImageIO.read(new File("src/ex2/img/pokeball.png"));
            _name = ImageIO.read(new File("src/ex2/img/name.gif"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void nodeIcon(Graphics g, int radius, geo_location fp) {
        g.drawImage(_image_node, (int) fp.x() - radius, (int) fp.y() - radius, 5 * radius, 5 * radius, null);
    }

    @Override
    public void pokIcon(Graphics g, int radius, geo_location fp) {
        g.drawImage(_image_pok, (int) fp.x() - radius, (int) fp.y() - radius, 3 * radius, 3 * radius, null);
    }
}
