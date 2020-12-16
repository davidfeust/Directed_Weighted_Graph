package gameClient;

public class Ex2 {

    /**
     * Main of the Game Pokemons.
     * starts new Game with the giving id and level from args
     *
     * @param args args[0]= id, args[1]= level.
     */
    public static void main(String[] args) {
        int _id, _level;
        try {
            _id = Integer.parseInt(args[0]);
            _level = Integer.parseInt(args[1]);
        } catch (Exception ignored) {
            _id = -1;
            _level = 0;
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
