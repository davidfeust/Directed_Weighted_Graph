package game;

import Server.Game_Server_Ex2;
import api.directed_weighted_graph;
import api.game_service;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static game.Algo.*;

public class Ex2Thread {

    private static GameGUI _win;
    private static Arena _ar;
    private static directed_weighted_graph _graph;

    public static void main(String[] args) {
        play();
    }

    private static void play() {
        final int scenario_num = 23;
        game_service game = Game_Server_Ex2.getServer(scenario_num); // you have [0,23] games
//      game.login(314699059);
        System.out.println("Game Info: " + game);

        initArena(game);
        initGUI(scenario_num, game);

        game.startGame();
        _ar.set_timeStart(game.timeToEnd());

        ExecutorService pool = Executors.newFixedThreadPool(_ar.getAgents().size());
        ArrayList<Runnable> run_arr = new ArrayList<>();

        for (Agent a : _ar.getAgents()) {
            run_arr.add(new AgentThread(game, a));
        }
        for (Runnable tmp_run : run_arr) {
            pool.execute(tmp_run);
        }
        pool.shutdown();
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
        _win = new GameGUI(scenario_num, game);
        _win.set_ar(_ar);
        _win.setVisible(true);
    }


    private static class AgentThread implements Runnable {

        game_service _game;
        Agent _a;

        public AgentThread(game_service g, Agent a) {
            _game = g;
            _a = a;
        }

        @Override
        public void run() {
            while (_game.isRunning()) {
                if (_a.get_path().isEmpty()) {
                    createPath(_game, _a);
                }
                if (!_a.isMoving()) {
                    nextMove(_game, _a);
                }

                _game.move();
                synchronized (this) {
                    _win.repaint();
                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(_game);
            System.exit(0);
        }
    }

    private static void initAlgo() {
        set_graph(_graph);
        set_ar(_ar);
    }
}
