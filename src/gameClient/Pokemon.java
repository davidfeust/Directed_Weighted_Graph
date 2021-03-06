package gameClient;

import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;
import com.google.gson.JsonObject;
import gameClient.util.Point3D;

/**
 * This class is a representation of Pokemon in the Pokemons Game.
 * most of the data are coming from the server as Json,
 * and there data that coming from the client, like _edge and graph.
 */
public class Pokemon {

    private static final double EPS = 0.001 * 0.001;
    private double _value;
    private int _type;
    private Point3D _pos;
    private edge_data _edge;
    static private directed_weighted_graph _graph;

    /**
     * Constructor. uses update method.
     *
     * @param json json object, coming from the server.
     */
    public Pokemon(JsonObject json) {
        update(json);
    }

    /**
     * update the field by the json
     *
     * @param json json object, coming from the server.
     */
    public void update(JsonObject json) {
        _value = json.get("value").getAsDouble();
        _type = json.get("type").getAsInt();
        _pos = new Point3D(json.get("pos").getAsString());
        _edge = findEdge();
    }

    /**
     * Find the edge that this Pokemon on it. uses inOnEdge method and
     * check it on all the node in the _graph.
     *
     * @return edge_data on which the pokemon found
     */
    public edge_data findEdge() {
        for (node_data v : _graph.getV()) {
            for (edge_data e : _graph.getE(v.getKey())) {
                if (isOnEdge(e)) {
                    return e;
                }
            }
        }
        return null;
    }

    /**
     * Returns true iff this Pokemon location is on the giving edge.
     * the type field says if the pokemon on edge that src < dest -> -1 type,
     * or src > dest -> 1 type.
     *
     * @param e edge_data
     * @return true if this is on e.
     */
    private boolean isOnEdge(edge_data e) {
        int src = e.getSrc();
        int dest = e.getDest();
        if (_type < 0 && dest > src) {
            return false;
        }
        if (_type > 0 && src > dest) {
            return false;
        }
        geo_location src_loc = _graph.getNode(src).getLocation();
        geo_location dest_loc = _graph.getNode(dest).getLocation();
        double dist = src_loc.distance(dest_loc);
        double d1 = src_loc.distance(get_pos()) + get_pos().distance(dest_loc);
        return dist > d1 - EPS;
    }

    // Getters & Setters:

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

    /**
     * Returns true iff o is {@link Pokemon} and all the fields in this and o are equals.
     *
     * @param o {@link java.util.Objects}
     * @return true iff o equals to this.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pokemon pokemon = (Pokemon) o;
        return Double.compare(pokemon._value, _value) == 0 && _type == pokemon._type && _pos.equals(pokemon._pos) && _edge.equals(pokemon._edge);
    }

}
