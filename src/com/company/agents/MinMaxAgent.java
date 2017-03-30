package com.company.agents;

import com.company.game.Board;
import com.company.enums.Player;
import com.company.enums.WallDirection;
import com.company.moves.Move;
import com.company.moves.WallMove;

public class MinMaxAgent extends GameAgent{

    public MinMaxAgent(Board board, Player player) {
        super(board, player);
    }

    @Override
    public Move nextMove(){
        return new WallMove(board, player,1, 1, WallDirection.horizontal);
    }

    class BoardState{
        Board board;
        Move move;

        /**
         *
         * @param board - board to perform actions on
         * @param move - move to get this state
         */
        BoardState(Board board, Move move){
            this.board = board;
            this.move = move;
        }
    }

}
