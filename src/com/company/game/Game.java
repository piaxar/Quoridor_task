package com.company.game;

import com.company.agents.GameAgent;
import com.company.agents.MinMaxAgent;
import com.company.agents.UserAgent;
import com.company.enums.Player;
import com.company.moves.Move;

public class Game {

    public enum Players{
        MinMax, User, AlphaBeta
    }
    private GameAgent firstAgent;
    private GameAgent secondAgent;
    private Board board;
    private Player winner;

    public Game(Players firstAgent, int firstAgentDepth, Players secondAgent, int secondAgentDepth){
        board = new Board();
        switch (firstAgent){
            case User:
                this.firstAgent = new UserAgent(board, Player.FIRST_PLAYER, Player.SECOND_PLAYER);
                break;
            case MinMax:
                this.firstAgent = new MinMaxAgent(board, Player.FIRST_PLAYER, Player.SECOND_PLAYER, firstAgentDepth);
                break;
        }
        switch (secondAgent){
            case User:
                this.secondAgent = new UserAgent(board, Player.SECOND_PLAYER, Player.FIRST_PLAYER);
                break;
            case MinMax:
                this.secondAgent = new MinMaxAgent(board, Player.SECOND_PLAYER, Player.FIRST_PLAYER, secondAgentDepth);
                break;
        }
    }

    public void run(){
        while (true) {
            board.printBoard();
            Move nextMove = firstAgent.nextMove();
            nextMove.admit();
            System.out.println("length for "+ Player.FIRST_PLAYER.toString()+" = "+board.shortestPath(Player.FIRST_PLAYER));
            System.out.println("length for "+ Player.SECOND_PLAYER.toString()+" = "+board.shortestPath(Player.SECOND_PLAYER));
            if (winnerFound()) return;
            board.printBoard();
            nextMove = secondAgent.nextMove();
            nextMove.admit();
            System.out.println("length for "+ Player.FIRST_PLAYER.toString()+" = "+board.shortestPath(Player.FIRST_PLAYER));
            System.out.println("length for "+ Player.SECOND_PLAYER.toString()+" = "+board.shortestPath(Player.SECOND_PLAYER));
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
