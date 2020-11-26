package ex2.tests;

import ex2.src.api.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class WDGraph_DSTest {

    private static directed_weighted_graph g;

    @BeforeEach
    void setUp() {
        g = new WDGraph_DS();
    }

    @Test
    void getNode() {
        node_data n = new NodeData(0);
        g.addNode(n);
        assertEquals(0, g.getNode(0).getKey());
        assertEquals("", g.getNode(0).getInfo());
        assertEquals(-1, g.getNode(0).getTag());
        node_data n1 = g.getNode(0);
        n.setTag(7);
        assertEquals(7, n1.getTag());
        assertEquals(n, n1);
        assertSame(n, n1);
    }

    @Test
    void getEdge() {
        assertNull(g.getEdge(0, 1));
        g.addNode(new NodeData(0));
        g.addNode(new NodeData(1));
        g.connect(0, 1, 1.5);
        assertNotNull(g.getEdge(0, 1));
        assertNull(g.getEdge(1, 0));
        assertEquals(1.5, g.getEdge(0, 1).getWeight());
        g.removeEdge(0, 1);
        assertNull(g.getEdge(0, 1));
        g.connect(0, 1, 2.5);
        g.connect(1, 0, 3.5);
        assertEquals(3.5, g.getEdge(1, 0).getWeight());
        g.addNode(new NodeData(2));
        assertNull(g.getEdge(0, 2));
        g.connect(0, 2, 1.5);
        g.connect(2, 0, 2.5);
        assertEquals(1.5, g.getEdge(0, 2).getWeight());
        assertEquals(2.5, g.getEdge(2, 0).getWeight());
        assertEquals(0, g.getEdge(0, 2).getSrc());
        assertEquals(2, g.getEdge(0, 2).getDest());
    }

    @Test
    void addNode() {
        assertEquals(0, g.nodeSize());
        for (int i = 0; i < 123; i++) {
            g.addNode(new NodeData(i));
        }
        assertEquals(123, g.nodeSize());
        for (int i = 0; i < 123; i++) {
            assertEquals(i, g.getNode(i).getKey());
        }
    }

    @Test
    void connect() {
        g.addNode(new NodeData(0));
        g.addNode(new NodeData(1));
        g.addNode(new NodeData(2));
        g.connect(1, 2, 0.1);
        assertNotNull(g.getEdge(1, 2));
        assertNull(g.getEdge(2, 1));
        g.connect(1, 2, 0.2);
        g.connect(1, 2, 0.3);
        assertEquals(0.3, g.getEdge(1, 2).getWeight());
    }

    @Test
    void getV() {
        assertNotNull(g.getV());
        Collection<node_data> c = g.getV();
        for (int i = 0; i < 20; i++) {
            g.addNode(new NodeData(i));
            assertTrue(c.contains(g.getNode(i)));
        }
    }

    @Test
    void getE() {
        assertNull(g.getE(0));
        g.addNode(new NodeData(0));
        assertNotNull(g.getE(0));
        Collection<edge_data> c = g.getE(0);
        assertEquals(0, c.size());
        for (int i = 1; i < 20; i++) {
            g.addNode(new NodeData(i));
            g.connect(i, 0, 0.2);
            assertFalse(c.contains(g.getEdge(i, 0)));
            g.connect(0, i, 0.3);
            assertTrue(c.contains(g.getEdge(0, i)));
        }
    }

    @Test
    void removeNode() {
        g.addNode(new NodeData(0));
        g.addNode(new NodeData(1));
        g.addNode(new NodeData(2));
        assertEquals(3, g.nodeSize());
        g.removeNode(0);
        g.removeNode(0);
        assertEquals(2, g.nodeSize());
        node_data rem = null;
        for (node_data i : g.getV()) {
            if (i.getKey() == 0)
                rem = i;
        }
        assertNull(rem);
    }

    @Test
    void removeEdge() {
        g.addNode(new NodeData(0));
        g.addNode(new NodeData(1));
        g.addNode(new NodeData(2));
        g.connect(0, 1, 1);
        g.connect(0, 1, 1);
        g.connect(0, 2, 1);
        g.connect(2, 1, 1);
        g.removeEdge(1, 5);
        g.removeEdge(1, 2);
        assertEquals(3, g.edgeSize());
    }

    @Test
    void nodeSize() {
        int r1 = nextRnd(0, 100);
        for (int i = 0; i < r1; i++) {
            g.addNode(new NodeData(i));
        }
        assertEquals(r1, g.nodeSize());
        int r2 = nextRnd(0, r1);
        for (int i = 0; i < r2; i++) {
            g.removeNode(i);
        }
        assertEquals(r1 - r2, g.nodeSize());
    }

    @Test
    void edgeSize() {
        int n = nextRnd(10, 100), e = nextRnd(n, n * 3);
        for (int i = 0; i < n; i++) {
            g.addNode(new NodeData(i));
        }
        while (g.edgeSize() < e) {
            int a = nextRnd(0, n);
            int b = nextRnd(0, n);
            g.connect(a, b, 5);
        }
        assertEquals(e, g.edgeSize());
        int r = nextRnd(0, e);
        while (g.edgeSize() > e - r) {
            g.removeEdge(nextRnd(0, e), nextRnd(0, e));
        }
        assertEquals(e - r, g.edgeSize());
    }

    @Test
    void equal() {
        directed_weighted_graph g1 = new WDGraph_DS();
        directed_weighted_graph g2 = new WDGraph_DS();
        assertEquals(g2, g1);
        for (int i = 0; i < 6; i++) {
            g1.addNode(new NodeData(i));
            g2.addNode(new NodeData(i));
        }
        assertEquals(g2, g1);
        g1.connect(0,1,5);
        g2.connect(0,1,5);
        assertEquals(g2, g1);
    }

///////////////

    static int nextRnd(int min, int max) {
        double v = nextRnd(0.0 + min, 0.0 + max);
        return (int) v;
    }

    private static double nextRnd(double min, double max) {
        Random rnd = new Random();
        double d = rnd.nextDouble();
        double dx = max - min;
        double ans = d * dx + min;
        DecimalFormat df = new DecimalFormat("####0.00");
        return Double.parseDouble(df.format(ans));
    }
}