package gameClient;

import javax.swing.*;

public class GameGUI extends JFrame {
    private Arena _ar;

    public GameGUI(String name) {
        super(name);
    }

    public void set_ar(Arena _ar) {
        this._ar = _ar;
    }


}
