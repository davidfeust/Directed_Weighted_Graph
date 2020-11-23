package ex2.src.api;

import ex1.src.node_info;
import ex1.src.weighted_graph;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;

public class WDGraph_Algo implements dw_graph_algorithms {

    private directed_weighted_graph g;

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
        this.g = g;
    }

    /**
     * Return the underlying graph of which this class works.
     *
     * @return
     */
    @Override
    public directed_weighted_graph getGraph() {
        return  this.g;
    }

    /**
     * Compute a deep copy of this weighted graph.
     *
     * @return
     */
    @Override
    public directed_weighted_graph copy() {

        directed_weighted_graph g1 = new WDGraph_DS();

        for (node_data i : g.getV()) {
            g1.addNode(i);//--------------------------------we need to correct the add
        }
        for (node_data i : g.getV()) {
            for (edge_data j : g.getE(i.getKey())) {

                g1.connect(j.getSrc(), j.getDest(), j.getWeight());
            }
        }

        return g1;
    }

    /**
     * Returns true if and only if (iff) there is a valid path from each node to each
     * other node. NOTE: assume directional graph (all n*(n-1) ordered pairs).
     *
     * @return
     */
    @Override
    public boolean isConnected() {
        return false;
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

        node_data Src = g.getNode(src);
        node_data Dest = g.getNode(dest);

        if (Src != null && Dest != null) {

            // if the dest is the src itself
            if (Src == Dest)
                return 0;

            // initialize the nodes tag
            for (node_data i : g.getV()) {
                i.setTag(-1);
            }

            setDistance(Src, Dest);

            return Dest.getTag();
        }

        return -1;
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
            node_data Src = this.g.getNode(src);
            node_data Dest = this.g.getNode(dest);

            return ShortPath(Dest, Src, ll, flag);
        }


        return null;
    }

    private void setDistance(node_data n, node_data dest) {

        PriorityQueue<node_data> q = new PriorityQueue<>();

        n.setTag(0);
        q.add(n);

        while (!q.isEmpty()) {
            node_data temp = q.poll();

            boolean flag = true;

            if (dest.getTag() > 0) {
                {
                    while (flag) {
                        flag = false;
                        if (temp != null && temp.getTag() > dest.getTag()) {
                            temp = q.poll();
                            flag = true;
                        }
                    }
                }
            }

            if (temp != null) {
                for (edge_data i : g.getE(temp.getKey())) {
                    double SEdge = i.getWeight() + temp.getTag();
                    if (g.getNode(i.getDest()).getTag() == -1 || (g.getNode(i.getDest()).getTag() > SEdge && g.getNode(i.getDest()).getTag() != 0)) {
                        q.add(g.getNode(i.getDest()));
                        g.getNode(i.getDest()).setTag(SEdge);//--------------tag get int
                    }
                }
            }
        }
    }
    /**
     *
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
        double index = distance;
        Stack<node_data> stack = new Stack<>();

        stack.add(dest);
        node_data temp = stack.peek();

        while (temp != src) {

            for (edge_data i : g.getE(temp.getKey())) {
//                double edge = g.getEdge(i.getKey(), temp.getKey());
                if (temp.getTag() == temp.getTag() + i.getWeight() && i.getTag() != -1) {
                    stack.add(g.getNode(i.getDest()));
                    temp.setTag(-1);
                    index = index - i.getWeight();
                    temp = stack.peek();
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









    /**
     * Saves this weighted (directed) graph to the given
     * file name - in JSON format
     *
     * @param file - the file name (may include a relative path).
     * @return true - iff the file was successfully saved
     */
    @Override
    public boolean save(String file) {
        return false;
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
        return false;
    }
}
