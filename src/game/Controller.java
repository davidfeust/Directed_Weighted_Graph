package game;

import api.game_service;
import com.google.gson.JsonParser;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Controller extends WindowAdapter implements ActionListener {

    public static Runner _run;

    //    public static RunnerTreads _run;
    private static Thread _thread;
    private static GameGUI _win;
    private static int _id = 205474026, _level = 23;

    public Controller(Runner run, Thread thread) {
        _run = run;
        _thread = thread;
    }

    @Override
    public void windowClosing(WindowEvent e) {
        System.out.println("done");
        System.exit(0);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String str = e.getActionCommand();
        String[] strA = str.split("\\D+");

        game_service game = _run.get_game();
        game.stopGame();
        int moves = JsonParser.parseString(game.toString()).getAsJsonObject().getAsJsonObject("GameServer").get("moves").getAsInt();
        System.out.println("Grade: " + _run.get_ar().getGrade() + "\tMoves: " + moves);
        _thread.stop();

        _run = new Runner(Integer.parseInt(strA[1]), _id);
//        _run = new RunnerTreads(Integer.parseInt(strA[1]), _id);
        _run.set_win(_win);
        _thread = new Thread(_run);
        _thread.start();
    }
    public void set_win(GameGUI win) {
        _win = win;
    }
}

