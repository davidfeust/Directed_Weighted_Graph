package ex2.src.api;

import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

public class WDGraph_DS implements directed_weighted_graph {

    private HashMap<Integer, node_data> _graphNodes;
    private int _edge_size;
    private int _mode_count;

    public WDGraph_DS() {
        this._graphNodes = new HashMap<>();
        this._edge_size = 0;
        this._mode_count = 0;
    }

    /**
     * Copy constructor
     *
     * @param g
     */
    public WDGraph_DS(directed_weighted_graph g) {
        _graphNodes = new HashMap<>();
        for (node_data i : g.getV()) {
            _graphNodes.put(i.getKey(), new NodeData(i));
        }
        for (node_data i : g.getV()) {
            for (edge_data j : g.getE(i.getKey())) {
                connect(i.getKey(), j.getDest(), g.getEdge(i.getKey(), j.getDest()).getWeight());
            }
        }
    }

    /**
     * returns the node_data by the node_id,
     *
     * @param key - the node_id
     * @return the node_data by the node_id, null if none.
     */
    @Override
    public node_data getNode(int key) {
        return this._graphNodes.get(key);
    }

    /**
     * returns the data of the edge (src,dest), null if none.
     * Note: this method should run in O(1) time.
     *
     * @param src
     * @param dest
     * @return
     */
    @Override
    public edge_data getEdge(int src, int dest) {
        NodeData temp = (NodeData) this.getNode(src);
        if (temp == null)
            return null;
        return temp.getNeighborsDis().get(dest);
    }

    /**
     * adds a new node to the graph with the given node_data.
     * Note: this method should run in O(1) time.
     *
     * @param n
     */
    @Override
    public void addNode(node_data n) {
        if (!this._graphNodes.containsKey(n.getKey())) {
        this._graphNodes.put(n.getKey(), n);
    }
    }

    /**
     * Connects an edge with weight w between node src to node dest.
     * * Note: this method should run in O(1) time.
     *
     * @param src  - the source of the edge.
     * @param dest - the destination of the edge.
     * @param w    - positive weight representing the cost (aka time, price, etc) between src-->dest.
     */
    @Override
    public void connect(int src, int dest, double w) {
        NodeData tempSrc = (NodeData) this.getNode(src);
        NodeData tempDest = (NodeData) this.getNode(dest);
        if (tempSrc == null || tempDest == null) {
            return;
        }
        if (this.getEdge(src, dest) != null)
            if (this.getEdge(src, dest).getWeight() == w || src == dest) {
                return;
            }
        if (this.getEdge(src, dest) == null) {
            this._edge_size++;
        }

        tempDest.getConnectedNode().put(src, tempSrc);
        tempSrc.getNeighborsDis().put(dest, new EdgeData(src, dest, w));
        this._mode_count++;
    }

    /**
     * This method returns a pointer (shallow copy) for the
     * collection representing all the nodes in the graph.
     * Note: this method should run in O(1) time.
     *
     * @return Collection<node_data>
     */
    @Override
    public Collection<node_data> getV() {
        return this._graphNodes.values();
    }

    /**
     * This method returns a pointer (shallow copy) for the
     * collection representing all the edges getting out of
     * the given node (all the edges starting (source) at the given node).
     * Note: this method should run in O(k) time, k being the collection size.
     *
     * @param node_id
     * @return Collection<edge_data>
     */
    @Override
    public Collection<edge_data> getE(int node_id) {
        NodeData temp = (NodeData) this.getNode(node_id);
        if (temp == null)
            return null;
        return temp.getNeighborsDis().values();
    }

