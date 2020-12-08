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
    private BufferedImage[] _image_agents;

    public GameGUIPlus(int scenario_num, game_service game) {
        super(scenario_num, game);
        loadImg();
//        setBackground(Color.gray);
    }

    @Override
    public void paintComponents(Graphics g) {
        super.paintComponents(g);
        g.drawImage(_name, (int) getWidth() / 2 - getHeight() / 7, 50, getWidth() / 5, getHeight() / 6, null);
    }

    private void loadImg() {
        try {
            _image_agents = new BufferedImage[4];
            for (int i = 0; i < 4; i++) {
                _image_agents[i] = ImageIO.read(new File("src/ex2/img/agent" + (i + 1) + ".png"));
            }

            _image_node = ImageIO.read(new File("src/ex2/img/node.jpg"));
            _image_pok = ImageIO.read(new File("src/ex2/img/pokeball.png"));
            _name = ImageIO.read(new File("src/ex2/img/name.gif"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    @Override
//    public void nodeIcon(Graphics g, int radius, geo_location fp) {
//        g.drawImage(_image_node, (int) fp.x() - radius, (int) fp.y() - radius, 5 * radius, 5 * radius, null);
//    }

    @Override
    public void pokIcon(Graphics g, int radius, geo_location fp) {
        g.drawImage(_image_pok, (int) fp.x() - radius, (int) fp.y() - radius, 3 * radius, 3 * radius, null);
    }

    @Override
    public void agentIcon(Graphics g, int r, geo_location fp, int id) {
        g.drawImage(_image_agents[(id % 4)], (int) fp.x() - r, (int) fp.y() - r -1, 4 * r, 4 * r, null);
    }
}
