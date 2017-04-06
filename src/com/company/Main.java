package com.company;

import com.company.game.Game;

public class Main {

    public static void main(String[] args) {
        Game newGame = new Game(Game.Players.MCST, 5, Game.Players.MinMax, 2);
        newGame.run();
        System.out.println(newGame.getWinner().toString() + " wins!");
    }
}
