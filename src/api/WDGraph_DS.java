package ex2.src.api;

import java.util.Collection;
import java.util.HashMap;

public class WDGraph_DS implements directed_weighted_graph {

    private HashMap<Integer, node_data> graphNodes;
    private int edge_size;
    private int mode_count;

    public WDGraph_DS() {
        this.graphNodes = new HashMap<>();
        this.edge_size = 0;
        this.mode_count = 0;
    }


    /**
     * returns the node_data by the node_id,
     *
     * @param key - the node_id
     * @return the node_data by the node_id, null if none.
     */
    @Override
    public node_data getNode(int key) {
        return this.graphNodes.get(key);
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
        NodeData temp = (NodeData) this.graphNodes.get(src);
        return temp.neighborsDis.get(dest);
    }

    /**
     * adds a new node to the graph with the given node_data.
     * Note: this method should run in O(1) time.
     *
     * @param n
     */
    @Override
    public void addNode(node_data n) {
        this.graphNodes.put(n.getKey(), n);
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
        if (this.graphNodes.containsKey(src) && this.graphNodes.containsKey(dest)) {
            if (this.getEdge(src, dest).getWeight() == w) {
                return;
            }

            NodeData tempSrc = (NodeData) this.graphNodes.get(src);
            NodeData tempDest = (NodeData) this.graphNodes.get(dest);
            tempDest.neighborNodes.put(src, tempSrc);
            tempSrc.neighborsDis.put(dest, new EdgeData(src, dest, w));
            this.edge_size++;
            this.mode_count++;
        }
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

        return this.graphNodes.values();
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
        NodeData temp = (NodeData) this.graphNodes.get(node_id);
        return temp.neighborsDis.values();
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

        NodeData temp = (NodeData) this.graphNodes.get(key);
        if (temp == null) {
            return null;
        }
        for (node_data i : temp.neighborNodes.values()) {
            NodeData tempI = (NodeData) i;
            tempI.neighborsDis.remove(key);
            tempI.neighborNodes.remove(temp);
            this.edge_size--;
            this.mode_count++;
        }
        int t = getE(key).size();
        edge_size -= t;
        mode_count += t;
        mode_count++;
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

        NodeData tempSrc = (NodeData) this.graphNodes.get(src);
        NodeData tempDest = (NodeData) this.graphNodes.get(dest);
        if(tempDest == null || tempSrc == null || this.getEdge(src, dest)==null) {
            return null; }
        this.mode_count++;
        this.edge_size--;
        tempDest.neighborNodes.remove(src);
        return tempSrc.neighborsDis.remove(dest);
    }

    /**
     * Returns the number of vertices (nodes) in the graph.
     * Note: this method should run in O(1) time.
     *
     * @return
     */
    @Override
    public int nodeSize() {
        return this.graphNodes.size();
    }

    /**
     * Returns the number of edges (assume directional graph).
     * Note: this method should run in O(1) time.
     *
     * @return
     */
    @Override
    public int edgeSize() {
        return this.edge_size;
    }

    /**
     * Returns the Mode Count - for testing changes in the graph.
     *
     * @return
     */
    @Override
    public int getMC() {
        return this.mode_count;
    }
    public node_data newNode(){
    return new NodeData();
    }

    private static class NodeData implements node_data {
        static int masterKey = 0;
        private int key;
        private HashMap<Integer, node_data> neighborNodes;
        private HashMap<Integer, edge_data> neighborsDis;
        private int Tag;
        double weight;
        private String remark;
        geo_location GLocation;

        public NodeData() {
            this.key = masterKey++;
            this.neighborNodes = new HashMap<>();
            this.neighborsDis = new HashMap<>();
            this.remark = "";
            this.setTag(-1);
            GLocation = null;
            weight = 0;
        }

        public HashMap<Integer, edge_data> getNeighborsDis() {
            return neighborsDis;
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
            return this.Tag;
        }

        /**
         * Allows setting the "tag" value for temporal marking an node - common
         * practice for marking by algorithms.
         *
         * @param t - the new value of the tag
         */
        @Override
        public void setTag(int t) {
            this.Tag = t;
        }
    }


    private class EdgeData implements edge_data {
        int _src, _dest, _tag;
        double _weight;
        String _info;


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
    }
}
