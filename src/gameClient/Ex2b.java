package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Ex2b {

    private static GameGUI _win;
    private static Arena _ar;
    private static directed_weighted_graph _graph;

    static int _counter = 0;

    public static void main(String[] args) {
        int id; //= Integer.parseInt(args[0]);
//        int num = Integer.parseInt(args[0]);

        final int scenario_num = 23;
        game_service game = Game_Server_Ex2.getServer(scenario_num); // you have [0,23] games
        System.out.println(game.toString());
//        id = 314699059;
//        game.login(id);
        initArena(game);
        initGUI(scenario_num);

        game.startGame();

        while (game.isRunning()) {
            for (CL_Agent a : _ar.getAgents()) {
                if (a.get_curr_path() == null || a.get_curr_path().isEmpty()) {
                    createPath(game, a);
                }
//                if (!isMoving(game, a.getID())) {
//                    if (!a.isMoving()) {

//                    if (_ar.isOnEdge(a.getLocation(), a.get_curr_path().get(0).getLocation(),a.get_curr_path().get(0).getLocation())){
                        a.get_curr_path().remove(0);
                    moveAgant(game, a);
//                } else {
//                    updateArena(game);
//                }
            }
            try {
                _win.repaint();
                Thread.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String res = game.toString();

        System.out.println(res);
        System.exit(0);
//
    }

    private static void initGUI(int scenario_num) {
        _win = new GameGUI(scenario_num);
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
//            for (CL_Pokemon p : _ar.getPokemons()) {
//                Arena.updateEdge(p, _graph);
//            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private static List<CL_Agent> cerateAgents(int numOfAgents, game_service game) {
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
        int div = 1;
        div = pq.size() / numOfAgents;
        List<CL_Agent> agents_list = new ArrayList<>();
        for (int i = 0; i < numOfAgents; i++) {
            CL_Agent agents = new CL_Agent(_graph, pq.peek().getKey(), i);
            agents_list.add(agents);
            game.addAgent(pq.peek().getKey());
            for (int j = 0; j < div; j++) {
                pq.poll();
            }
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

    private static void moveAgant(game_service game, CL_Agent a) {
        int dest;
        dest = a.get_curr_path().get(0).getKey();
//        a.get_curr_path();
        a.setCurrNode(dest);
        if (a.get_curr_path().size() == 1) {
            _ar.remove_pokemonsWithOwner(a.minPokemon);
        }
        a.setNextNode(dest);
        long is_choosen = game.chooseNextEdge(a.getID(), dest);
        System.out.println("**" + is_choosen);
        System.out.println(game.getAgents());
//        if (is_choosen != -1) {
            game.move();
//        } else {
//        }
        updateArena(game);
        System.out.println("Agent: " + a.getID() + ", val: " + a.getValue() + "   turned to node: " + dest);
    }

    private static void createPath(game_service game, CL_Agent a) {
        updateArena(game);
        List<CL_Pokemon> pokemons_list = _ar.getPokemons();
        dw_graph_algorithms ga = new WDGraph_Algo();
        ga.init(_graph);
        //change and  do the shortest path dist  one time and chek the wight for the closest one

//            ga.shortestPathDist(a.getSrcNode(), a.getSrcNode());
//            double minWight = _graph.getNode(pokemons_list.get(0).get_edge().getSrc()).getWeight();
//            CL_Pokemon minPokemon = pokemons_list.get(0);
//            for (CL_Pokemon p : _ar.getPokemons()) {
//                if(_graph.getNode(p.get_edge().getSrc()).getWeight()<minWight){
//                    minPokemon = p;
//                    minWight = _graph.getNode(p.get_edge().getSrc()).getWeight();
//                }
//            }
        CL_Pokemon minPokemon = pokemons_list.get(0);
        int n = minPokemon.get_edge().getSrc();
        double shortest_way = ga.shortestPathDist(a.getSrcNode(), n);
        for (CL_Pokemon p : _ar.getPokemons()) {
            if (!_ar.get_pokemonsWithOwner().contains(p)) {
                edge_data pokemon_edge = p.get_edge();
                int s = pokemon_edge.getSrc();
                double dist_src = ga.shortestPathDist(a.getSrcNode(), s);
                if (dist_src < shortest_way) {
                    shortest_way = dist_src;
                    minPokemon = p;
                    n = s;
                }
            }
        }
        List<node_data> path = ga.shortestPath(a.getSrcNode(), minPokemon.get_edge().getSrc());
        path.add(_graph.getNode(minPokemon.get_edge().getDest()));
        a.set_curr_path(path);
        a.set_curr_fruit(minPokemon);
        _ar.add_pokemonsWithOwner(minPokemon);
//        System.out.println(path);
    }

    private static void updateArena(game_service game) {
        List<CL_Agent> log = Arena.getAgents(game.getAgents(), _graph);
        System.out.println("$$" + _counter++);
        List<CL_Pokemon> ffs = Arena.json2Pokemons(game.getPokemons());
        _ar.setPokemons(ffs);
        findEdges();
        _ar.setAgents(log);
    }

    public static void findEdges() {
        for (CL_Pokemon p : _ar.getPokemons()) {
            geo_location pok_pos = p.getLocation();
//            System.out.println(p.getLocation());
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

    public static boolean isMoving(game_service game, int agent_id) {
        String json_str = game.getAgents();

        JsonObject json_obj;
        json_obj = JsonParser.parseString(json_str).getAsJsonObject();
        JsonArray arr = json_obj.getAsJsonArray("Agents");
        for (JsonElement i : arr) {
            int id = i.getAsJsonObject().get("Agent").getAsJsonObject().get("id").getAsInt();
            System.out.println("id =" + id);
            if (id == agent_id) {
                int dest = i.getAsJsonObject().get("Agent").getAsJsonObject().get("dest").getAsInt();
                System.out.println("dest= " + dest);
                if (dest == -1) {
                    return false;
                }
            }
        }
        return true;
    }
}