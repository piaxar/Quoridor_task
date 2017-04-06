package com.company;
import com.company.game.Game;

public class Main {

    public static void main(String[] args) {

        // Start new game by creating an instance of class Game
        // Players can be User, MinMax, AlphaBeta, MCST
        // For User depth doesn't matter
        // for MinMax and AlphaBeta depth is depth of tree
        // for MCST depth is number of iterations (use about 2000 for smart playing)

        Game newGame = new Game(Game.Players.MCST, 5000, Game.Players.MinMax, 2);
        newGame.run();
        System.out.println("Winner is: " + newGame.getWinner().toString());
    }
}