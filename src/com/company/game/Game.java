package com.company.game;

import com.company.agents.*;
import com.company.enums.Player;
import com.company.moves.Move;
import com.company.moves.WallMove;

public class Game {

    public enum Players {
        MinMax, User, AlphaBeta, MCST
    }

    private GameAgent firstAgent;
    private GameAgent secondAgent;
    private Board board;
    private Player winner;

    public Game(Players firstAgent, int firstAgentDepth, Players secondAgent, int secondAgentDepth) {
        board = new Board();
        switch (firstAgent) {
            case User:
                this.firstAgent = new UserAgent(board, Player.FIRST_PLAYER, Player.SECOND_PLAYER);
                break;
            case MinMax:
                this.firstAgent = new MinMaxAgent(board, Player.FIRST_PLAYER, Player.SECOND_PLAYER, firstAgentDepth);
                break;
            case AlphaBeta:
                this.firstAgent = new AlphaBetaAgent(board, Player.FIRST_PLAYER, Player.SECOND_PLAYER, firstAgentDepth);
                break;
            case MCST:
                this.firstAgent = new MonteCarloAgent(board, Player.FIRST_PLAYER, Player.SECOND_PLAYER, firstAgentDepth);
                break;
        }
        switch (secondAgent) {
            case User:
                this.secondAgent = new UserAgent(board, Player.SECOND_PLAYER, Player.FIRST_PLAYER);
                break;
            case MinMax:
                this.secondAgent = new MinMaxAgent(board, Player.SECOND_PLAYER, Player.FIRST_PLAYER, secondAgentDepth);
                break;
            case AlphaBeta:
                this.secondAgent = new AlphaBetaAgent(board, Player.SECOND_PLAYER, Player.FIRST_PLAYER, secondAgentDepth);
                break;
            case MCST:
                this.secondAgent = new MonteCarloAgent(board, Player.SECOND_PLAYER, Player.FIRST_PLAYER, secondAgentDepth);

        }
    }

    public void run() {
        while (true) {
            board.printBoard();
            Move nextMove = firstAgent.nextMove();
            System.out.println(nextMove.toString());
            nextMove.admit();
            if (winnerFound()) {
                WallMove.clearAdmittedWallMoves();
                return;
            }
            board.printBoard();
            nextMove = secondAgent.nextMove();
            System.out.println(nextMove.toString());
            nextMove.admit();
            if (winnerFound()) {
                WallMove.clearAdmittedWallMoves();
                return;
            }
        }
    }

    public Player getWinner() {
        return winner;
    }

    private boolean winnerFound() {
        if (board.getPlayerPosition(Player.FIRST_PLAYER).getRow() == 8) {
            winner = Player.FIRST_PLAYER;
            return true;
        } else if (board.getPlayerPosition(Player.SECOND_PLAYER).getRow() == 0) {
            winner = Player.SECOND_PLAYER;
            return true;
        }
        return false;
    }
}
