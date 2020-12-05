package game;

import api.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import game.util.*;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Arena {

    private directed_weighted_graph _graph;
    private List<Agent> _agents;
    private List<Pokemon> _pokemons;
    private List<Pokemon> _pokemonsWithOwner;
    private long _time;
    private double _time_to_end;
    private long _all_time;
    private boolean _start = false;

    public Arena(game_service game) {
        updateGraph(game.toString());
        Agent.set_graph(_graph);
        _agents = new ArrayList<>();
        Pokemon.set_graph(_graph);
        _pokemons = new ArrayList<>();
        updatePokemons(game.getPokemons());
        _pokemonsWithOwner = new ArrayList<>();
    }

    public void update(game_service game) {
        updateGraph(game.toString());
        Agent.set_graph(_graph);
        _agents = new ArrayList<>();
        updateAgents(game.getAgents());
        Pokemon.set_graph(_graph);
        _pokemons = new ArrayList<>();
        updatePokemons(game.getPokemons());
        _time = game.timeToEnd();
//        _time_to_end = (double) _time / _all_time;
//        if (!_start) {
//            _all_time = _time;
//            _start = true;
//        }
    }

    private void updatePokemons(String json) {
        JsonObject json_obj = JsonParser.parseString(json).getAsJsonObject();
        JsonArray pokemons_arr = json_obj.getAsJsonArray("Pokemons");
        for (JsonElement i : pokemons_arr) {
            JsonObject pok = i.getAsJsonObject().get("Pokemon").getAsJsonObject();
            Point3D pos = new Point3D(pok.get("pos").getAsString());
            Pokemon update_pok = null;
            for (Pokemon j : _pokemons) {
                if (pos.equals(j.get_pos())) {
                    update_pok = j;
                }
            }
            if (update_pok == null) {
                update_pok = new Pokemon(pok);
                _pokemons.add(update_pok);
            } else {
                update_pok.update(pok);
            }
        }
    }

    private void updateAgents(String json) {
        JsonObject json_obj = JsonParser.parseString(json).getAsJsonObject();
        JsonArray agents_arr = json_obj.getAsJsonArray("Agents");
        for (JsonElement i : agents_arr) {
            JsonObject agent = i.getAsJsonObject().get("Agent").getAsJsonObject();
            int id = agent.get("id").getAsInt();
            Agent update_agent = null;
            for (Agent j : _agents) {
                if (j.getId() == id) {
                    update_agent = j;
                }
            }
            if (update_agent == null) {
                update_agent = new Agent(agent);
                _agents.add(update_agent);
            } else {
                update_agent.update(agent);
            }
        }
    }

    public void updateGraph(String json) {
        JsonObject jo = JsonParser.parseString(json).getAsJsonObject().getAsJsonObject("GameServer");
        String graph_path = jo.get("graph").getAsString();
        dw_graph_algorithms ga = new WDGraph_Algo();
        ga.load(graph_path);
        directed_weighted_graph graph = ga.getGraph();
        _graph = graph;
    }

    public directed_weighted_graph getGgraph() {
        return _graph;
    }

    public List<Agent> getAgents() {
        return _agents;
    }

    public List<Pokemon> getPokemons() {
        return _pokemons;
    }

    public List<Pokemon> get_pokemonsWithOwner() {
        return _pokemonsWithOwner;
    }

    public directed_weighted_graph get_graph() {
        return _graph;
    }

    private static Range2D GraphRange(directed_weighted_graph g) {
        Iterator<node_data> itr = g.getV().iterator();
        double x0 = 0, x1 = 0, y0 = 0, y1 = 0;
        boolean first = true;
        while (itr.hasNext()) {
            geo_location p = itr.next().getLocation();
            if (first) {
                x0 = p.x();
                x1 = x0;
                y0 = p.y();
                y1 = y0;
                first = false;
            } else {
                if (p.x() < x0) {
                    x0 = p.x();
                }
                if (p.x() > x1) {
                    x1 = p.x();
                }
                if (p.y() < y0) {
                    y0 = p.y();
                }
                if (p.y() > y1) {
                    y1 = p.y();
                }
            }
        }
        Range xr = new Range(x0, x1);
        Range yr = new Range(y0, y1);
        return new Range2D(xr, yr);
    }

    public static Range2Range w2f(directed_weighted_graph g, Range2D frame) {
        Range2D world = GraphRange(g);
        Range2Range ans = new Range2Range(world, frame);
        return ans;
    }

    public long getTime() {
        return _time / 1000;
    }
}