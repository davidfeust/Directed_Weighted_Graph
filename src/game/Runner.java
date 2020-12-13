package game;

import Server.Game_Server_Ex2;
import api.directed_weighted_graph;
import api.game_service;
import com.google.gson.JsonParser;

import static game.Algo.*;

public class Runner implements Runnable {


    private GameGUI _win;
    private Arena _ar;
    private directed_weighted_graph _graph;
    private game_service _game;
    private final int _scenario_num, _id;

    public Runner(int scenario_num, int id) {
        _scenario_num = scenario_num;
        _id = id;
    }

    @Override
    public void run() {
        _game = Game_Server_Ex2.getServer(_scenario_num); // you have [0,23] games
//        _game.login(_id);
//        System.out.println("Game Info: " + _game);

        initArena(_game);
        initGUI();

        _game.startGame();
        _ar.set_timeStart(_game.timeToEnd());

        Thread painter = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (_game.isRunning()) {
                        Thread.sleep((long) 1000 / 60);
                        _win.repaint();
                    }
                } catch (InterruptedException exception) {
                    exception.printStackTrace();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        Thread mover = new Thread(new Runnable() {
            @Override
            public void run() {

                while (_game.isRunning()) {
                    long sleep = Integer.MAX_VALUE;
                    for (Agent a : _ar.getAgents()) {
                        long tm = toSleep(a, 0);
                        if (tm == -1) {
                            createPath(_game, a);
                            continue;
                        }
                        sleep = Math.min(tm, sleep);
                    }
                    try {
                        Thread.sleep(sleep);
                    } catch (InterruptedException exception) {
                        exception.printStackTrace();
                    }
                    _ar.update(_game);
//                    _game.move();
                    _win.repaint();

//                    System.out.println(_game.move());
                }
            }
        });

//        mover.start();
        painter.start();

        int iteration = 0;
        while (_game.isRunning()) {
            iteration++;
            long minMoveTime = 100000;
//            long minMoveTime = 0;
            for (Agent a : _ar.getAgents()) {
                _ar.update(_game);
                if (a.get_path().isEmpty()) {
                    createPath(_game, a);
                }
                _ar.update(_game);
                int next_dest = a.getNextNode();
                if (!a.isMoving()) {
                    next_dest = nextMove(_game, a);
                }
//                _ar.updateAgents(_game.getAgents());
                long timeToMove = toSleep(a, next_dest);
                if (timeToMove < minMoveTime) {
                    minMoveTime = timeToMove;
                }
            }
            System.out.println("minTimeToMove=" + minMoveTime);

            try {
                Thread.sleep(minMoveTime);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
            _game.move();

        /*    synchronized (this) {
                Timer timer = new Timer("Move");
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        _game.move();
                    }
                }, minMoveTime);
            }*/
//            String move = _game.move();

//            if (iteration == 300) {
//            iteration = 0;
//            _game.move();
//            }

        }

        int moves = JsonParser.parseString(_game.toString()).getAsJsonObject().getAsJsonObject("GameServer").get("moves").getAsInt();
        System.out.println("Level: " + _scenario_num + "\t\tGrade: " + _ar.getGrade() + "\tMoves: " + moves);
//        System.exit(0);
//        mover.stop();
//        painter.stop();
    }

    private void initAlgo() {
        set_graph(_graph);
        set_ar(_ar);
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

    private void initGUI() {
        _win.set_ar(_ar);
        _win.set_level(_scenario_num);
        _win.setTitle("Pockemons Game " + _scenario_num);
        _win.setVisible(true);
    }

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
