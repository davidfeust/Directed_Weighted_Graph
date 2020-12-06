package game;

import api.*;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class Algo {

    private static Arena _ar;
    private static directed_weighted_graph _graph;

    static void placeAgents(int num_of_agents, game_service game) {
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

    static void nextMove(game_service game, Agent a) {
        int id = a.getId();
        if (!_ar.getPokemons().contains(a.get_curr_fruit())) {
            createPath(game, a);
            System.out.println("&&&&&&&&&&&&&&&&&&&&&7");
            return;
        }

        int next_dest = a.get_path().get(0).getKey();
        a.get_path().remove(0);
        if (a.get_path().isEmpty()) {
            _ar.get_pokemonsWithOwner().remove(a.get_curr_fruit());
        }

        long is_choosen = game.chooseNextEdge(id, next_dest);
        System.out.println("Agent: " + id + ", val: " + a.getValue() + "   turned to node: " + next_dest);
        System.out.println("\t\ton the way to: " + a.get_curr_fruit());
//        System.out.println("**" + is_choosen);
    }


    synchronized static void createPath(game_service game, Agent a) {
        dw_graph_algorithms ga = new WDGraph_Algo();
        ga.init(_graph);

        Pokemon min_pokemon = _ar.getPokemons().get(0);
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


    public static void set_ar(Arena _ar) {
        Algo._ar = _ar;
    }

    public static void set_graph(directed_weighted_graph _graph) {
        Algo._graph = _graph;
    }
}
