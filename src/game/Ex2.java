package game;

import Server.Game_Server_Ex2;
import api.*;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.parallel.Execution;

import javax.swing.*;

import static game.Algo.*;

public class Ex2 {

    private static GameGUI _win;
    private static Arena _ar;
    private static directed_weighted_graph _graph;
    private static game_service _game;

    public static void main(String[] args) {
        try {
            play(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        } catch (Exception e) {
//            play(1, 314699059);
            play(21, 205474026);
        }
    }

    private static void play(int scenario_num, int loginId) {
        if (_game != null) {
            _game.stopGame();
            _win.setVisible(false);
        }
        _game = Game_Server_Ex2.getServer(scenario_num); // you have [0,23] games
//        game.login(loginId);
        System.out.println("Game Info: " + _game);

        initArena(_game);
        initGUI(scenario_num, _game);

        _game.startGame();
        _ar.set_timeStart(_game.timeToEnd());

        int iteration = 0;

        while (_game.isRunning()) {
            iteration++;
            for (Agent a : _ar.getAgents()) {
                if (a.get_path().isEmpty()) {
                    createPath(_game, a);
                }
                _ar.update(_game);
                if (!a.isMoving()) {
                    nextMove(_game, a);
                }
                toMove(a);
            }
//            if (iteration == 100) {
//                iteration = 0;
//                _game.move();
//                _win.repaint();
//                try {
//                    Thread.sleep(2);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
        }
        int moves = JsonParser.parseString(_game.toString()).getAsJsonObject().getAsJsonObject("GameServer").get("moves").getAsInt();
        System.out.println("Grade: " + _ar.getGrade() + "\tMoves: " + moves);
        System.exit(0);
    }

    private static void initAlgo() {
        set_graph(_graph);
        set_ar(_ar);
    }

    private static void initArena(game_service game) {
        _ar = new Arena(game);
        _graph = _ar.get_graph();
        initAlgo();
        int num_of_agents = JsonParser.parseString(game.toString()).getAsJsonObject().getAsJsonObject("GameServer").get("agents").getAsInt();
        placeAgents(num_of_agents, game);
        _ar.updatePokemons(game.getPokemons());
        _ar.updateAgents(game.getAgents());
    }

    private static void initGUI(int scenario_num, game_service game) {
//        _win = new GameGUI(scenario_num, game);
        _win = new GameGUIPlus(scenario_num, game);
        _win.set_ar(_ar);
        _win.setVisible(true);
        _win.repaint();
    }

    public static void toMove(Agent a) {
        if (isClose2Pok(a)) {
            _game.move();
            _win.repaint();
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            _game.move();
            _win.repaint();
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
