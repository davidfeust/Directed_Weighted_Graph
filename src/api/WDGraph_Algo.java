package api;

import com.google.gson.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class WDGraph_Algo implements dw_graph_algorithms {

    private directed_weighted_graph _g;

    // default constructor
    public WDGraph_Algo() {
    }

    public WDGraph_Algo(directed_weighted_graph g) {
        init(g);
    }

    /**
     * Init the graph on which this set of algorithms operates on.
     *
     * @param g
     */
    @Override
    public void init(directed_weighted_graph g) {
        this._g = g;
    }

    /**
     * Return the underlying graph of which this class works.
     *
     * @return
     */
    @Override
    public directed_weighted_graph getGraph() {
        return this._g;
    }

    /**
     * Compute a deep copy of this weighted graph.
     *
     * @return
     */
//    TODO fix that method
    @Override
    public directed_weighted_graph copy() {
        return new WDGraph_DS(_g);
    }

    /**
     * Returns true if and only if (iff) there is a valid path from each node to each
     * other node. NOTE: assume directional graph (all n*(n-1) ordered pairs).
     *
     * @return
     */
    @Override
    public boolean isConnected() {
        int num = -1;
        if (_g.getV().iterator().hasNext()) {
            // initialize the nodes tag
            for (node_data i : this._g.getV()) {
                i.setTag(num);
            }

            node_data connectedNode = _g.getV().iterator().next();
            int sum = this.connectedCheck(connectedNode, connectedNode, num);

            if (sum != _g.getV().size()) {
                return false;
            }

            for (node_data i : _g.getV()) {

                    this.connectedCheck(i, connectedNode, ++num);

                    if (connectedNode.getTag() == num) {
                        return false;
                    }
            }
        }
        return true;
    }

    /**
     * change the tag to the distance between node_data to the rest of the graph nodes until it reach the destination
     * if the tag = -1 the nodes are not connected
     *
     * @@param node_data
     */
    private int connectedCheck(node_data src, node_data dest, int num) {
        int sum = 0;

        Queue<node_data> q = new LinkedList<>();
        src.setTag(num + 1);
        sum++;
        q.add(src);
        while (!q.isEmpty()) {
            int temp = q.poll().getKey();
            for (edge_data i : this._g.getE(temp)) {
                node_data n_d = _g.getNode(i.getDest());
                if (n_d.getTag() != num +1) {
                    sum++;
                    q.add(n_d);
                    n_d.setTag(num + 1);
                    if (n_d == dest)
                        return 0;
                }
            }
        }
        return sum;
    }


    /**
     * returns the the shortest path between src to dest - as an ordered List of nodes:
     * src--> n1-->n2-->...dest
     * see: https://en.wikipedia.org/wiki/Shortest_path_problem
     * Note if no such path --> returns null;
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public List<node_data> shortestPath(int src, int dest) {

        List<node_data> ll = new ArrayList<>();

        double flag = shortestPathDist(src, dest);

        if (flag != -1) {
            node_data Src = this._g.getNode(src);
            node_data Dest = this._g.getNode(dest);

            return ShortPath(Dest, Src, ll, flag);
        }


        return null;
    }

    /**
     * returns the length of the shortest path between src to dest
     * Note: if no such path --> returns -1
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public double shortestPathDist(int src, int dest) {

        node_data Src = _g.getNode(src);
        node_data Dest = _g.getNode(dest);

        if (Src != null && Dest != null) {

            this.initNodeWeight();

            this.initNodeTag();

            setDistance(Src, Dest);

            return Dest.getWeight();
        }

        return -1;
    }

    private void setDistance(node_data n, node_data dest) {

        PriorityQueue<node_data> q = new PriorityQueue<>(); // NodeData dose not implements Comparable
        // maybe we can use PriorityQueue that contains edge_data
        n.setWeight(0);
        q.add(n);

        while (!q.isEmpty()) {
            node_data temp = q.poll();

                for (edge_data i : _g.getE(temp.getKey())) {
                    double SEdge = i.getWeight() + temp.getWeight();
                    if (_g.getNode(i.getDest()).getWeight() == -1 || (_g.getNode(i.getDest()).getWeight() > SEdge )){//&& _g.getNode(i.getDest()).getWeight() != 0)
                        q.add(_g.getNode(i.getDest()));
                        _g.getNode(i.getDest()).setWeight(SEdge);
                    }
                }
//            }
        }
    }

    /**
     * return list with the path from one node to the other
     *
     * @@param node_info src
     * @@param node_info dest
     * @@param ArrayList<node_info> ll
     * @@param integer distance
     */
    private List<node_data> ShortPath(node_data dest, node_data src, List<node_data> ll, double distance) {
        // check if the nodes are even connected return an empty path if they dosnt
        // connected
        Stack<node_data> stack = new Stack<>();

        stack.add(dest);
        node_data temp = stack.peek();
        temp.setTag(-2);

        while (temp != src) {
            NodeData n_d = (NodeData) temp;
                for(node_data i : n_d.getConnectedNode().values()){
                    if (n_d.getWeight() ==  i.getWeight()+_g.getEdge(i.getKey(),temp.getKey()).getWeight() && i.getTag() != -2  ) {//
                        stack.add(i);
                        temp = stack.peek();
                        temp.setTag(-2);
                        break;

                    }

                }
            }

        int t = stack.size();
        for (int i = 0; i < t; i++) {
            ll.add(stack.pop());
        }
        return ll;
    }

    // initialize the nodes tag
    private void initNodeWeight() {
        for (node_data i : _g.getV()) {
            i.setWeight(-1);
        }
    }

    private void  initNodeTag() {
        for (node_data i : _g.getV()) {
            i.setTag(-1);
        }
    }
    /**
     * Saves this weighted (directed) graph to the given
     * file name - in JSON format
     *
     * @param file - the file name (may include a relative path).
     * @return true - iff the file was successfully saved
     */
    @Override
    public boolean save(String file) {
        JsonObject json_obj = new JsonObject();
        JsonArray nodes_arr = new JsonArray();
        for (node_data i : _g.getV()) {
            JsonObject jo_node = new JsonObject();
            if (i.getLocation() != null) {
                String loc = i.getLocation().x() + "," + i.getLocation().y() + "," + i.getLocation().z();
                jo_node.addProperty("pos", loc);
            } else {
                jo_node.addProperty("pos", ",,");
            }
            jo_node.addProperty("id", i.getKey());
            nodes_arr.add(jo_node);
        }

        JsonArray edges_arr = new JsonArray();
        for (node_data i : _g.getV()) {
            for (edge_data j : _g.getE(i.getKey())) {
                JsonObject jo_edge = new JsonObject();
                jo_edge.addProperty("src", j.getSrc());
                jo_edge.addProperty("w", j.getWeight());
                jo_edge.addProperty("dest", j.getDest());
                edges_arr.add(jo_edge);
            }
        }

        json_obj.add("Edges", edges_arr);
        json_obj.add("Nodes", nodes_arr);

        Gson gs = new Gson();
        File f = new File(file);
        try {
            FileWriter fw = new FileWriter(f);
            fw.write(gs.toJson(json_obj));
            fw.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * This method load a graph to this graph algorithm.
     * if the file was successfully loaded - the underlying graph
     * of this class will be changed (to the loaded one), in case the
     * graph was not loaded the original graph should remain "as is".
     *
     * @param file - file name of JSON file
     * @return true - iff the graph was successfully loaded.
     */
    @Override
    public boolean load(String file) {
        JsonObject json_obj;
        try {
            String json_str = new String(Files.readAllBytes(Paths.get(file)));
            directed_weighted_graph g = new WDGraph_DS();
            json_obj = JsonParser.parseString(json_str).getAsJsonObject();

            JsonArray nodes_arr = json_obj.get("Nodes").getAsJsonArray();
            for (JsonElement i : nodes_arr) {
                String[] xyz = i.getAsJsonObject().get("pos").getAsString().split(",");
                node_data n = new NodeData(i.getAsJsonObject().get("id").getAsInt());
                geo_location loc = new Geo_locationImpl
                        (Double.parseDouble(xyz[0]), Double.parseDouble(xyz[1]), Double.parseDouble(xyz[2]));
                n.setLocation(loc);
                g.addNode(n);
            }

            JsonArray edges_arr = json_obj.get("Edges").getAsJsonArray();
            for (JsonElement i : edges_arr) {
                int src = i.getAsJsonObject().get("src").getAsInt();
                int dest = i.getAsJsonObject().get("dest").getAsInt();
                double w = i.getAsJsonObject().get("w").getAsDouble();
                g.connect(src, dest, w);
            }
            init(g);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
