package gameClient;

import Server.Game_Server_Ex2;
import api.directed_weighted_graph;
import api.game_service;
import com.google.gson.JsonParser;

import static gameClient.Algo.*;

/**
 * This Runnable class is responsible for running the Pokemon game.
 * Runner class can run only one scenario game. when another scenario game chosen,
 * the thread runs this Runner will stop, and new thread with new Runner will start.
 */
public class Runner implements Runnable {

    private GameGUI _win;
    private Arena _ar;
    private directed_weighted_graph _graph;
    private game_service _game;
    private final int _scenario_num, _id;
    private final Thread _painter_thread;

    /**
     * Constructor.
     *
     * @param scenario_num level to get the proper scenario from the server
     * @param id           user's id to login
     */
    public Runner(int scenario_num, int id) {
        _scenario_num = scenario_num;
        _id = id;
        _painter_thread = new Thread(new Painter());
    }

    /**
     * Run current game.
     * init the Arena data base, from the data taken from the server.
     * also init GUI to show the game.
     * in the main loop the game runs, and in every iteration all the Agents without path to Pokemon
     * will get path, and all the standing Agent will move to theirs destination.
     * when the time's up, the grade and the numbers of move will print.
     */
    @Override
    public void run() {
        _game = Game_Server_Ex2.getServer(_scenario_num); // you have [0,23] games

        if (_id != -1) {
            _game.login(_id);
            System.out.println("logged in for " + _id);
        }

//        System.out.println("Game Info: " + _game);

        initArena(_game);
        initGUI();

        _game.startGame();
        long sum_time = _game.timeToEnd();
        _ar.set_timeStart(sum_time);
        GameView.set_startT(System.currentTimeMillis());
        _ar.update(_game);

        _painter_thread.start();

        while (_game.isRunning()) {
            long minSleepTime = Integer.MAX_VALUE;
            int next_dest = -1;

            for (Agent a : _ar.getAgents()) {
                _ar.update(_game);
                if (a.get_path().isEmpty()) {
                    createPath(a);
                }
                _ar.update(_game);
                if (!a.isMoving()) {
                    next_dest = nextMove(_game, a);
                }
                long timeToSleep = toSleep(a, next_dest);
                if (timeToSleep < minSleepTime) {
                    minSleepTime = timeToSleep;
                }
            }

            // sleep before move
            try {
                Thread.sleep(minSleepTime);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
            _game.move();
        }

        // finish _painter_thread
        try {
            _painter_thread.join();
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
        int moves = JsonParser.parseString(_game.toString()).getAsJsonObject().getAsJsonObject("GameServer").get("moves").getAsInt();
        System.out.printf("Level: %d\t\tGrade: %d,\tMoves: %d,\tAvg moves per sec: %.3f%n", _scenario_num, _ar.getGrade(), moves, moves / ((double) sum_time / 1000));
    }

    /**
     * Repaint the screen 60 times in second
     */
    class Painter implements Runnable {
        @Override
        public void run() {
            while (_game.isRunning()) {
                try {
                    Thread.sleep(1000 / 60);
                } catch (InterruptedException exception) {
                    exception.printStackTrace();
                }
                _win.repaint();
            }
        }
    }

    /**
     * init the arena data base before starting game.
     *
     * @param game game service
     */
    private void initArena(game_service game) {
        _ar = new Arena(game);
        _graph = _ar.get_graph();
        initAlgo();
        int num_of_agents = JsonParser.parseString(game.toString()).getAsJsonObject().getAsJsonObject("GameServer").get("agents").getAsInt();
        placeAgents(num_of_agents, game);
        _ar.updatePokemons(game.getPokemons());
        _ar.updateAgents(game.getAgents());
    }

    /**
     * init GUI before starting game with the Arena and the level number.
     */
    private void initGUI() {
        _win.set_ar(_ar);
        _win.set_level(_scenario_num);
        _win.setTitle("Pokemons Game " + _scenario_num + (_id != -1 ? (" - " + _id) : ""));
    }

    private void initAlgo() {
        set_graph(_graph);
        set_ar(_ar);
    }

    // Getters & Setters:

    public game_service get_game() {
        return _game;
    }

    public Arena get_ar() {
        return _ar;
    }

    public void set_win(GameGUI win) {
        _win = win;
    }
}
