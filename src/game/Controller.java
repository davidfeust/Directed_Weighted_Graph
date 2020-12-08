package game;


import api.game_service;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Controller extends WindowAdapter implements ActionListener {

    //    GameGUI _gui;
    game_service _game;

    public Controller(game_service game) {
        _game = game;
    }

    @Override
    public void windowClosing(WindowEvent e) {
        System.out.println("done");
        System.exit(0);
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String str = e.getActionCommand();
        String[] strA = str.split("\\D+");
        _game.stopGame();
//        Ex2.main(new String[]{strA[1], "205474026"});
        Ex2.play(Integer.parseInt(strA[1]), 205474026);
    }
}
