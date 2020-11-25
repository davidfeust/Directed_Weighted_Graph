package ex2.src.api;

import java.util.HashMap;
import java.util.Objects;

public class NodeData implements node_data {

    static int masterKey = 0;
    private final int key;
    private HashMap<Integer, node_data> neighborNodes;
    private HashMap<Integer, edge_data> neighborsDis;
    private int tag;
    private double weight;
    private String remark;
    private geo_location GLocation;

    public NodeData() {
        this.key = masterKey++;
        this.neighborNodes = new HashMap<>();
        this.neighborsDis = new HashMap<>();
        this.remark = "";
        this.setTag(-1);
        GLocation = null;
        weight = 0;
    }

    // should be removed & make masterKey private
    public static void setMaster(int s) {
        masterKey = s;
    }

    public HashMap<Integer, edge_data> getNeighborsDis() {
        return neighborsDis;
    }

    public HashMap<Integer, node_data> getNeighborNodes() {
        return neighborNodes;
    }

    /**
     * Returns the key (id) associated with this node.
     *
     * @return
     */
    @Override
    public int getKey() {
        return this.key;
    }

    /**
     * Returns the location of this node, if
     * none return null.
     *
     * @return
     */
    @Override
    public geo_location getLocation() {
        return GLocation;
    }

    /**
     * Allows changing this node's location.
     *
     * @param p - new new location  (position) of this node.
     */
    @Override
    public void setLocation(geo_location p) {
        this.GLocation = p;
    }

    /**
     * Returns the weight associated with this node.
     *
     * @return
     */
    @Override
    public double getWeight() {
        return weight;
    }

    /**
     * Allows changing this node's weight.
     *
     * @param w - the new weight
     */
    @Override
    public void setWeight(double w) {
        weight = w;
    }

    /**
     * Returns the remark (meta data) associated with this node.
     *
     * @return
     */
    @Override
    public String getInfo() {
        return this.remark;
    }

    /**
     * Allows changing the remark (meta data) associated with this node.
     *
     * @param s
     */
    @Override
    public void setInfo(String s) {
        this.remark = s;
    }

    /**
     * Temporal data (aka color: e,g, white, gray, black)
     * which can be used be algorithms
     *
     * @return
     */
    @Override
    public int getTag() {
        return this.tag;
    }

    /**
     * Allows setting the "tag" value for temporal marking an node - common
     * practice for marking by algorithms.
     *
     * @param t - the new value of the tag
     */
    @Override
    public void setTag(int t) {
        this.tag = t;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeData nodeData = (NodeData) o;
        return key == nodeData.key;
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

    @Override
    public String toString() {
        return "(" + key + ")";
    }
}