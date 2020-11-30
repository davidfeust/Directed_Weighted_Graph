package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SimpleTimeZone;

public class Ex2 {

    private static GameGUI _win;
    private static Arena _ar;
    private static directed_weighted_graph _graph;

    public static void main(String[] args) {
//        int id = Integer.parseInt(args[0]);
//        int num = Integer.parseInt(args[0]);

        final int scenario_num = 1;
        game_service game = Game_Server_Ex2.getServer(scenario_num); // you have [0,23] games
        System.out.println(game.toString());

//        game.login(id);
        initArena(game);
        initGUI(scenario_num);

        game.startGame();

        findEdges();
        System.out.println(game.getPokemons());
        for (CL_Pokemon p : _ar.getPokemons()) {
            System.out.println(p);
        }

        while (game.isRunning()) {
            moveAgants(game);
            try {
                _win.repaint();
                Thread.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//
    }

    private static void initGUI(int scenario_num) {
        _win = new GameGUI(scenario_num);
//        _win.setSize(1000, 700);
        _win.set_ar(_ar);
        _win.show();
    }

    public static void initArena(game_service game) {
        try {
            JSONObject jo = new JSONObject(game.toString()).getJSONObject("GameServer");
            String graph_path = jo.getString("graph");
            dw_graph_algorithms ga = new WDGraph_Algo();
            ga.load(graph_path);
            directed_weighted_graph graph = ga.getGraph();
            _graph = graph;
            _ar = new Arena();
            _ar.setGraph(graph);
            _ar.setPokemons(Arena.json2Pokemons(game.getPokemons()));
            _ar.setAgents(cerateAgents(jo.getInt("agents"), game));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static List<CL_Agent> cerateAgents(int numOfAgents, game_service game) {
        List<CL_Agent> agents_list = new ArrayList<>();
        for (int i = 0; i < numOfAgents; i++) {
            CL_Agent agents = new CL_Agent(_graph, i, 0);
            agents_list.add(agents);
            game.addAgent(i);
        }
        return agents_list;
    }

    public static directed_weighted_graph json2Graph(String graph_json) {
        dw_graph_algorithms ga = new WDGraph_Algo();
        File f = new File("temp graph");
        try (FileWriter fw = new FileWriter(f)) {
            fw.write(graph_json);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ga.load("temp graph");
        return ga.getGraph();
    }

    private static void moveAgants(game_service game) {
//        findEdges();
        List<CL_Pokemon> pokemons_list = _ar.getPokemons();
        dw_graph_algorithms ga = new WDGraph_Algo();
        ga.init(_graph);
        //change and  do the shortest path dist  one time and chek the wight for the closest one
        for (CL_Agent a : _ar.getAgents()) {
            CL_Pokemon shortest_pok = pokemons_list.get(0);
            int n = shortest_pok.get_edge().getSrc();
            double shortest_way = ga.shortestPathDist(a.getSrcNode(), n);
            for (CL_Pokemon p : _ar.getPokemons()) {
                edge_data pokemon_edge = p.get_edge();
                int s = pokemon_edge.getSrc();
                int d = pokemon_edge.getDest();
                double dist_src = ga.shortestPathDist(a.getSrcNode(), s);
                if (dist_src < shortest_way) {
                    shortest_way = dist_src;
                    shortest_pok = p;
                    n = s;
                }
                List<node_data> path = ga.shortestPath(a.getSrcNode(), n);
                System.out.println(path);
                int dest = path.get(1).getKey();
                a.set_curr_fruit(shortest_pok);
                a.setNextNode(dest);
                System.out.println(a.getID() + "****");
                System.out.println(game.chooseNextEdge(a.getID(), dest));
                System.out.println("Agent: " + a.getID() + ", val: " + a.getValue() + "   turned to node: " + dest);
            }

        }
    }

    public static void findEdges() {
        for (CL_Pokemon p : _ar.getPokemons()) {
            geo_location pok_pos = p.getLocation();
//            System.out.println(p.getLocation());
            for (node_data n : _graph.getV()) {
                geo_location n_pos = n.getLocation();
                for (edge_data e : _graph.getE(n.getKey())) {
                    geo_location dest_pos = _graph.getNode(e.getDest()).getLocation();
                    if (Math.abs(n_pos.distance(dest_pos) - (n_pos.distance(pok_pos) + pok_pos.distance(dest_pos))) <= 0.0001){
                        p.set_edge(e);
                        break;
                    }
                }
            }
        }
    }
}
