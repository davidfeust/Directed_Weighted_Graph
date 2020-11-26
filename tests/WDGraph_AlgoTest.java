package ex2.tests;

import ex2.src.api.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class WDGraph_AlgoTest {

    private static dw_graph_algorithms ga;
    private static directed_weighted_graph g;

    @BeforeEach
    void setUp() {
        g = new WDGraph_DS();
        ga = new WDGraph_Algo();
        ga.init(g);
        for (int i = 1; i < 7; i++) {
            g.addNode(new NodeData());
        }
        g.connect(1, 2, 7);
        g.connect(1, 6, 14);
        g.connect(1, 3, 9);
        g.connect(2, 3, 10);
        g.connect(2, 4, 15);
        g.connect(3, 6, 2);
        g.connect(3, 4, 11);
        g.connect(4, 5, 6);
        g.connect(5, 6, 9);
    }

    @Test
    void init() {
        g = new WDGraph_DS();
        ga = new WDGraph_Algo();
        assertNull(ga.getGraph());
        ga.init(g);
        assertNotNull(ga.getGraph());
        assertEquals(g, ga.getGraph());
    }

    @Test
    void getGraph() {
        NodeData.setMaster(0);
        ga = new WDGraph_Algo();
        ga.init(g);
        directed_weighted_graph g1 = ga.getGraph();
//        assertEquals(g1, g);  // needs to implements equal in WDGraph_DS
        assertSame(g1, g);
    }

    @Test
    void copy() {
        directed_weighted_graph g1 = ga.copy();
        assertEquals(g.edgeSize(), g1.edgeSize());
        assertEquals(g.nodeSize(), g1.nodeSize());
        assertEquals(g, g1);
        assertNotSame(g, g1);
        g.getNode(2).setTag(5);
        g1.getNode(2).setTag(4);
        assertEquals(5, g.getNode(2).getTag());
        assertEquals(4, g1.getNode(2).getTag());
        g.connect(1, 5, 1.5);
        assertEquals(1.5, g.getEdge(1, 5).getWeight());
        assertNull(g1.getEdge(1, 5));
        g1.connect(3, 5, 1.9);
        assertNull( g.getEdge(3, 5));
        assertEquals(1.9, g1.getEdge(3, 5).getWeight());
    }

    @Test
    void isConnected() {

    }

    @Test
    void shortestPathDist() {
    }

    @Test
    void shortestPath() {
    }

    @Test
    void save() {
        String path = "G1.json";
        dw_graph_algorithms ga = new WDGraph_Algo();
        directed_weighted_graph g = new WDGraph_DS();
        ga.init(g);
        for (int i = 0; i < 5; i++) {
            g.addNode(new NodeData());
        }
        g.connect(0, 1, 5.265);
        g.connect(1, 0, 8.965);
        g.connect(3, 4, 12.54);
        g.connect(2, 4, 1.1);
        g.connect(0, 4, 3.2);
        ga.save(path);
    }

    @Test
    void load() {
        String path = System.getProperty("user.dir") + "\\src\\ex2\\data\\";
        dw_graph_algorithms ga = new WDGraph_Algo();
        for (int i = 0; i <= 5; i++) {
            ga.load(path + "A" + i);
            System.out.println(ga.getGraph() + "\n");
            NodeData.setMaster(0);
        }
    }

    @Test
    void save_load() {
        NodeData.setMaster(0);
        String path = System.getProperty("user.dir") + "\\src\\ex2\\data\\";
        dw_graph_algorithms ga = new WDGraph_Algo();
        for (int i = 0; i <= 5; i++) {
            ga.load(path + "A" + i);
            ga.save(path + "B" + i + ".json");
            System.out.println(ga.getGraph());
            Path A = Paths.get(path + "A" + i);
            Path B = Paths.get(path + "B" + i + ".json");
            String strA = "", strB = "";
            try {
                strA = new String(Files.readAllBytes(A));
                strB = new String(Files.readAllBytes(B));
            } catch (IOException e) {
                e.printStackTrace();
                fail("fail to read file!");
            }
            if (!strA.equals(strB)) fail("not same file: A" + i + " != B" +i);
            assertEquals(strA, strB);
            NodeData.setMaster(0);
        }
    }
}