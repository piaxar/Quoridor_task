package com.company;

import com.company.agents.UserAgent;
import com.company.game.Game;

public class Main {

    public static void main(String[] args) {
        Game newGame = new Game(UserAgent.class, UserAgent.class);
        newGame.run();
        System.out.println(newGame.getWinner().toString() + " wins!");
    }
}
