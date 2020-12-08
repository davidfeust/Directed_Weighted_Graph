package game;


import api.game_service;
import com.google.gson.JsonParser;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Ex2 extends WindowAdapter implements ActionListener {

    public static Runner _run;
    private static Thread _thread;
    private static int _id, _level = 0;

    public static void main(String[] args) {
        try {
            _level = Integer.parseInt(args[0]);
            _id = Integer.parseInt(args[1]);
        } catch (Exception e) {
//            _level = 23;
            _id = 205474026;
        }
        _run = new Runner(_level, _id);
        _thread = new Thread(_run);
        _thread.start();
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

        Runner run = new Runner(Integer.parseInt(strA[1]), _id);
        _thread = new Thread(run);
        _thread.start();
    }
}
