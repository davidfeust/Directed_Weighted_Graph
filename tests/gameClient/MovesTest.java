package gameClient;

import api.game_service;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MovesTest {

    /**
     * Test if there less then 11 call for move() method in a second.
     */
    @Test
    void movesPerSec() {
        for (int i = 0; i < 24; i++) {
            Runner run = new Runner(i, -1);
            Thread thread = new Thread(run);
            Controller ctrl = new Controller(run, thread, -1, i);
            GameGUI win = new GameGUI(i, ctrl);
            run.set_win(win);
            ctrl.set_win(win);
            long start_time = System.currentTimeMillis();
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
            long end_time = System.currentTimeMillis();
            long game_time = end_time - start_time;
            game_service game = run.get_game();
            JsonObject gameJsonObject = JsonParser.parseString(game.toString()).getAsJsonObject().getAsJsonObject("GameServer");
            int moves = gameJsonObject.get("moves").getAsInt();
            Assertions.assertTrue(11 > moves / ((double) game_time / 1000));
            win.setVisible(false);

        }
    }
}
