package com.company.game;

import com.company.agents.GameAgent;
import com.company.enums.Player;
import com.company.moves.Move;

import java.lang.reflect.InvocationTargetException;

public class Game {

    private GameAgent firstAgent;
    private GameAgent secondAgent;
    private Board board;
    private Player winner;

    public <T extends GameAgent> Game(Class<T> firstAgent, Class<T> secondAgent){
        board = new Board();
        try {
            this.firstAgent = firstAgent.getDeclaredConstructor(Board.class, Player.class)
                    .newInstance(board, Player.FIRST_PLAYER);
            this.secondAgent = secondAgent.getDeclaredConstructor(Board.class, Player.class)
                    .newInstance(board, Player.SECOND_PLAYER);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e){
            e.printStackTrace();
        }
    }

    public void run(){
        while (true) {
            board.printBoard();
            Move nextMove = firstAgent.nextMove();
            nextMove.admit();
            if (winnerFound()) return;
            board.printBoard();
            nextMove = secondAgent.nextMove();
            nextMove.admit();
            if (winnerFound()) return;
        }
    }

    public Player getWinner(){
        return winner;
    }

    private boolean winnerFound(){
         if (board.getPlayerPosition(Player.FIRST_PLAYER).getRow() == 8){
             winner = Player.FIRST_PLAYER;
             return true;
         } else if (board.getPlayerPosition(Player.SECOND_PLAYER).getRow() == 0){
             winner = Player.SECOND_PLAYER;
             return true;
         }
         return false;
    }
}
