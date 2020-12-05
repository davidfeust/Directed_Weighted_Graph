package game;

import Server.Game_Server_Ex2;
import api.*;
import com.google.gson.JsonParser;
import okhttp3.internal.concurrent.Task;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Ex2Thread {

    private static GameGUI _win;
    private static Arena _ar;
    private static directed_weighted_graph _graph;

    public static void main(String[] args) {
        play();
    }

    private static void play() {
        final int scenario_num = 11;
        game_service game = Game_Server_Ex2.getServer(scenario_num); // you have [0,23] games
//        game.login(314699059);
        System.out.println("Game Info: " + game);

        initArena(game);
        initGUI(scenario_num);


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

//        ArrayList<Thread> threads_arr = new ArrayList<>();
//        for (Agent a : _ar.getAgents()) {
//            Thread at = new Thread(new AgentThread(game, a));
//            threads_arr.add(at);
////            at.start();
//        }
//        for (Thread at : threads_arr) {
//            at.start();
////                at.join();
//        }

        while (!game.isRunning()) {
            System.out.println(game);
            System.exit(0);
        }
    }

    public static void initArena(game_service game) {
        _ar = new Arena(game);
        _graph = _ar.get_graph();
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

    private static void placeAgents(int num_of_agents, game_service game) {
        dw_graph_algorithms ga = new WDGraph_Algo(_graph);
        ga.shortestPathDist(0, 0);

        PriorityQueue<node_data> pq = new PriorityQueue<>(new Comparator<node_data>() {
            @Override
            public int compare(node_data o1, node_data o2) {
                return Double.compare(o1.getWeight(), o2.getWeight());
            }
        });

        for (Pokemon i : _ar.getPokemons()) {
            pq.add(_graph.getNode(i.get_edge().getSrc()));
        }
        int div = pq.size() / num_of_agents;
        for (int i = 0; i < num_of_agents; i++) {
            game.addAgent(pq.peek().getKey());
            for (int j = 0; j < div; j++) {
                pq.poll();
            }
        }
    }

    private static void nextMove(game_service game, Agent a) {
        int id = a.getId();

        int next_dest = a.get_path().get(0).getKey();
        a.get_path().remove(0);
        if (a.get_path().isEmpty()) {
            _ar.get_pokemonsWithOwner().remove(a.get_curr_fruit().get_edge());
        }

        long is_choosen = game.chooseNextEdge(id, next_dest);
        System.out.println("Agent: " + id + ", val: " + a.getValue() + "   turned to node: " + next_dest);
        System.out.println("\t\ton the way to: " + a.get_curr_fruit());
//        System.out.println("**" + is_choosen);
    }


    private synchronized static void createPath(game_service game, Agent a) {
        dw_graph_algorithms ga = new WDGraph_Algo();
        ga.init(_graph);

        Pokemon min_pokemon = _ar.getPokemons().get(0);
        int n = min_pokemon.get_edge().getSrc();
        double shortest_way = ga.shortestPathDist(a.getSrcNode(), n);

        for (Pokemon p : _ar.getPokemons()) {
            if (!_ar.get_pokemonsWithOwner().contains(p.get_edge())) {
                edge_data pokemon_edge = p.get_edge();
                int s = pokemon_edge.getSrc();
                double dist_src = ga.shortestPathDist(a.getSrcNode(), s);
                if (dist_src < shortest_way) {
                    shortest_way = dist_src;
                    min_pokemon = p;
                    n = s;
                }
            }
        }
        List<node_data> path = ga.shortestPath(a.getSrcNode(), min_pokemon.get_edge().getSrc());
        path.add(_graph.getNode(min_pokemon.get_edge().getDest()));
        path.remove(0);
        a.set_path(path);

        a.set_curr_fruit(min_pokemon);
        _ar.get_pokemonsWithOwner().add(min_pokemon.get_edge());
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
                _ar.update(_game);
                synchronized (this) {
                    _win.repaint();
                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
