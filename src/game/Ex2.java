package game;


public class Ex2 {

    public static Runner _run;
    private static Thread _thread;
    private static int _id = 205474026, _level = 19;

    public static void main(String[] args) {
        try {
            _id = Integer.parseInt(args[0]);
            _level = Integer.parseInt(args[1]);
        } catch (Exception ignored) {
        }
        _run = new Runner(_level, _id);
        _thread = new Thread(_run);
        Controller ctrl = new Controller(_run, _thread, _id, _level);
        GameGUIPlus win = new GameGUIPlus(_level, ctrl);
        _run.set_win(win);
        ctrl.set_win(win);
        _thread.start();
    }
}
