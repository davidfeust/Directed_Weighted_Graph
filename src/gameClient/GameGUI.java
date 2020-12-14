package gameClient;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This class is the main GUI of the Pokemons Game.
 * This class consists of two main panels:
 * _panel - the upper bar panel, shows info and allows changes level game
 * _view - shows the board game, the agents and the pokemons in action.
 */
public class GameGUI extends JFrame {

    private Panel _panel;
    private GameView _view;
    private Controller _ctrl;
    private BufferedImage _image_pok;

    /**
     * init the JPanels, menu, and the default settings of the GUI
     * @param level level game
     * @param ctrl {@link Controller}, which control the buttons
     */
    public GameGUI(int level, Controller ctrl) {
        super();

        _ctrl = ctrl;
        _panel = new Panel(this, _ctrl);
//        _view = new GameView(this, 5);
        _view = new GameViewPlus();
        _panel.set_level(level);

        addWindowListener(_ctrl);
        Dimension user_dim = Toolkit.getDefaultToolkit().getScreenSize();
        getRootPane().setDefaultButton(_panel.get_submit());

        this.setPreferredSize(new Dimension((int) (user_dim.getWidth()*0.75) , (int) (user_dim.getHeight()*0.75)));

        menu();
        icon();

        this.setLayout(new BorderLayout());
        this.add(_panel, BorderLayout.NORTH);
        this.add(_view, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * set icon game on the window
     */
    private void icon() {
        try {
            _image_pok = ImageIO.read(new File("img/pokeball.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        setIconImage(_image_pok);
    }

    /**
     * init menu bar, contains 23 scenario games.
     */
    private void menu() {
        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Level");
        menuBar.add(menu);
        this.setMenuBar(menuBar);
        ArrayList<MenuItem> menuItems = new ArrayList<>();
        String s = "Level number - ";
        for (int i = 0; i < 24; i++) {
            menuItems.add(new MenuItem(s + i));
        }
        for (MenuItem i : menuItems) {
            i.addActionListener(_ctrl);
            menu.add(i);
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
    }

    public void set_level(int level) {
        _panel.set_level(level);
    }

    public void set_ar(Arena ar) {
        _view.set_ar(ar);
        _panel.set_ar(ar);
    }
}