package gameClient;


import Server.Game_Server_Ex2;
import api.*;
import com.google.gson.JsonObject;
import netscape.javascript.JSObject;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Ex2 {

    private static GameGUI _win;
    private static Arena _ar;
    private static directed_weighted_graph _graph;

    public static void main(String[] args) {
        final int scenario_num = 1;
        game_service game = Game_Server_Ex2.getServer(scenario_num); // you have [0,23] games
        System.out.println(game.getPokemons());
        initArena(game);
        initGUI(scenario_num);

        game.addAgent(0);
        System.out.println(game.startGame());

        while (game.isRunning()) {
            moveAgants(game, _graph);
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
        _win.setSize(1000, 700);
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
            _ar.setAgents(cerateAgents(jo.getInt("agents"), graph));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static List<CL_Agent> cerateAgents(int numOfAgents, directed_weighted_graph g) {
        List<CL_Agent> agents_list = new ArrayList<>();
        for (int i = 0; i < numOfAgents; i++) {
            agents_list.add(new CL_Agent(g, i));
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

    private static void moveAgants(game_service game, directed_weighted_graph gg) {
        List<CL_Pokemon> pokemons_list = Arena.json2Pokemons(game.getPokemons());
        dw_graph_algorithms ga = new WDGraph_Algo();
        ga.init(gg);
//        HashMap<Integer, Double> len_to_pokemon = new HashMap<>();
        for (CL_Agent a : _ar.getAgents()) {
            CL_Pokemon shortest_pok = pokemons_list.get(0);
//            shortest_pok.getLocation();
            int n = shortest_pok.get_edge().getSrc();
            double shortest_way = ga.shortestPathDist(a.getSrcNode(), n);
            for (CL_Pokemon p : _ar.getPokemons()) {
                edge_data pokemon_edge = p.get_edge();
                int s = pokemon_edge.getSrc();
                int d = pokemon_edge.getDest();
                double dist_src = ga.shortestPathDist(a.getSrcNode(), s);
                double dist_dest = ga.shortestPathDist(a.getSrcNode(), d);
                if (dist_src < shortest_way) {
                    shortest_way = dist_src;
                    shortest_pok = p;
                    n = s;
                } else if (dist_dest < shortest_way) {
                    shortest_way = dist_dest;
                    shortest_pok = p;
                    n = d;
                }
            }
            List<node_data> path = ga.shortestPath(a.getSrcNode(), n);
            Iterator<node_data> it = path.iterator();
            it.next();
            int dest = it.next().getKey();
            game.chooseNextEdge(a.getID(), dest);
            System.out.println("Agent: " + a.getID() + ", val: " + a.getValue() + "   turned to node: " + dest);
        }


    }
}
