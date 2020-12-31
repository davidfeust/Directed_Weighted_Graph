package compare;

import api.WDGraph_Algo;
import api.directed_weighted_graph;
import api.dw_graph_algorithms;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashSet;
import java.util.Set;

import static java.lang.System.currentTimeMillis;
import static java.lang.System.out;

class Compare_to_py {

    private dw_graph_algorithms _ga;
    private directed_weighted_graph _g;

    @Disabled
    @Test
    void connected_component() {
//        WDGraph_DS g = new WDGraph_DS();
        WDGraph_Algo ga = new WDGraph_Algo();
        ga.load("data/A5");
        out.println("ga.connected_component(0) = " + ga.connected_component(0));
        out.println(ga.getGraph());
        ga.getGraph().removeEdge(13, 14);
        out.println("ga.connected_component(0) = " + ga.connected_component(0));
        out.println(ga.getGraph());

    }

    void init(String file) {
        _ga = new WDGraph_Algo();
        _ga.load(file);
        _g = _ga.getGraph();
    }

    @Test
    public void test_built_times() {
        long start_time = currentTimeMillis();
        init("data/10kG.json");
        long end_time = currentTimeMillis();
        out.println("Java Graph: test_built_times(10kG.json): " + (end_time - start_time) / 1000.);
    }

    @Test
    public void test_save() {
        init("data/10kG.json");
        long start_time = currentTimeMillis();
        _ga.save("data/g_saveTestTime.json");
        long end_time = currentTimeMillis();
        out.println("Java Graph: test_save(10kG.json): " + (end_time - start_time) / 1000.);
    }

    @Test
    public void test_shortest_path() {
        init("data/1kG.json");
        long start_time = currentTimeMillis();
        _ga.shortestPath(0, 11);
        long end_time = currentTimeMillis();
        out.println("Java Graph: test_shortest_path(0, 11): " + (end_time - start_time) / 1000.);

        start_time = currentTimeMillis();
        _ga.shortestPath(0, 500);
        end_time = currentTimeMillis();
        out.println("Java Graph: test_shortest_path(0, 500): " + (end_time - start_time) / 1000.);

        start_time = currentTimeMillis();
        _ga.shortestPath(0, 999);
        end_time = currentTimeMillis();
        out.println("Java Graph: test_shortest_path(0, 999): " + (end_time - start_time) / 1000.);
    }

    @Test
    public void test_connected_component() {
        init("data/A5");
        long start_time = currentTimeMillis();
        ((WDGraph_Algo) _ga).connected_component(1);
        long end_time = currentTimeMillis();
        out.println("Java Graph: test_connected_component(1): " + (end_time - start_time) / 1000.);

        init("data/A5_edited");
        start_time = currentTimeMillis();
        ((WDGraph_Algo) _ga).connected_component(1);
        end_time = currentTimeMillis();
        out.println("Java Graph: test_connected_component(1): " + (end_time - start_time) / 1000.);
    }

    @Test
    public void test_connected_components() {
        init("data/A5");
        long start_time = currentTimeMillis();
//        ((WDGraph_Algo) _ga).connected_components();
        long end_time = currentTimeMillis();
        out.println("Java Graph: test_connected_components(): " + (end_time - start_time) / 1000.);

        init("data/A5_edited");
        start_time = currentTimeMillis();
        ((WDGraph_Algo) _ga).connected_component(1);
        end_time = currentTimeMillis();
        out.println("Java Graph: test_connected_components(): " + (end_time - start_time) / 1000.);
    }
}