package game;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Panel extends JPanel {

    private JFrame _frame;
    private static JTextField _id_field;
    private static JTextField _s_n;
    private JButton _button;
    private JLabel _id_label;
    private JLabel _level_label;
    private JLabel _time;
    private JLabel _level;
    private JLabel _score;
    private BufferedImage _name;
    private JLabel _name_img;
    private Arena _ar;
    private int _scenario_num;
    private Controller _ctrl;
    static ImageIcon[] _image_sound;
    static JButton un_mute;
    static boolean muteFlag;

    public Panel(JFrame frame, Controller ctrl) {
        _frame = frame;
        _ctrl = ctrl;
        setBackground(Color.gray);
        setBorder(new BevelBorder(BevelBorder.RAISED));

        sound_button();
        insertBox();
        infoBox();
        nameImg();

        setPreferredSize(new Dimension(_frame.getWidth(), 110));
        repaint();
    }

    private void insertBox() {
        _id_label = new JLabel("ID: ");
        _level_label = new JLabel("level: ");
        _id_field = new JTextField();
        _s_n = new JTextField();
        _button = new JButton("Submit");
        _button.addActionListener(_ctrl);
        _id_label.setForeground(Color.white);
        _level_label.setForeground(Color.white);
        _id_label.setForeground(Color.white);
        add(_button);
        add(_id_field);
        add(_s_n);
        add(_level_label);
        add(_id_label);
    }

    private void updateInsertBox() {
        _id_field.setBounds(getWidth() - 150, 20, 120, 22);
        _id_label.setBounds(getWidth() - 185, 20, 50, 22);
        _s_n.setBounds(getWidth() - 150, 45, 120, 22);
        _level_label.setBounds(getWidth() - 185, 45, 50, 22);
        _button.setBounds(getWidth() - 135, 70, 95, 25);
    }

    private void infoBox() {
        _time = new JLabel("Time to end: ");
        _time.setForeground(Color.white);
        add(_time);
        _level = new JLabel("Game level: ");
        _level.setForeground(Color.white);
        add(_level);
        _score = new JLabel("Score: ");
        _score.setForeground(Color.white);
        add(_score);
    }

    private void updateInfoBox() {
        if (_ar != null) {
            _time.setText("Time to end: " + (int) _ar.getTime() / 1000);
            _level.setText("Game level: " + _scenario_num);
            _score.setText("Score: " + _ar.getGrade());
        }
        _time.setBounds(20, 20, 150, 30);
        _level.setBounds(20, 40, 150, 30);
        _score.setBounds(20, 60, 150, 30);

    }

    private void nameImg() {
        _name = null;
        try {
            _name = ImageIO.read(new File("img/name.gif"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        _name_img = new JLabel();
        double size = 0.8;
        _name_img.setIcon(new javax.swing.ImageIcon(_name.getScaledInstance((int) (324 * size), (int) (124 * size), WIDTH)));
        add(_name_img);
    }

    private void updateNameImg() {
        _name_img.setBounds(_frame.getWidth() / 2 - 150, 0, 300, 100);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        updateInsertBox();
        updateInfoBox();
        updateNameImg();
        update_sound_button();
    }
    public void sound_button(){

         _image_sound = new ImageIcon[2];

         _image_sound[0] = (new ImageIcon("img/mute.png"));
         _image_sound[1] = (new ImageIcon("img/unmute.png"));

        Image image = _image_sound[0].getImage(); // transform it
        Image newimg = image.getScaledInstance(27, 27,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        _image_sound[0] = new ImageIcon(newimg);

        image = _image_sound[1].getImage(); // transform it
        newimg = image.getScaledInstance(27, 27,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        _image_sound[1] = new ImageIcon(newimg);

         un_mute = new JButton("mute");
        un_mute.setFont(new Font("david", Font.PLAIN, 1));
         un_mute.addActionListener(_ctrl);
         un_mute.setIcon(_image_sound[0]);
        muteFlag=true;
         add(un_mute);
    }
    public void update_sound_button(){
        un_mute.setBounds(_frame.getWidth()  - 250, 27, 40, 40);
    }

    public static int changeMuteIcon(){
        if(muteFlag==true){
        un_mute.setIcon(_image_sound[1]);
        muteFlag=false;
        return 1;//indicate for stopping the music
        }
        else {
            un_mute.setIcon(_image_sound[0]);
            muteFlag=true;
            return 0;//indicate for starting the music
        }
    }

    public void set_ar(Arena ar) {
        _ar = ar;
    }

    public void set_level(int level) {
        _scenario_num = level;
    }

    public static String getLevel() {
        return _s_n.getText();
    }

    public static String getId() {
        return _id_field.getText();
    }
}
