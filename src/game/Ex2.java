package game;

import Server.Game_Server_Ex2;
import api.*;
import com.google.gson.JsonParser;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import static game.Algo.*;

public class Ex2 {

    private static GameGUI _win;
    private static Arena _ar;
    private static directed_weighted_graph _graph;

    public static void main(String[] args) {
        play();
    }

    private static void play() {
        final int scenario_num = 23;
        game_service game = Game_Server_Ex2.getServer(scenario_num); // you have [0,23] games
//        game.login(314699059);
        System.out.println("Game Info: " + game);

        initArena(game);
        initGUI(scenario_num);

        game.startGame();
        _ar.set_timeStart(game.timeToEnd());

        while (game.isRunning()) {
            for (Agent a : _ar.getAgents()) {
                if (a.get_path().isEmpty()) {
                    createPath(game, a);
                }
                if (!a.isMoving()) {
                    nextMove(game, a);
                }
                game.move();
                _ar.update(game);
                _win.repaint();

                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println(game);
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
//        _ar.updatePokemons(game.getPokemons());
        _ar.updateAgents(game.getAgents());
    }

    private static void initGUI(int scenario_num) {
        _win = new GameGUI(scenario_num);
        _win.set_ar(_ar);
        _win.setVisible(true);
    }
}
