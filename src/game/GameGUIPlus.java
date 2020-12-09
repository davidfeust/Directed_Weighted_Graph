package game;

import api.game_service;
import api.geo_location;
import game.util.Point3D;
import game.util.Range2Range;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class GameGUIPlus extends GameGUI {

    private BufferedImage _name;
    private BufferedImage _image_node;
    private BufferedImage _image_pok;
    private BufferedImage[] _image_agents;
    private BufferedImage[] _image_fruits;

    public GameGUIPlus(int scenario_num, Controller ctrl) {
        super(scenario_num, ctrl);
        loadImg();
        setIconImage(_image_pok);
        sound();

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
//                _image_agents[i] = ImageIO.read(new File("src/ex2/img/agent" + (i + 1) + ".png"));
                _image_agents[i] = ImageIO.read(new File("img/agent" + (i + 1) + ".png"));
            }

            _image_fruits = new BufferedImage[3];
            for (int i = 0; i < 3; i++) {
//                _image_fruits[i] = ImageIO.read(new File("src/ex2/img/fruit" + (i + 1) + ".png"));
                _image_fruits[i] = ImageIO.read(new File("img/fruit" + (i + 1) + ".png"));
            }
//            _image_node = ImageIO.read(new File("src/ex2/img/node.jpg"));
//            _image_pok = ImageIO.read(new File("src/ex2/img/pokeball.png"));
//            _name = ImageIO.read(new File("src/ex2/img/name.gif"));
            _image_node = ImageIO.read(new File("img/node.jpg"));
            _image_pok = ImageIO.read(new File("img/pokeball.png"));
            _name = ImageIO.read(new File("img/name.gif"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    @Override
//    public void nodeIcon(Graphics g, int radius, geo_location fp) {
//        g.drawImage(_image_node, (int) fp.x() - radius, (int) fp.y() - radius, 5 * radius, 5 * radius, null);
//    }

    @Override
    protected void pokIcon(Graphics g, int radius, geo_location fp,int flag) {
        g.drawImage(_image_fruits[flag], (int) fp.x() - radius, (int) fp.y() - radius, 3 * radius, 3 * radius, null);
    }

    @Override
    protected void agentIcon(Graphics g, int r, geo_location fp, int id) {
        g.drawImage(_image_agents[(id % 4)], (int) fp.x() - r, (int) fp.y() - r - 1, 4 * r, 4 * r, null);
    }

    @Override
    protected void drawPokemons(Graphics g, Arena ar, Range2Range _w2f) {
        List<Pokemon> fs = new ArrayList<>(ar.getPokemons());
        int flag = 2;
        if (fs.isEmpty()) {
            return;
        }
        for (Pokemon f : fs) {
            if (f == null) continue;
            Point3D c = f.get_pos();
            int radius = 10;
//            g.setColor(Color.green);
            if (f.get_type() < 0) {
                 flag = 1;
//                g.setColor(Color.orange);
            }
            if (f.get_value() > 10) {
                    flag = 0;
            }
            if (c != null) {
                geo_location fp = _w2f.world2frame(c);
                pokIcon(g, radius, fp,flag);
                g.setColor(Color.BLACK);
                g.setFont(new Font(null, Font.BOLD, 12));
                g.drawString("" + (int) f.get_value(), (int) fp.x(), (int) fp.y() + 2);
            }
        }
    }

    public void sound() {
        try {
            Clip clip = AudioSystem.getClip();
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File("songs/pokemonSong.wav"));
            clip.open(ais);
            clip.loop(Clip.LOOP_CONTINUOUSLY);

        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException ex) {
            ex.printStackTrace();
        }
    }

}
