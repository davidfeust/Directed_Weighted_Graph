package game;

import Server.Game_Server_Ex2;
import api.*;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.*;

public class Ex2 {

    private static GameGUI _win;
    private static Arena _ar;
    private static directed_weighted_graph _graph;

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

//        game.login(314699059);

        game.startGame();
        while (game.isRunning()) {
            for (Agent a : _ar.getAgents()) {
                if (a.get_path().isEmpty()) {
                    createPath(game, a);
                }
                if (!a.isMoving()) {
                    nextMove(game, a);
                } //else {
                game.move();
                _ar.update(game);
                //}

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

    private static void nextMove(game_service game, Agent a) {
        int id = a.getId();
        int next_dest = a.get_path().get(0).getKey();
        a.get_path().remove(0);

        long is_choosen = game.chooseNextEdge(id, next_dest);
        System.out.println("**" + is_choosen);
        System.out.println("Agent: " + id + ", val: " + a.getValue() + "   turned to node: " + next_dest +
                "   on the way to: " + a.get_curr_fruit());
    }

    public static void initArena(game_service game) {
        _ar = new Arena(game);
        _ar.updateGraph(game.toString());
        _graph = _ar.get_graph();
        JsonObject jo = JsonParser.parseString(game.toString()).getAsJsonObject().getAsJsonObject("GameServer");
        placeAgents(jo.get("agents").getAsInt(), game);
        _ar.update(game);
    }

    private static void initGUI(int scenario_num) {
        _win = new GameGUI(scenario_num);
        _win.set_ar(_ar);
        _win.setVisible(true);
    }

    private static void placeAgents(int numOfAgents, game_service game) {
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
        int div = pq.size() / numOfAgents;
        for (int i = 0; i < numOfAgents; i++) {
            game.addAgent(pq.peek().getKey());
            for (int j = 0; j < div; j++) {
                pq.poll();
            }
        }
    }


    private static void createPath(game_service game, Agent a) {
        List<Pokemon> pokemons_list = _ar.getPokemons();
        dw_graph_algorithms ga = new WDGraph_Algo();
        ga.init(_graph);

        Pokemon min_pokemon = pokemons_list.get(0);
        int n = min_pokemon.get_edge().getSrc();
        double shortest_way = ga.shortestPathDist(a.getSrcNode(), n);

        for (Pokemon p : _ar.getPokemons()) {
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
        List<node_data> path = ga.shortestPath(a.getSrcNode(), min_pokemon.get_edge().getSrc());
        path.add(_graph.getNode(min_pokemon.get_edge().getDest()));
        path.remove(0);
        a.set_path(path);

        a.set_curr_fruit(min_pokemon);
        _ar.get_pokemonsWithOwner().add(min_pokemon);
    }
}
