package game;

import Server.Game_Server_Ex2;
import api.*;
import com.google.gson.JsonParser;

import static game.Algo.*;

public class Ex2 {

    private static GameGUI _win;
    private static Arena _ar;
    private static directed_weighted_graph _graph;

    public static void main(String[] args) {
        if (args.length >= 2) {
            play(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        } else {
            play(13, 314699059);
        }
    }

    private static void play(int scenario_num, int loginId) {
        game_service game = Game_Server_Ex2.getServer(scenario_num); // you have [0,23] games
//       game.login(314699059);
//        game.login(205474026);
        System.out.println("Game Info: " + game);

        initArena(game);
        initGUI(scenario_num, game);

        game.startGame();
        _ar.set_timeStart(game.timeToEnd());

        int iteration = 0;

        while (game.isRunning()) {
            iteration++;
            for (Agent a : _ar.getAgents()) {
                if (a.get_path().isEmpty()) {
                    createPath(game, a);
                }
                if (!a.isMoving()) {
                    nextMove(game, a);
                }

            }
            if (iteration == 600 ) {
                iteration = 0;
                game.move();
                _win.repaint();
//                try {
//                    Thread.sleep(2);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                _ar.update(game);
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

    private static void initGUI(int scenario_num, game_service game) {
        if (_win == null) {
            _win = new GameGUI(scenario_num, game);
        } else {
            _win.set_scenario_num(scenario_num);
        }
        _win.set_ar(_ar);
        _win.setVisible(true);
    }
}
