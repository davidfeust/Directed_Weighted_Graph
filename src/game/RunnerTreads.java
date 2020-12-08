package game;

import Server.Game_Server_Ex2;
import api.directed_weighted_graph;
import api.game_service;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static game.Algo.*;

public class RunnerTreads implements Runnable {

    private static GameGUI _win;
    private static Arena _ar;
    private static directed_weighted_graph _graph;
    private static game_service _game;
    private int _scenario_num, _id;

    public RunnerTreads(int scenario_num, int id) {
        _scenario_num = scenario_num;
        _id = id;
    }

    @Override
    public void run() {
        if (_game != null) {
            _game.stopGame();
            _win.setVisible(false);
        }
        _game = Game_Server_Ex2.getServer(_scenario_num); // you have [0,23] games
//      game.login(314699059);
        System.out.println("Game Info: " + _game);

        initArena(_game);
        initGUI(_scenario_num, _game);

        _game.startGame();
        _ar.set_timeStart(_game.timeToEnd());

        ExecutorService pool = Executors.newFixedThreadPool(_ar.getAgents().size());
        ArrayList<Runnable> run_arr = new ArrayList<>();

        for (Agent a : _ar.getAgents()) {
            run_arr.add(new AgentThread(_game, a));
        }
        for (Runnable tmp_run : run_arr) {
            pool.execute(tmp_run);
        }
        pool.shutdown();


        int moves = JsonParser.parseString(_game.toString()).getAsJsonObject().getAsJsonObject("GameServer").get("moves").getAsInt();
        System.out.println("Level: " + _scenario_num + "\t\tGrade: " + _ar.getGrade() + "\tMoves: " + moves);
//        System.exit(0);
    }

    private void initArena(game_service game) {
        _ar = new Arena(game);
        _graph = _ar.get_graph();
        initAlgo();
        int num_of_agents = JsonParser.parseString(game.toString()).getAsJsonObject().getAsJsonObject("GameServer").get("agents").getAsInt();
        placeAgents(num_of_agents, game);
        _ar.updatePokemons(game.getPokemons());
        _ar.updateAgents(game.getAgents());
    }

    private void initGUI(int scenario_num, game_service game) {
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

    public game_service get_game() {
        return _game;
    }

    public Arena get_ar() {
        return _ar;
    }

    private void initAlgo() {
        set_graph(_graph);
        set_ar(_ar);
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
                synchronized (this) {
                    _ar.update(_game);
                    _win.repaint();
                }
                if (!_a.isMoving()) {
                    nextMove(_game, _a);
                }
                toMove(_a);

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
}
