package game;

import api.*;
import game.util.*;
import com.google.gson.JsonObject;

public class Pokemon {

    private double _value;
    private int _type;
    private Point3D _pos;
    private edge_data _edge;
    static private directed_weighted_graph _graph;

    public Pokemon(JsonObject json) {
        update(json);
    }

    public void update(JsonObject json) {
        _value = json.get("value").getAsDouble();
        _type = json.get("type").getAsInt();
        _pos = new Point3D(json.get("pos").getAsString());
        _edge = findEdge();
    }

    public edge_data findEdge() {
        for (node_data n : _graph.getV()) {
            geo_location n_pos = n.getLocation();
            for (edge_data e : _graph.getE(n.getKey())) {
                geo_location dest_pos = _graph.getNode(e.getDest()).getLocation();
                if (_type == -1 && e.getSrc() < e.getDest()) {
                    if (Math.abs(n_pos.distance(dest_pos) - (n_pos.distance(_pos) + _pos.distance(dest_pos))) <= 0.0001) {
                        return e;
                    }
                } else if (_type == 1 && e.getSrc() > e.getDest()) {
                    if (Math.abs(n_pos.distance(dest_pos) - (n_pos.distance(_pos) + _pos.distance(dest_pos))) <= 0.0001) {
                        return e;
                    }
                }
            }
        }
        return null;
    }

    public static void set_graph(directed_weighted_graph _graph) {
        Pokemon._graph = _graph;
    }

    public double get_value() {
        return _value;
    }

    public int get_type() {
        return _type;
    }

    public Point3D get_pos() {
        return _pos;
    }

    public edge_data get_edge() {
        return _edge;
    }

    @Override
    public String toString() {
        return "Pokemon{" +
                "_value=" + _value +
                ", _type=" + _type +
                ", _pos=" + _pos +
                ", _edge=" + _edge +
                '}';
    }
}
