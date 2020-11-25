//package ex2.tests;
//import ex2.src.*;
//
//import ex2.src.api.WDGraph_DS;
//import ex2.src.api.directed_weighted_graph;
//import ex2.src.api.node_data;
//import org.junit.jupiter.api.Test;
//
//import java.util.Date;
//import java.util.LinkedList;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class myWDGraph_DSTest {
//    /**
//     * test for the speed to build a new graph
//     * in the test I build a new graph with a million nodes and 10 million edges
//     * the assert true check if the time that take to the program to build million nodes is less then 10 seconds
//     */
//    @Test
//    void million() {
//        long start = new Date().getTime();
//
//        directed_weighted_graph million = new WDGraph_DS();
//        for (int i = 0; i < 1000000; i++) {
//            million.addNode(million);
//        }
//        for (int i = 0; i < million.nodeSize(); i++) {
//            for (int j = 0; j < 10; j++) {
//
//                million.connect(i, i - j, i * Math.E);
//            }
//        }
//        long end = new Date().getTime();
//        double dt = (end - start) / 1000.0;
//        assertTrue(dt < 10, "fail to build million nodes in 10 seconds");
//    }
//
//    /**
//     * test for the function getNode
//     * in the test I build a new weighted graph and added a few nodes
//     * the assert true check if the key of the node equal to the info that I added to him
//     */
//    @Test
//    void getNode() {
//        directed_weighted_graph g = new WGraph_DS();
//        for (int i = 0; i < 10; i++) {
//            g.addNode(i);
//            if (i == 0) {
//                g.getNode(0).setInfo("working");
//            }
//            if (i == 1) {
//                g.getNode(1).setInfo("a");
//            }
//
//        }
//
//        assertTrue(g.getNode(0).getInfo() == "working", "fail to get the right node ");
//        assertFalse(g.getNode(1).getInfo() == "working", "fail to get the right node ");
//
//    }
//
//    /**
//     * test for the function hasEdge
//     * in the test I build a new weighted graph and added a few nodes, then I connected them to each other
//     * the assert true check if the node connected, and assert false check before I coonnect them
//     */
//    @Test
//    void testHasEdge() {
//        directed_weighted_graph g = new WGraph_DS();
//        for (int i = 0; i < 10; i++) {
//            g.addNode(i);
//        }
//        String error = "hasEdge function dosent work properly";
//
//        assertFalse(g.hasEdge(0, 2), error);
//        assertFalse(g.hasEdge(2, 4), error);
//        assertFalse(g.hasEdge(6, 8), error);
//        assertFalse(g.hasEdge(9, 9), error);
//        //connect the nodes
//        for (int i = 0; i < g.nodeSize(); i++) {
//            for (int j = 0; j < 10; j++) {
//                g.connect(j, i, i * j);
//            }
//        }
//        assertTrue(g.hasEdge(0, 2), error);
//        assertTrue(g.hasEdge(2, 4), error);
//        assertTrue(g.hasEdge(6, 8), error);
//        //check that the node doesn't "connected" to itself
//        assertFalse(g.hasEdge(9, 9), error);
//    }
//
//    /**
//     * test for the function GetEdge
//     * in the test I build a new weighted graph and added a few nodes, then I connected them to each other
//     * the assert true check if the function return the right value
//     */
//    @Test
//    void testGetEdge() {
//        directed_weighted_graph g = new WGraph_DS();
//        for (int i = 0; i < 10; i++) {
//            g.addNode(i);
//        }
//        String error = "getEdge function dosent work properly";
//
//        assertTrue(g.getEdge(0, 2) == -1, error);
//        assertTrue(g.getEdge(2, 4) == -1, error);
//        assertTrue(g.getEdge(6, 8) == -1, error);
//        assertTrue(g.getEdge(9, 9) == -1, error);
//        //connect the nodes
//        for (int i = 0; i < g.nodeSize(); i++) {
//            for (int j = 0; j < 10; j++) {
//                g.connect(j, i, 2.5);
//            }
//        }
//        assertTrue(g.getEdge(0, 2) == 2.5, error);
//        assertTrue(g.getEdge(2, 4) == 2.5, error);
//        assertTrue(g.getEdge(6, 8) == 2.5, error);
//        assertFalse(g.getEdge(0, 2) == -1, error);
//        assertFalse(g.getEdge(2, 4) == -1, error);
//        assertFalse(g.getEdge(6, 8) == 2, error);
//        assertTrue(g.getEdge(9, 9) == -1, error);
//
//    }
//
//    /**
//     * test for the function AddNode
//     * in the test I build a new weighted graph and added a million nodes
//     * the assert true check if the function run in legitimate time run, and if all the nodes are added
//     */
//    @Test
//    void testAddNode() {
//        String error = "AddNode function dosent work properly";
//
//        long start = new Date().getTime();
//
//        directed_weighted_graph Million = new WGraph_DS();
//        for (int i = 0; i < 1000000; i++) {
//            Million.addNode(i);
//        }
//        long end = new Date().getTime();
//        double dt = (end - start) / 1000.0;
//        assertTrue(Million.nodeSize() == 1000000, error);
//        assertTrue(dt < 10, error + " time fault");
//    }
//
//    /**
//     * test for the function Connect
//     * in the test I build a new weighted graph and added a 100 nodes
//     * the assert true check if the function run in legitimate time run, and if all the nodes are connected
//     * the assert false check the re define of the edge between two nodes
//     */
//    @Test
//    void testConnect() {
//        String error = "Connect function dosent work properly";
//
//        long start = new Date().getTime();
//
//        directed_weighted_graph g = new WGraph_DS();
//        for (int i = 0; i < 100; i++) {
//            g.addNode(i);
//        }
//        for (int i = 0; i < g.nodeSize() - 10; i++) {
//            for (int j = 10; j > 0; j--) {
//                g.connect(i, j + i, Math.random() * 10);
//            }
//        }
//
//        long end = new Date().getTime();
//        double dt = (end - start) / 1000.0;
//
//        assertTrue(g.nodeSize() == 100, error);
//        assertTrue(dt < 10, error + " time fault");
//
//        directed_weighted_graph_algorithms ga = new WGraph_Algo();
//        ga.init(g);
//        assertTrue(ga.isConnected(), error + ", the graph is not connected");
//
//        boolean thrown = false;
//        try {
//            g.addNode(300);
//            g.connect(0, 300, -3.1);
//            g.connect(0, 300, -3);
//        } catch (IllegalArgumentException e) {
//            thrown = true;
//        }
//        assertTrue(thrown, error + " did not throw IllegalArgumentException");
//
//        //checking the re define the edge between two nodes
//        double prevDist = g.getEdge(1, 9);
//        g.connect(1, 9, prevDist + 10);
//        assertTrue(g.getEdge(1, 9) != prevDist, error + "did not re define the value of the edge");
//    }
//
//    /**
//     * test for the function GetV
//     * in the test I build a new weighted graph and added a 5 nodes
//     * the assert true check if the function work and give you the shallow copy of the graph
//     */
//    @Test
//    void testGetV() {
//        directed_weighted_graph g = new WGraph_DS();
//        for (int i = 0; i < 5; i++) {
//            g.addNode(i);
//        }
//        for (int i = 0; i < g.nodeSize() - 1; i++) {
//            g.connect(i, i + 1, 0);
//
//        }
//        directed_weighted_graph_algorithms ga = new WGraph_Algo();
//        ga.init(g);
//
//        LinkedList<node_info> al = new LinkedList<>(g.getV());
//        for (node_info i : al) {
//            i.setInfo("has been marked");
//        }
//        for (int i = 0; i < g.nodeSize() - 1; i++) {
//            assertTrue(g.getNode(i).getInfo() == "has been marked", "did not change the actual graph");
//        }
//
//    }
//
//    /**
//     * test for the function GetVInt
//     * in the test I build a new weighted graph and added a 5 nodes
//     * the assert true check if the function returns a Collection that containing all the
//     * nodes connected to node_id
//     */
//    @Test
//    void testGetVInt() {
//        directed_weighted_graph g = new WGraph_DS();
//        for (int i = 0; i < 5; i++) {
//            g.addNode(i);
//        }
//
//        for (int i = 0; i < g.nodeSize(); i++) {
//            g.connect(0, i, 2);
//        }
//
//        LinkedList<node_info> al = new LinkedList<>(g.getV(0));
//        assertTrue(al.size() == 4, "GetVInt dosent work properly");
//        for (int i = 1; i < 5; i++) {
//            assertTrue(al.contains(g.getNode(i)), "GetVInt dosent work properly");
//        }
//    }
//
//    /**
//     * test for the function removeNode
//     * in the test I build a new weighted graph and added a 5 nodes
//     * the assert true check if the all the node have edge with node(key=0)
//     * and after that I use the function removeNode I check if all the edges has been removed
//     */
//    @Test
//    void removeNode() {
//        directed_weighted_graph g = new WGraph_DS();
//        for (int i = 0; i < 5; i++) {
//            g.addNode(i);
//        }
//
//        for (int i = 0; i < g.nodeSize(); i++) {
//            g.connect(0, i, 2);
//        }
//
//        for (int i = 1; i < g.nodeSize(); i++) {
//            assertTrue(g.getEdge(0, i) == 2, "removeNode dosent work properly");
//        }
//        g.removeNode(0);
//
//        for (int i = 1; i < g.nodeSize(); i++) {
//            Double temp = g.getEdge(0, i);
//            assertTrue(temp == -1, "removeNode dosent work properly");
//        }
//    }
//
//    /**
//     * test for the function removeEdge
//     * in the test I build a new weighted graph and added a 5 nodes
//     * the assert true check if the node(key=1) has edge with the node(key=0)
//     * and after that I use the function removeEdge I check if the edge has been removed
//     * and the other edges still connected
//     */
//    @Test
//    void removeEdge() {
//        directed_weighted_graph g = new WGraph_DS();
//        for (int i = 0; i < 5; i++) {
//            g.addNode(i);
//        }
//
//        for (int i = 0; i < g.nodeSize(); i++) {
//            g.connect(0, i, 2);
//        }
//        assertTrue(g.getEdge(0, 1) == 2, "removeEdge dosent work properly");
//
//        g.removeEdge(0, 1);
//
//        assertTrue(g.getEdge(0, 1) == -1, "removeEdge dosent work properly");
//
//        assertTrue(g.getEdge(0, 2) == 2, "removeEdge dosent work properly");
//
//
//    }
//
//    /**
//     * test for the function nodeSize
//     * in the test I build a new weighted graph and added a 500 nodes
//     * the assert true check if the nodeSize return 500
//     * and after that I use the function removeNode to check if the nodeSize changing
//     */
//    @Test
//    void nodeSize() {
//        directed_weighted_graph g = new WGraph_DS();
//        for (int i = 0; i < 500; i++) {
//            g.addNode(i);
//        }
//        assertTrue(g.nodeSize() == 500, "nodeSize dosent work properly");
//
//        for (int i = 0; i < g.nodeSize() - 10; i++) {
//            for (int j = 0; j < 10; j++) {
//                g.connect(i, i + j, 2);
//            }
//        }
//        assertTrue(g.nodeSize() == 500, "nodeSize dosent work properly");
//        //checking nodeSize after changing the edges weight (w)
//        for (int i = 0; i < g.nodeSize() - 10; i++) {
//            for (int j = 0; j < 10; j++) {
//                g.connect(i, i + j, 3);
//            }
//        }
//        assertTrue(g.nodeSize() == 500, "nodeSize dosent work properly");
//
//        for (int i = 0; i < 100; i++) {
//            g.removeNode(i);
//        }
//        assertTrue(g.nodeSize() == 400, "nodeSize dosent work properly");
//    }
//
//    /**
//     * test for the function edgeSize
//     * in the test I build a new weighted graph and added a 50 nodes
//     * and then I added 50 edgs in the graph
//     * the assert true check if the edgeSize return 50
//     * and after that I use the function removeNode to check if the edgeSize changing to the right value
//     * and after that I use the function removeEdge to check if the edgeSize changing to the right value
//     */
//    @Test
//    void edgeSize() {
//        directed_weighted_graph g = new WGraph_DS();
//        for (int i = 0; i < 50; i++) {
//            g.addNode(i);
//        }
//        for (int i = 0; i < 10; i++) {
//            for (int j = 10; j < 15; j++) {
//                g.connect(i, i + j, 2);
//            }
//        }
//        assertTrue(g.edgeSize() == 50, "edgeSize dosent work properly");
//
//        //checking nodeSize after changing the edges weight (w)
//        for (int i = 0; i < 10; i++) {
//            for (int j = 10; j < 15; j++) {
//                g.connect(i, i + j, 5);
//            }
//        }
//        assertTrue(g.edgeSize() == 50, "edgeSize dosent work properly");
//
//        g.removeNode(0);
//        g.removeNode(1);
//        g.removeNode(2);
//        g.removeNode(0);
//
//        assertTrue(g.edgeSize() == 35, "edgeSize dosent work properly");
//
//        g.removeEdge(3, 13);
//        g.removeEdge(3, 13);
//        g.removeEdge(3, 13);
//        g.removeEdge(4, 16);
//
//        assertTrue(g.edgeSize() == 33, "edgeSize dosent work properly");
//
//    }
//
//    /**
//     * test for the function getMC
//     * in the test I build a new weighted graph and added a 50 nodes
//     * and then I added 50 edgs in the graph
//     * the assert true check if the getMC according to the changes in the graph
//     */
//    @Test
//    void getMC() {
//        directed_weighted_graph g = new WGraph_DS();
//        for (int i = 0; i < 50; i++) {
//            g.addNode(i);
//        }
//
//        for (int i = 0; i < 10; i++) {
//            for (int j = 10; j < 15; j++) {
//                g.connect(i, i + j, 2);
//            }
//        }
//
//        //check getMC after I connected the same nodes with the same weight (w)
//        int prevMc = g.getMC();
//
//        for (int i = 0; i < 10; i++) {
//            for (int j = 10; j < 15; j++) {
//                g.connect(i, i + j, 2);
//            }
//        }
//        int currentMc = g.getMC();
//        assertTrue(currentMc == prevMc, "getMC dosent work properly");
//
//
//        //checking getMC after changing the edges weight (w)
//        prevMc = g.getMC();
//        for (int i = 0; i < 10; i++) {
//            for (int j = 10; j < 15; j++) {
//                g.connect(i, i + j, 3);
//            }
//        }
//        currentMc = g.getMC();
//        assertFalse(currentMc == prevMc, "getMC dosent work properly");
//
//        //checking getMC after removing one of the edges
//        prevMc = g.getMC();
//        g.removeEdge(0,12);
//        currentMc = g.getMC();
//        assertFalse(currentMc == prevMc, "getMC dosent work properly");
//
//        prevMc = g.getMC();
//        g.removeEdge(0,12);
//        assertTrue(currentMc == prevMc, "getMC dosent work properly");
//
//        //checking getMC after removing one of the nodes
//        prevMc = g.getMC();
//        g.removeNode(0);
//        currentMc = g.getMC();
//        assertFalse(currentMc == prevMc, "getMC dosent work properly");
//
//    }
//    /**
//     * test for the function Equals
//     * in the test I build two weighted graph and added a 50 nodes
//     * and then I added 50 edgs in the graph
//     * the assert true check if they are equals
//     */
//    @Test
//    void testEquals() {
//        directed_weighted_graph g = new WGraph_DS();
//        for (int i = 0; i < 50; i++) {
//            g.addNode(i);
//        }
//
//        for (int i = 0; i < 10; i++) {
//            for (int j = 10; j < 15; j++) {
//                g.connect(i, i + j, 2);
//            }
//        }
//
//        directed_weighted_graph g1 = new WGraph_DS();
//        for (int i = 0; i < 50; i++) {
//            g1.addNode(i);
//        }
//
//        for (int i = 0; i < 10; i++) {
//            for (int j = 10; j < 15; j++) {
//                g1.connect(i, i + j, 2);
//            }
//        }
//
//        assertEquals(g,g1,"the function equals dosent work properly");
//        g.removeNode(0);
//        assertNotEquals(g,g1,"the function equals dosent work properly");
//
//    }
//}
