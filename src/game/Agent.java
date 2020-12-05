package game;

import api.*;
import com.google.gson.JsonObject;
import game.util.Point3D;

import java.util.ArrayList;
import java.util.List;

public class Agent {
    private int _id;
    private double _value, _speed;
    private geo_location _pos;
    private node_data _node;
    private edge_data _edge;
    private List<node_data> _path;
    private Pokemon _curr_fruit;
    static private directed_weighted_graph _graph;

    public Agent(JsonObject json) {
        update(json);
        _path = new ArrayList<>();
    }

    public void update(JsonObject agent) {
        _id = agent.get("id").getAsInt();
        _value = agent.get("value").getAsDouble();
        _speed = agent.get("speed").getAsDouble();
        _pos = new Point3D(agent.get("pos").getAsString());
        setNode(agent.get("src").getAsInt());
        setNextNode(agent.get("dest").getAsInt());
    }

    public boolean setNextNode(int dest) {
        boolean ans = false;
        int src = this._node.getKey();
        this._edge = _graph.getEdge(src, dest);
        if (_edge != null) {
            ans = true;
        } else {
            _edge = null;
        }
        return ans;
    }

    public int getNextNode() {
        if (this._edge == null) {
            return -1;
        } else {
            return this._edge.getDest();
        }
    }

    public boolean isMoving() {
        return this._edge != null;
    }

    public static void set_graph(directed_weighted_graph _graph) {
        Agent._graph = _graph;
    }

    public void setNode(int src) {
        this._node = _graph.getNode(src);
    }


    public int getId() {
        return _id;
    }

    public double getValue() {
        return _value;
    }

    public double getSpeed() {
        return _speed;
    }

    public geo_location getPos() {
        return _pos;
    }

    public int getSrcNode() {
        return this._node.getKey();
    }

    public Pokemon get_curr_fruit() {
        return _curr_fruit;
    }

    public void set_curr_fruit(Pokemon _curr_fruit) {
        this._curr_fruit = _curr_fruit;
    }

    public List<node_data> get_path() {
        return _path;
    }

    public void set_path(List<node_data> _path) {
        this._path = _path;
    }

    @Override
    public String toString() {
        return "Agent{" +
                "_id=" + _id +
                ", _value=" + _value +
                ", _speed=" + _speed +
                ", _pos=" + _pos +
                ", _node=" + _node +
                ", _edge=" + _edge +
                '}';
    }
}
