package com.company.game;

import com.company.agents.GameAgent;
import com.company.agents.MinMaxAgent;
import com.company.agents.UserAgent;
import com.company.enums.Player;
import com.company.moves.Move;

import java.lang.reflect.InvocationTargetException;

public class Game {

    public enum Players{
        MinMax, User
    }
    private GameAgent firstAgent;
    private GameAgent secondAgent;
    private Board board;
    private Player winner;

    public Game(Players firstAgent, Players secondAgent){
        board = new Board();
        switch (firstAgent){
            case MinMax:
                this.firstAgent = new MinMaxAgent(board, Player.FIRST_PLAYER, Player.SECOND_PLAYER, 2);
                break;
            case User:
                this.firstAgent = new UserAgent(board, Player.FIRST_PLAYER, Player.SECOND_PLAYER);
                break;
        }
        switch (secondAgent){
            case MinMax:
                this.secondAgent = new MinMaxAgent(board, Player.SECOND_PLAYER, Player.FIRST_PLAYER, 2);
                break;
            case User:
                this.secondAgent = new UserAgent(board, Player.SECOND_PLAYER, Player.FIRST_PLAYER);
                break;
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
