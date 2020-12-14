package gameClient;

import org.junit.jupiter.api.Test;

class RunnerTest {

    private final int _id = 205474026;

    @Test
    void runAllGames() {
        for (int i = 0; i < 24; i++) {
            Runner run = new Runner(i, _id);
            Thread thread = new Thread(run);
            Controller ctrl = new Controller(run, thread, _id, i);
            GameGUI win = new GameGUI(i, ctrl);
            run.set_win(win);
            ctrl.set_win(win);
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
            win.setVisible(false);
        }
    }

    @Test
    void runFewGames() {
        int[] levels = {17,16,22,23,0,1};
        for (int i : levels) {
            Runner run = new Runner(i, _id);
            Thread thread = new Thread(run);
            Controller ctrl = new Controller(run, thread, _id, i);
            GameGUI win = new GameGUI(i, ctrl);
            run.set_win(win);
            ctrl.set_win(win);
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
            win.setVisible(false);
        }
    }
}