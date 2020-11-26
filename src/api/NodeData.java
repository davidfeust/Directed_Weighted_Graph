package ex2.src.api;

import java.util.HashMap;
import java.util.Objects;

public class NodeData implements node_data , Comparable<node_data> {

    private static int _masterKey = 0;
    private final int _key;
    private final HashMap<Integer, node_data> _connectedNode;
    private final HashMap<Integer, edge_data> _neighborsDis;
    private int _tag;
    private double _weight;
    private String _remark;
    private geo_location _GLocation;

    public NodeData() {
        this._key = _masterKey++;
        this._connectedNode = new HashMap<>();
        this._neighborsDis = new HashMap<>();
        this._remark = "";
        this.setTag(-1);
        _GLocation = new Geo_locationImpl(0,0,0);
        _weight = 0;
    }

    public NodeData(int key) {
        this._key = key;
        this._connectedNode = new HashMap<>();
        this._neighborsDis = new HashMap<>();
        this._remark = "";
        this.setTag(-1);
        _GLocation = new Geo_locationImpl(0,0,0);
        _weight = 0;
    }

    /**
     * Copy constructor
     *
     * @param n node data
     */
    public NodeData(node_data n) {
        _key = n.getKey();
        _remark = n.getInfo();
        _weight = n.getWeight();
        _tag = n.getTag();
        _connectedNode = new HashMap<>();
        _neighborsDis = new HashMap<>();
        if (n.getLocation() == null)
            _GLocation = null;
        else
            _GLocation = new Geo_locationImpl(n.getLocation());
    }

    public HashMap<Integer, edge_data> getNeighborsDis() {
        return _neighborsDis;
    }

    public HashMap<Integer, node_data> getConnectedNode() { return _connectedNode; }

    /**
     * Returns the key (id) associated with this node.
     *
     * @return
     */
    @Override
    public int getKey() {
        return this._key;
    }

    /**
     * Returns the location of this node, if
     * none return null.
     *
     * @return
     */
    @Override
    public geo_location getLocation() {
        return _GLocation;
    }

    /**
     * Allows changing this node's location.
     *
     * @param p - new new location  (position) of this node.
     */
    @Override
    public void setLocation(geo_location p) {
        this._GLocation = p;
    }

    /**
     * Returns the weight associated with this node.
     *
     * @return
     */
    @Override
    public double getWeight() {
        return _weight;
    }

    /**
     * Allows changing this node's weight.
     *
     * @param w - the new weight
     */
    @Override
    public void setWeight(double w) {
        _weight = w;
    }

    /**
     * Returns the remark (meta data) associated with this node.
     *
     * @return
     */
    @Override
    public String getInfo() {
        return this._remark;
    }

    /**
     * Allows changing the remark (meta data) associated with this node.
     *
     * @param s
     */
    @Override
    public void setInfo(String s) {
        this._remark = s;
    }

    /**
     * Temporal data (aka color: e,g, white, gray, black)
     * which can be used be algorithms
     *
     * @return
     */
    @Override
    public int getTag() {
        return this._tag;
    }

    /**
     * Allows setting the "tag" value for temporal marking an node - common
     * practice for marking by algorithms.
     *
     * @param t - the new value of the tag
     */
    @Override
    public void setTag(int t) {
        this._tag = t;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeData nodeData = (NodeData) o;
        return _key == nodeData._key &&
                _neighborsDis.equals(nodeData._neighborsDis) &&
                _GLocation.equals(nodeData._GLocation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_key);
    }

    @Override
    public String toString() {
        return "(" + _key + ")";
    }

    @Override
    public int compareTo(node_data o) {
        if (this._weight > o.getWeight())
            return 1;
        if (this._weight < o.getWeight())
            return -1;
        return 0;
    }
}