    /**
     * Deletes the node (with the given ID) from the graph -
     * and removes all edges which starts or ends at this node.
     * This method should run in O(k), V.degree=k, as all the edges should be removed.
     *
     * @param key
     * @return the data of the removed node (null if none).
     */
    @Override
    public node_data removeNode(int key) {
        NodeData temp = (NodeData) this.getNode(key);
        if (temp == null) {
            return null;
        }
        for (node_data i : temp.getConnectedNode().values()) {
            NodeData tempI = (NodeData) i;
            tempI.getNeighborsDis().remove(key);
            tempI.getConnectedNode().remove(key);
            this._edge_size--;
            this._mode_count++;
        }
        int t = getE(key).size();
        _edge_size -= t;
        _mode_count += t;
        _mode_count++;
        this._graphNodes.remove(key);
        return temp;
    }

    /**
     * Deletes the edge from the graph,
     * Note: this method should run in O(1) time.
     *
     * @param src
     * @param dest
     * @return the data of the removed edge (null if none).
     */
    @Override
    public edge_data removeEdge(int src, int dest) {
        NodeData tempSrc = (NodeData) this.getNode(src);
        NodeData tempDest = (NodeData) this.getNode(dest);
        if (tempDest == null || tempSrc == null || this.getEdge(src, dest) == null) {
            return null;
        }
        this._mode_count++;
        this._edge_size--;
        tempDest.getConnectedNode().remove(src);
        return tempSrc.getNeighborsDis().remove(dest);
    }

    /**
     * Returns the number of vertices (nodes) in the graph.
     * Note: this method should run in O(1) time.
     *
     * @return
     */
    @Override
    public int nodeSize() {
        return this._graphNodes.size();
    }

    /**
     * Returns the number of edges (assume directional graph).
     * Note: this method should run in O(1) time.
     *
     * @return
     */
    @Override
    public int edgeSize() {
        return this._edge_size;
    }

    /**
     * Returns the Mode Count - for testing changes in the graph.
     *
     * @return
     */
    @Override
    public int getMC() {
        return this._mode_count;
    }

    @Override
    public String toString() {
        String edgesStr = "[";
        for (node_data i : getV()) {
            for (edge_data j : getE(i.getKey())) {
                edgesStr += j + ", ";
            }
        }
        return "WDGraph_DS{" +
                ", edge_size=" + _edge_size +
                ", mode_count=" + _mode_count +
                "\n\tNodes=" + _graphNodes +
                "\n\tEdges=" + edgesStr + "]" +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WDGraph_DS that = (WDGraph_DS) o;
        return _edge_size == that._edge_size &&
                _graphNodes.equals(that._graphNodes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_graphNodes, _edge_size);
    }

    private class EdgeData implements edge_data {

        private int _src, _dest, _tag;
        private double _weight;
        private String _info;


        public EdgeData(int src, int dest, double weight) {
            _src = src;
            _dest = dest;
            _tag = 0;
            _weight = weight;
            _info = "";
        }

        /**
         * The id of the source node of this edge.
         *
         * @return
         */
        @Override
        public int getSrc() {
            return _src;
        }

        /**
         * The id of the destination node of this edge
         *
         * @return
         */
        @Override
        public int getDest() {
            return _dest;
        }

        /**
         * @return the weight of this edge (positive value).
         */
        @Override
        public double getWeight() {
            return _weight;
        }

        /**
         * Returns the remark (meta data) associated with this edge.
         *
         * @return
         */
        @Override
        public String getInfo() {
            return _info;
        }

        /**
         * Allows changing the remark (meta data) associated with this edge.
         *
         * @param s
         */
        @Override
        public void setInfo(String s) {
            this._info = s;
        }

        /**
         * Temporal data (aka color: e,g, white, gray, black)
         * which can be used be algorithms
         *
         * @return
         */
        @Override
        public int getTag() {
            return _tag;
        }

        /**
         * This method allows setting the "tag" value for temporal marking an edge - common
         * practice for marking by algorithms.
         *
         * @param t - the new value of the tag
         */
        @Override
        public void setTag(int t) {
            this._tag = t;
        }

        @Override
        public String toString() {
            return "(" + _src + " -> " + _dest + "): weight=" + _weight + '}';
        }
    }
}
