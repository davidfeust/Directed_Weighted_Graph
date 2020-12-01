package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import okhttp3.internal.concurrent.Task;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Ex2 {

    private static GameGUI _win;
    private static Arena _ar;
    private static directed_weighted_graph _graph;

    public static void main(String[] args) {


        int id; //= Integer.parseInt(args[0]);
//        int num = Integer.parseInt(args[0]);

        final int scenario_num = 22;
        game_service game = Game_Server_Ex2.getServer(scenario_num); // you have [0,23] games
        System.out.println(game.toString());
//        id = 314699059;
//        game.login(id);
        initArena(game);
        initGUI(scenario_num);

        game.startGame();
        int i = 0;

        ArrayList<MyThread> agents_mov = new ArrayList<>();
        for (CL_Agent a : _ar.getAgents()) {
//            MyThread t = new MyThread(game,a);
            agents_mov.add(new MyThread(game, a));
        }

        while (game.isRunning()) {
        for (MyThread t : agents_mov) {
            t.run();
      }
        }
//        while (i>0){
//            agents_mov[--i].start();
//        }
//        while (game.isRunning()) {
//            for (Thread tmp_run : agents_mov) {
//                if (!tmp_run.isAlive())
//                    tmp_run.start();
//            }

        try {
            _win.repaint();
            Thread.sleep(100);
        } catch (Exception e) {
            e.printStackTrace();
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

//    private static void moveAgant(game_service game, CL_Agent a) {
//        updateArena(game);
//
//        List<CL_Pokemon> pokemons_list = _ar.getPokemons();
//        dw_graph_algorithms ga = new WDGraph_Algo();
//        ga.init(_graph);
//
//        CL_Pokemon minPokemon = pokemons_list.get(0);
//        int n = minPokemon.get_edge().getSrc();
//        double shortest_way = ga.shortestPathDist(a.getSrcNode(), n);
//        for (CL_Pokemon p : _ar.getPokemons()) {
//            edge_data pokemon_edge = p.get_edge();
//            int s = pokemon_edge.getSrc();
//            double dist_src = ga.shortestPathDist(a.getSrcNode(), s);
//            if (dist_src < shortest_way) {
//                shortest_way = dist_src;
//                minPokemon = p;
//                n = s;
//            }
//        }
//        List<node_data> path = ga.shortestPath(a.getSrcNode(), minPokemon.get_edge().getSrc());
//        path.add(_graph.getNode(minPokemon.get_edge().getDest()));
//        System.out.println(path);
////            System.out.println(path);
//        int dest;
//        if (path.size() > 1)
//            dest = path.get(1).getKey();
//        else
//            dest = path.get(0).getKey();
//        a.set_curr_fruit(minPokemon);
////                a.setCurrNode();
//        a.setNextNode(dest);
//        game.chooseNextEdge(a.getID(), dest);
//        System.out.println("Agent: " + a.getID() + ", val: " + a.getValue() + "   turned to node: " + dest);
//}

    private static void updateArena(game_service game) {
        List<CL_Agent> log = Arena.getAgents(game.move(), _graph);
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


    public static class MyThread extends Thread {
        private game_service _game;
        private CL_Agent _ag;
//        private CL_Pokemon minPokemon;
        long _flag = 0L;

        public MyThread(game_service game, CL_Agent a) {
            _game = game;
            _ag = a;
        }

        @Override
        public void run() {
//            while (this._game.isRunning()) {
                try {
                    updateArena(_game);
                    if (_ag._curr_pathQ == null || _ag._curr_pathQ.isEmpty()) {
                        List<CL_Pokemon> pokemons_list = _ar.getPokemons();
                        dw_graph_algorithms ga = new WDGraph_Algo();
                        ga.init(_graph);

                        _ag.minPokemon = pokemons_list.get(0);
                        ga.shortestPathDist(_ag.getSrcNode(), _ag.getSrcNode());
                        System.out.println(_graph.getNode(_ag.getSrcNode()).getWeight());
                        double node_wight = _graph.getNode(_ag.minPokemon.get_edge().getSrc()).getWeight();
                        for (CL_Pokemon p : _ar.getPokemons()) {
                            if (_graph.getNode(p.get_edge().getSrc()).getWeight() < node_wight) {
                                node_wight = _graph.getNode(p.get_edge().getSrc()).getWeight();
                                _ag.minPokemon = p;
                            }
                        }
                        List<node_data> path = ga.shortestPath(_ag.getSrcNode(), _ag.minPokemon.get_edge().getSrc());
                        path.add(_graph.getNode(_ag.minPokemon.get_edge().getDest()));
                        _ag._curr_pathQ = new LinkedList<>(path);
                        System.out.println(path);
//            System.out.println(path);
//            _ag.set_curr_path(path);
//        }

//
//                List<node_data> path = _ag.get_curr_path();

                    }
//       int dest=0;
//                System.out.println(dest);
//       if(dest==_ag._curr_pathQ.peek().getKey()){}
                    int dest = _ag._curr_pathQ.peek().getKey();

                    if (this._flag == -1)
                        dest = _ag._curr_pathQ.poll().getKey();


                    _ag.set_curr_fruit(_ag.minPokemon);
//                a.setCurrNode();
                    _ag.setNextNode(dest);
                    this._flag = _game.chooseNextEdge(_ag.getID(), dest);
                    System.out.println("Agent: " + _ag.getID() + ", val: " + _ag.getValue() + "   turned to node: " + dest);

                    Thread.sleep(1);
                } catch (Exception e) {
                    System.out.println("Exception occur");
//                }
            }
        }
    }
}
