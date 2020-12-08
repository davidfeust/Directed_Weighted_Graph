package game;

import api.*;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class Algo {

    private static Arena _ar;
    private static directed_weighted_graph _graph;

    static void placeAgents(int num_of_agents, game_service game) {
//        dw_graph_algorithms ga = new WDGraph_Algo(_graph);
//        ga.shortestPathDist(0, 0);
//
////        by distance :
//        PriorityQueue<node_data> pq = new PriorityQueue<>(new Comparator<node_data>() {
//            @Override
//            public int compare(node_data o1, node_data o2) {
//                return Double.compare(o1.getWeight(), o2.getWeight());
//            }
//        });
//
//        for (Pokemon i : _ar.getPokemons()) {
//            pq.add(_graph.getNode(i.get_edge().getSrc()));
//        }
//        int div = pq.size() / num_of_agents;
//        for (int i = 0; i < num_of_agents; i++) {
//            game.addAgent(pq.peek().getKey());
//            for (int j = 0; j < div; j++) {
//                System.out.println(pq.peek());
//                pq.poll();
//            }
//        }
//        for(; !pq.isEmpty();) {
//            System.out.println(pq.poll());
//        }
//    }


//        by value :
        PriorityQueue<Pokemon> pq = new PriorityQueue<>(new Comparator<Pokemon>() {
            @Override
            public int compare(Pokemon o1, Pokemon o2) {
                return Double.compare(o2.get_value(), o1.get_value());
            }
        });

        pq.addAll(_ar.getPokemons());

        for (int i = 0; i < num_of_agents; i++) {
            game.addAgent(pq.poll().get_edge().getSrc());
        }

    }

    static void nextMove(game_service game, Agent a) {
        int id = a.getId();
        if (indexOfPok(_ar.getPokemons(), a.get_curr_fruit()) == -1) {
            createPath(game, a);
            return;
        }

        int next_dest = a.get_path().get(0).getKey();
        a.get_path().remove(0);
        if (a.get_path().isEmpty()) {
            _ar.get_pokemonsWithOwner().remove(a.get_curr_fruit());
        }

        long is_choosen = game.chooseNextEdge(id, next_dest);
//        System.out.println("Agent: " + id + ", val: " + a.getValue() + "   turned to node: " + next_dest);
//        System.out.println("\t\ton the way to: " + a.get_curr_fruit());
//        System.out.println("**" + is_choosen);
//        System.out.println("&& " + game.getPokemons());
    }


    synchronized static void createPath(game_service game, Agent a) {
        //by value diveded by distance
        {
            dw_graph_algorithms ga = new WDGraph_Algo();
            ga.init(_graph);

            ga.shortestPathDist(a.getSrcNode(), a.getSrcNode());
            Pokemon min_pokemon = _ar.getPokemons().get(0);
            double shortest_way = _graph.getNode(min_pokemon.get_edge().getSrc()).getWeight();
            if (shortest_way == 0) {
                shortest_way = 0.000001;
            }
            double max_ValDivDist = min_pokemon.get_value() / shortest_way;
            for (Pokemon p : _ar.getPokemons()) {
                if (indexOfPok(_ar.get_pokemonsWithOwner(), p) == -1) {
                    double p_src_weight = _graph.getNode(p.get_edge().getSrc()).getWeight();
                    if (p_src_weight == 0) {
                        p_src_weight = 0.000001;
                    }
                    double temp = p.get_value() / p_src_weight;
                    if (max_ValDivDist < temp) {
                        max_ValDivDist = temp;
                        min_pokemon = p;
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

    //by value - seem like it dose not give the best result
//    {
//        dw_graph_algorithms ga = new WDGraph_Algo();
//        ga.init(_graph);
//
//        Pokemon min_pokemon = _ar.getPokemons().get(0);
//
//        for (Pokemon p : _ar.getPokemons()) {
//            if (indexOfPok(_ar.get_pokemonsWithOwner(), p) == -1) {
//
//                if (min_pokemon.get_value() < p.get_value()) {
//                    min_pokemon = p;
//                }
//            }
//        }
//        List<node_data> path = ga.shortestPath(a.getSrcNode(), min_pokemon.get_edge().getSrc());
//        path.add(_graph.getNode(min_pokemon.get_edge().getDest()));
//        path.remove(0);
//        a.set_path(path);
//
//        a.set_curr_fruit(min_pokemon);
//        _ar.get_pokemonsWithOwner().add(min_pokemon);
//    }
//
//    }
    //by distance
//    {
//        dw_graph_algorithms ga = new WDGraph_Algo();
//        ga.init(_graph);
//
//        Pokemon min_pokemon = _ar.getPokemons().get(0);
//        int n = min_pokemon.get_edge().getSrc();
//        double shortest_way = ga.shortestPathDist(a.getSrcNode(), n);
//
//        for (Pokemon p : _ar.getPokemons()) {
//            if (indexOfPok(_ar.get_pokemonsWithOwner(), p) == -1) {
//                edge_data pokemon_edge = p.get_edge();
//                int s = pokemon_edge.getSrc();
//                double dist_src = ga.shortestPathDist(a.getSrcNode(), s);
//                if (dist_src < shortest_way) {
//                    shortest_way = dist_src;
//                    min_pokemon = p;
//                    n = s;
//                }
//            }
//        }
//        List<node_data> path = ga.shortestPath(a.getSrcNode(), min_pokemon.get_edge().getSrc());
//        path.add(_graph.getNode(min_pokemon.get_edge().getDest()));
//        path.remove(0);
//        a.set_path(path);
//
//        a.set_curr_fruit(min_pokemon);
//        _ar.get_pokemonsWithOwner().add(min_pokemon);
//    }
//}
    public static int indexOfPok(List<Pokemon> arr, Pokemon pok) {
        int ans = -1;
        for (int i = 0; i < arr.size(); i++) {
            if (arr.get(i).equals(pok)) {
                ans = i;
                break;
            }
        }
        return ans;
    }

    public static boolean isClose2Pok(Agent ag) {
        for (Pokemon i : _ar.getPokemons()) {
            if (Math.abs(ag.getPos().distance(i.get_pos())) < 0.001) {
                return true;
            }
        }
        return false;
    }

    public static void set_ar(Arena _ar) {
        Algo._ar = _ar;
    }

    public static void set_graph(directed_weighted_graph _graph) {
        Algo._graph = _graph;
    }
}
