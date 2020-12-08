package game;

import Server.Game_Server_Ex2;
import api.directed_weighted_graph;
import api.game_service;
import com.google.gson.JsonParser;

import static game.Algo.*;

public class Runner implements Runnable {



    private GameGUI _win;// = new GameGUI();
    private Arena _ar;
    private directed_weighted_graph _graph;
    private game_service _game;
    private int _scenario_num, _id;

    public Runner(int scenario_num, int id) {
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
//        _game.login(_id);
//        System.out.println("Game Info: " + _game);

        initArena(_game);
        initGUI();

        _game.startGame();
        _ar.set_timeStart(_game.timeToEnd());

        int iteration = 0;

        while (_game.isRunning()) {
//            iteration++;
            for (Agent a : _ar.getAgents()) {
                if (a.get_path().isEmpty()) {
                    createPath(_game, a);
                }
                _ar.update(_game);
                if (!a.isMoving()) {
                    nextMove(_game, a);
                }
//                toMove(a);
            }
//            if (iteration == 0) {
//                iteration = 0;
                _game.move();
                _win.repaint();
//                try {
//                    Thread.sleep(2);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
        }
        int moves = JsonParser.parseString(_game.toString()).getAsJsonObject().getAsJsonObject("GameServer").get("moves").getAsInt();
        System.out.println("Level: " + _scenario_num + "\t\tGrade: " + _ar.getGrade() + "\tMoves: " + moves);
//        System.exit(0);
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
//        _win = new GameGUI(scenario_num, game);
//        _win = new GameGUIPlus(scenario_num, game);
        _win.set_ar(_ar);
        _win.setVisible(true);
    }

    public void toMove(Agent a) {
//        if (isClose2Pok(a)) {
//            _game.move();
//            _win.repaint();
//            try {
//                Thread.sleep(2);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        } else {
//            _game.move();
//            _win.repaint();
//            try {
//                Thread.sleep(50);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
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
