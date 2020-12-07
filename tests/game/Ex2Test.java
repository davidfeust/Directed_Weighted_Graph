package game;

import org.junit.jupiter.api.Test;

import java.util.Stack;
import java.util.StringJoiner;

import static org.junit.jupiter.api.Assertions.*;

class Ex2Test {

    @Test
    void runAllGames() {
        String[] args = new String[2];
        args[1] = "205474026";
        for (int i = 0; i < 24; i++) {
            args[0] = i + "";
            Ex2.main(args);
        }
    }
}