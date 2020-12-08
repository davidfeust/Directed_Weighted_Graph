package game;

import org.junit.jupiter.api.Test;

class RunnerTest {

    @Test
    void runAllGames() {
        for (int i = 0; i < 24; i++) {
            Runner run = new Runner(i, 205474026);
            Thread thread = new Thread(run);
            Controller ctrl = new Controller(run, thread);
            GameGUIPlus win = new GameGUIPlus(i, ctrl);
            run.set_win(win);
            ctrl.set_win(win);
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
            thread.stop();
        }
    }

    @Test
    void runAll() {
        for (int i = 0; i < 24; i++) {
            Runner run = new Runner(i, 0);
            Thread thread = new Thread(run);
            Controller ctrl = new Controller(run, thread);
            GameGUIPlus win = new GameGUIPlus(i, ctrl);
            run.set_win(win);
            ctrl.set_win(win);
            thread.start();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}