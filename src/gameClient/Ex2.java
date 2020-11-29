package gameClient;


import Server.Game_Server_Ex2;
import api.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Ex2 {

    public static void main(String[] args) {
        final int scenario_num = 20;
        game_service game = Game_Server_Ex2.getServer(scenario_num); // you have [0,23] games
        game.startGame();
//        System.out.println(game.getGraph());
        System.out.println(game);
        //gg.init(g);
        directed_weighted_graph graph = json2Graph(game.getGraph());
        Arena _ar = new Arena();
        _ar.setGraph(graph);
        _ar.setPokemons(Arena.json2Pokemons(game.getPokemons()));
        _ar.setAgents(cerateAgents(game.toString(), graph));
        GameGUI _win = new GameGUI(scenario_num);
        _win.setSize(1000, 700);
        _win.set_ar(_ar);
        _win.show();
//
    }

    private static List<CL_Agent> cerateAgents(String game_json, directed_weighted_graph g) {
        try {
            int numOfAgents = new JSONObject(game_json).getJSONObject("GameServer").getInt("agents");
//            System.out.println(numOfAgents);
            List<CL_Agent> agents_list = new ArrayList<>();
            for (int i = 0; i < numOfAgents; i++) {
                agents_list.add(new CL_Agent(g, 0));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
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

}
