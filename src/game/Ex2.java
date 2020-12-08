package game;


import api.game_service;
import com.google.gson.JsonParser;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Ex2 {

    public static Runner _run;
    //    public static RunnerTreads _run;
    private static Thread _thread;
    private static int _id = 205474026, _level = 23;

    public static void main(String[] args) {
        try {
            _level = Integer.parseInt(args[0]);
            _id = Integer.parseInt(args[1]);
        } catch (Exception ignored) {
        }
        _run = new Runner(_level, _id);
//        _run = new RunnerTreads(_level, _id);
        _thread = new Thread(_run);
        Controller ctrl = new Controller(_run, _thread);
        GameGUIPlus win = new GameGUIPlus(_level, ctrl);
        _run.set_win(win);
        ctrl.set_win(win);
        _thread.start();
    }


}
