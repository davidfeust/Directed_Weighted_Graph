package ex2.tests;

import ex2.src.api.*;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class WDGraph_AlgoTest {

    @Test
    void init() {
    }

    @Test
    void getGraph() {
    }

    @Test
    void copy() {
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
        String path = "G1.json";//System.getProperty("user.dir") + "\\src\\ex2\\data\\";
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
        String path = System.getProperty("user.dir") + "\\src\\ex2\\data\\";
        dw_graph_algorithms ga = new WDGraph_Algo();
        for (int i = 0; i <= 5; i++) {
            ga.load(path + "A" + i);
            ga.save(path + "B" + i + ".json");
            Path A = Paths.get(path + "A" + i);
            Path B = Paths.get(path + "B" + i + ".json");
            String strA ="", strB="";
            try {
                strA = new String(Files.readAllBytes(A));
                strB = new String(Files.readAllBytes(B));
            } catch (IOException e) {
                e.printStackTrace();
                fail("fail to read file!");
            }
            System.out.println(strA);
            System.out.println(strB);
            assertEquals(strA, strB);
            NodeData.setMaster(0);
        }
    }
}