package compare;

import api.WDGraph_Algo;
import api.WDGraph_DS;
import org.junit.jupiter.api.Test;

class Compare_to_py {

    @Test
    void connected_component() {
//        WDGraph_DS g = new WDGraph_DS();
        WDGraph_Algo ga = new WDGraph_Algo();
        ga.load("data/A5");
        System.out.println("ga.connected_component(0) = " + ga.connected_component(0));
        System.out.println(ga.getGraph());
        ga.getGraph().removeEdge(13,14);
        System.out.println("ga.connected_component(0) = " + ga.connected_component(0));
        System.out.println(ga.getGraph());

    }


}