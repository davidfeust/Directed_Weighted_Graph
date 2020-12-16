package gameClient;

import api.game_service;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * This class is the Controller of the GUI.
 */
public class Controller extends WindowAdapter implements ActionListener {

    public Runner _run;
    private Thread _thread;
    private GameGUI _win;
    private int _id, _level;

    /**
     * Constructor.
     * get the main Runner, and the thread performs it.
     *
     * @param run    the main Runner of the game
     * @param thread the executor Tread
     * @param id     user's id to login
     * @param level  one of the scenario games
     */
    public Controller(Runner run, Thread thread, int id, int level) {
        _run = run;
        _thread = thread;
        _id = id;
        _level = level;
    }

    /**
     * Closes window and exit the program
     *
     * @param e window event
     */
    @Override
    public void windowClosing(WindowEvent e) {
        System.out.println("done");
        System.exit(0);
    }

    /**
     * Action Performed. handle about all action performed in {@link GameGUI}, and {@link Panel}.
     * Possible actions:
     * 1. mute / un-mute music
     * 2. submit the entry id and level
     * 3. choose level from menu.
     *
     * @param e action even
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String str = e.getActionCommand();
        if (str.equals("mute")) {
            if (gameClient.Panel.changeMuteIcon() == 1) {
                gameClient.GameViewPlus.soundOff();
            } else {
                gameClient.GameViewPlus.soundOn();
            }
        } else {
            int id = -1;
            if (str.equals("Submit")) {
                try {
                    _level = Integer.parseInt(Panel.getLevel());
                } catch (NumberFormatException ignored) {
                    return;
                }
                try {
                    id = Integer.parseInt(Panel.getId());
                } catch (NumberFormatException ignored) {
                }

            } else {
                String[] strA = str.split("\\D+");
                _level = Integer.parseInt(strA[1]);
            }

            game_service game = _run.get_game();
            if (game.isRunning()) {
                System.out.print("Game stopped:\t");
                game.stopGame();
            }
            try {
                _thread.join();
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }

            _run = new Runner(_level, id);
            _run.set_win(_win);
            _thread = new Thread(_run);
            _thread.start();
        }
    }

    public void set_win(GameGUI win) {
        _win = win;
    }
}

