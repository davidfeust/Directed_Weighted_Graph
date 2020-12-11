package game;

public class Ex2 {

    private static int _id = 205474026, _level = 1;

    public static void main(String[] args) {
        try {
            _id = Integer.parseInt(args[0]);
            _level = Integer.parseInt(args[1]);
        } catch (Exception ignored) {
        }

        Runner run = new Runner(_level, _id);
        Thread thread = new Thread(run);
        Controller ctrl = new Controller(run, thread, _id, _level);
        GameGUI win = new GameGUI(_level, ctrl);
        run.set_win(win);
        ctrl.set_win(win);
        thread.start();
    }
}
