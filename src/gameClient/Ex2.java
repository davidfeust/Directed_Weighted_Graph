package gameClient;


import Server.Game_Server_Ex2;
import trys.GUI_102;
import api.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Ex2 {

    public static void main(String[] args) {
        game_service game = Game_Server_Ex2.getServer(1); // you have [0,23] games
        game.startGame();
        String g = game.getGraph();
        String fs = game.getPokemons();
        directed_weighted_graph gg = game.getJava_Graph_Not_to_be_used();
        //gg.init(g);
        Arena _ar = new Arena();
        _ar.setGraph(gg);
        _ar.setPokemons(Arena.json2Pokemons(fs));
        GameGUI _win = new GameGUI(1);
        _win.setSize(1000, 700);
        _win.set_ar(_ar);

        _win.show();

//
    }

}
