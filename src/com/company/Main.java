package com.company;

import com.company.game.Game;

public class Main {

    public static void main(String[] args) {
        Game newGame = new Game(Game.Players.AlphaBeta, 2, Game.Players.User, 2);
        newGame.run();
        System.out.println(newGame.getWinner().toString() + " wins!");
    }
}
