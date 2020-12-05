package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.*;

public class Ex2c {

    private static GameGUI _win;
    private static Arena _ar;
    private static directed_weighted_graph _graph;
    private static HashMap<Integer, LinkedList<node_data>> _paths = new HashMap<>();

//    static int _counter = 0;

    public static void main(String[] args) {
        play();
    }

    private static void play() {
        final int scenario_num = 11;
        game_service game = Game_Server_Ex2.getServer(scenario_num); // you have [0,23] games
        System.out.println("Game Info: " + game);

        initArena(game);
        initGUI(scenario_num);

        game.startGame();
        while (game.isRunning()) {
            for (CL_Agent a : _ar.getAgents()) {
                if (_paths.get(a.getID()).isEmpty()) {
                    createPath(game, a);
                }
                if (!a.isMoving()) {
                    nextMove(game, a);
                } else {
                    game.move();
                    updateArena(game);
                }

//                System.out.println(game.timeToEnd() /1000.);

                _win.repaint();

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println(game);
        System.exit(0);
    }

    private static void nextMove(game_service game, CL_Agent a) {
        int id = a.getID();
//        if (_paths.get(id).size() == 1) {
//            _flag = true;
//            _ar.remove_pokemonsWithOwner(a.get_curr_fruit());
//            createPath(game, a);
//        }
        if (a.getNextNode() != -1) {
            return;
        }
        int next_dest = _paths.get(id).get(0).getKey();
        _paths.get(id).remove(0);

        long is_choosen = game.chooseNextEdge(id, next_dest);
//        System.out.println("**" + is_choosen);
        game.move();
        updateArena(game);
        System.out.println("Agent: " + id + ", val: " + a.getValue() + "   turned to node: " + next_dest);
    }

    public static void initArena(game_service game) {
        JsonObject jo = JsonParser.parseString(game.toString()).getAsJsonObject().getAsJsonObject("GameServer");
        String graph_path = jo.get("graph").getAsString();
        dw_graph_algorithms ga = new WDGraph_Algo();
        ga.load(graph_path);
        directed_weighted_graph graph = ga.getGraph();
        _graph = graph;
        _ar = new Arena();
        _ar.setGraph(graph);
        _ar.setPokemons(Arena.json2Pokemons(game.getPokemons()));
        placeAgents(jo.get("agents").getAsInt(), game);
        _ar.setAgents(Arena.getAgents(game.getAgents(), _graph));
    }

    private static void initGUI(int scenario_num) {
        _win = new GameGUI(scenario_num);
        _win.set_ar(_ar);
        _win.setVisible(true);
    }

    private static void placeAgents(int numOfAgents, game_service game) {
        findEdges();
        dw_graph_algorithms ga = new WDGraph_Algo(_graph);
        ga.shortestPathDist(0, 0);

        PriorityQueue<node_data> pq = new PriorityQueue<>(new Comparator<node_data>() {
            @Override
            public int compare(node_data o1, node_data o2) {
                return Double.compare(o1.getWeight(), o2.getWeight());
            }
        });

        for (CL_Pokemon i : _ar.getPokemons()) {
            pq.add(_graph.getNode(i.get_edge().getSrc()));
        }
        int div = pq.size() / numOfAgents;
        for (int i = 0; i < numOfAgents; i++) {
            game.addAgent(pq.peek().getKey());
            _paths.put(i, new LinkedList<>());
            for (int j = 0; j < div; j++) {
                pq.poll();
            }
        }
    }

    public static void findEdges() {
        for (CL_Pokemon p : _ar.getPokemons()) {
            geo_location pok_pos = p.getLocation();
            for (node_data n : _graph.getV()) {
                geo_location n_pos = n.getLocation();
                for (edge_data e : _graph.getE(n.getKey())) {
                    geo_location dest_pos = _graph.getNode(e.getDest()).getLocation();
                    if (Math.abs(n_pos.distance(dest_pos) - (n_pos.distance(pok_pos) + pok_pos.distance(dest_pos))) <= 0.0001) {
                        p.set_edge(e);
                        break;
                    }
                }
            }
        }
    }

    private static void updateArena(game_service game) {
        List<CL_Pokemon> pokemons = Arena.json2Pokemons(game.getPokemons());
        List<CL_Agent> agents = Arena.getAgents(game.getAgents(), _graph);
        _ar.setPokemons(pokemons);
        _ar.setAgents(agents);
        findEdges();
    }

    private static void createPath(game_service game, CL_Agent a) {
        List<CL_Pokemon> pokemons_list = _ar.getPokemons();
        dw_graph_algorithms ga = new WDGraph_Algo();
        ga.init(_graph);

        CL_Pokemon min_pokemon = pokemons_list.get(0);
        int n = min_pokemon.get_edge().getSrc();
        double shortest_way = ga.shortestPathDist(a.getSrcNode(), n);

        for (CL_Pokemon p : _ar.getPokemons()) {
            if (!_ar.get_pokemonsWithOwner().contains(p)) {
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

        _paths.put(a.getID(), new LinkedList<>(ga.shortestPath(a.getSrcNode(), min_pokemon.get_edge().getSrc())));
        _paths.get(a.getID()).add(_graph.getNode(min_pokemon.get_edge().getDest()));
        a.set_curr_fruit(min_pokemon);
        _ar.add_pokemonsWithOwner(min_pokemon);
        _paths.get(a.getID()).remove(0);
    }
}
