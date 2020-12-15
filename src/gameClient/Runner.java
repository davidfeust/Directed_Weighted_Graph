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

    /**
     * Constructor.
     *
     * @param scenario_num level to get the proper scenario from the server
     * @param id           user's id to login
     */
    public Runner(int scenario_num, int id) {
        _scenario_num = scenario_num;
        _id = id;
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
//        _game.login(_id);
//        System.out.println("Game Info: " + _game);

        initArena(_game);
        initGUI();

        _game.startGame();
        long sum_time = _game.timeToEnd();
        _ar.set_timeStart(sum_time);

        Thread painter = new Thread(new Runnable() {
            @Override
            public void run() {
//                try {
//                    while (_game.isRunning()) {
//                        Thread.sleep((long) 1000 / 60);
//                        _win.repaint();
//                    }
//                } catch (InterruptedException exception) {
//                    exception.printStackTrace();
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
            }
        });

        Thread mover = new Thread(new Runnable() {

            @Override
            public void run() {

                while (_game.isRunning()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException exception) {
                        exception.printStackTrace();
                    }
//                    _ar.update(_game);
//                    _game.move();
                    _win.repaint();

//                    System.out.println(_game.move());
                }
            }
        });

//        mover.start();
//        painter.start();
        int num_of_moves = 0;

        int iteration = 0;
        while (_game.isRunning()) {
//            _game.move();

            iteration++;
            long minMoveTime = 100000;
//            long minMoveTime = 0;
            synchronized (_win) {
                int next_dest = -1;//a.getSrcNode();
                for (Agent a : _ar.getAgents()) {
                    _ar.update(_game);
                    if (a.get_path().isEmpty()) {
                        createPath(a);
                    }
                    _ar.update(_game);
                    if (!a.isMoving()) {
                        next_dest = nextMove(_game, a);
                    }
                    long timeToMove = toSleep(a, next_dest);
                    if (timeToMove < minMoveTime) {
                        minMoveTime = timeToMove;
                    }
                }
//                System.out.println("minTimeToMove=" + minMoveTime);
                try {
//                    Thread.sleep(100);
                    Thread.sleep(minMoveTime);
                } catch (InterruptedException exception) {
                    exception.printStackTrace();
                }
//                    _ar.update(_game);
                _game.move();
                _win.repaint();
//                System.out.println(num_of_moves++);
            }
//            try {
//                if (minMoveTime > 10)
//                    minMoveTime += 0;
//                Thread.sleep(minMoveTime);
//            } catch (InterruptedException exception) {
//                exception.printStackTrace();
//            }
//            _game.move();
//            _win.repaint();
        }
        int moves = JsonParser.parseString(_game.toString()).getAsJsonObject().getAsJsonObject("GameServer").get("moves").getAsInt();
        System.out.printf("Level: %d\t\tGrade: %d,\tMoves: %d,\tAvg moves per sec: %.3f%n", _scenario_num, _ar.getGrade(), moves, moves / ((double) sum_time / 1000));
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
        _win.setTitle("Pokemons Game " + _scenario_num);
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
