package game;

import api.game_service;
import com.google.gson.JsonParser;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Controller extends WindowAdapter implements ActionListener {

    public Runner _run;
    private Thread _thread;
    private GameGUI _win;
    private int _id, _level;

    public Controller(Runner run, Thread thread, int id, int level) {
        _run = run;
        _thread = thread;
        _id = id;
        _level = level;
    }

    @Override
    public void windowClosing(WindowEvent e) {
        System.out.println("done");
        System.exit(0);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        game_service game = _run.get_game();
        game.stopGame();
//        _thread.stop();
//        int moves = JsonParser.parseString(game.toString()).getAsJsonObject().getAsJsonObject("GameServer").get("moves").getAsInt();
//        System.out.println("Level: " + _level + "\t\tGrade: " + _run.get_ar().getGrade() + "\tMoves: " + moves);

        String str = e.getActionCommand();
        String[] strA = str.split("\\D+");
        _level = Integer.parseInt(strA[1]);

        _run = new Runner(_level, _id);
        _run.set_win(_win);
        _thread = new Thread(_run);
        _thread.start();
    }

    public void set_win(GameGUI win) {
        _win = win;
    }
}

