package game;


import api.game_service;

import java.awt.event.*;

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
//        System.out.println("-----------------222222222222222222222------------------------" + str);
        String[] strA = str.split("\\D+");
//        Pattern p = Pattern.compile("numFound=\"([0-9]+)\"");
//        Matcher m = p.matcher(str);
//        String[] strA= {m.toString()};
//        this._scenario_num = Integer.parseInt(strA[0]);0
//        System.out.println("-----------------------------------------" + strA[1]);
//        Ex2b.
//        Ex2b.main(strA);
        _game.stopGame();
        Ex2.main(new String[]{strA[1], "205474026"});
    }
}
