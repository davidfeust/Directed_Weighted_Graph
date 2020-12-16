package gameClient;

import Server.Game_Server_Ex2;
import api.game_service;
import api.node_data;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AlgoTest {

    static game_service _game;
    static Agent _agent;
    static Arena _arena;


    @BeforeAll
    static void beforeAll() {
        _game = Game_Server_Ex2.getServer(0);
        _arena = new Arena(_game);
        Algo.set_ar(_arena);
        Algo.set_graph(_arena.get_graph());
    }

    @Order(1)
    @Test
    void placeAgents() {
        assertTrue(_arena.getAgents().isEmpty());
        Algo.placeAgents(1, _game);
        _arena.update(_game);
        _agent = _arena.getAgents().get(0);
        assertNotNull(_agent);
        assertFalse(_arena.getAgents().isEmpty());
        assertEquals(9, _agent.getSrcNode());
    }

    @Order(2)
    @Test
    void createPath() {
        assertNull(_agent.get_curr_fruit());
        assertTrue(_agent.get_path().isEmpty());
        Algo.createPath(_agent);
        assertNotNull(_agent.get_curr_fruit());
        assertFalse(_agent.get_path().isEmpty());
    }

    @Order(3)
    @Test
    void createPathByValDist() {
        Algo.createPathByValDist(_agent);
        ArrayList<node_data> arrayList = new ArrayList<>();
        arrayList.add(_arena.get_graph().getNode(8));
        assertEquals(arrayList, _agent.get_path());
    }

    @Order(4)
    @Test
    void nextMove() {
        _game.startGame();
        assertFalse(_agent.isMoving());
        int nextMove = Algo.nextMove(_game, _agent);
        assertEquals(8, nextMove);
    }

}