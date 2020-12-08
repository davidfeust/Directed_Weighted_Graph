package game;

import org.junit.jupiter.api.Test;

class RunnerTest {

    @Test
    void runAllGames() {
        for (int i = 0; i < 24; i++) {
            Thread t = new Thread(new Runner(i, 205474026));
            t.start();
            try {
                t.join();
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
            t.stop();
        }
    }
}