package game;

import api.*;
import game.util.*;
import com.google.gson.JsonObject;

import java.util.Objects;

import static gameClient.Arena.isOnEdge;

public class Pokemon {

    private static final double EPS = 0.001 * 0.001;
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
//        for (node_data n : _graph.getV()) {
//            geo_location n_pos = n.getLocation();
//            for (edge_data e : _graph.getE(n.getKey())) {
//                geo_location dest_pos = _graph.getNode(e.getDest()).getLocation();
//                if (_type == 1 && e.getSrc() > e.getDest()) {
//                    if (Math.abs(n_pos.distance(dest_pos) - (n_pos.distance(_pos) + _pos.distance(dest_pos))) <= EPS) {
//                        return e;
//                    }
//                }
//                if (_type == -1 && e.getSrc() < e.getDest()) {
//                    if (Math.abs(n_pos.distance(dest_pos) - (n_pos.distance(_pos) + _pos.distance(dest_pos))) <= EPS) {
//                        return e;
//                    }
//                }
//            }
//        }
//        return null;

        for (node_data v : _graph.getV()) {
            for (edge_data e : _graph.getE(v.getKey())) {
                boolean f = isOnEdge(get_pos(), e, get_type(), _graph);
                if (f) {
                    return e;
                }
            }
        }
        return null;
    }

    public static boolean isOnEdge(geo_location p, geo_location src, geo_location dest) {
        boolean ans = false;
        double dist = src.distance(dest);
        double d1 = src.distance(p) + p.distance(dest);
        if (dist > d1 - EPS) {
            ans = true;
        }
        return ans;
    }

    private static boolean isOnEdge(geo_location p, int s, int d, directed_weighted_graph g) {
        geo_location src = g.getNode(s).getLocation();
        geo_location dest = g.getNode(d).getLocation();
        return isOnEdge(p, src, dest);
    }

    private static boolean isOnEdge(geo_location p, edge_data e, int type, directed_weighted_graph g) {
//        int src = g.getNode(e.getSrc()).getKey();
//        int dest = g.getNode(e.getDest()).getKey();
        int src = e.getSrc();
        int dest = e.getDest();
        if (type < 0 && dest > src) {
            return false;
        }
        if (type > 0 && src > dest) {
            return false;
        }
        return isOnEdge(p, src, dest, g);
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
                '}' + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pokemon pokemon = (Pokemon) o;
        return Double.compare(pokemon._value, _value) == 0 && _type == pokemon._type && _pos.equals(pokemon._pos) && _edge.equals(pokemon._edge);
    }

}
