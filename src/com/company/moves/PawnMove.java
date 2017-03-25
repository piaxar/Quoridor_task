package com.company.moves;

import com.company.Board;
import com.company.Cell;
import com.company.enums.MoveDirection;
import com.company.enums.Player;

/**
 * Created by dell on 24.03.17.
 */
public class PawnMove extends Move{
    private Player player;
    private Board board;
    private int rowFrom;
    private int rowTo;
    private int columnFrom;
    private int columnTo;

    private Cell from;
    private Cell to;


    public PawnMove(Player player, Board board, int rowTo, int columnTo) {
        this.player = player;
        this.board = board;
        this.rowTo = rowTo;
        this.columnTo = columnTo;
        from = board.getPlayerPosition(player);
        if (Board.cellValid(rowTo, columnTo)) {
            to = board.getBoard()[rowTo][columnTo];
        }
        rowFrom = from.getRow();
        columnFrom = from.getColumn();
    }

    @Override
    public Boolean isValid() {
        // validity checking
        if (!Board.cellValid(rowTo, columnTo)){
            return false;
        }
        // occupation checking
        if (board.getPlayerPosition(player) == board.getAnotherPlayerPosition(player)){
            return false;
        }
        // if aim cell is one of the neighbours
        for (Cell c:from.getNeighbours()){
            if (c != null){
                if (c == to) return true;
            }
        }
        // check if move was a straight jump
        MoveDirection direction = null;
        if (rowTo == rowFrom && Math.abs(columnFrom - columnTo) == 2){
            direction = columnTo > columnFrom ? MoveDirection.RIGHT : MoveDirection.LEFT;
        } else if (columnTo == columnFrom && Math.abs(rowFrom - rowTo) == 2){
            direction = rowTo > rowFrom ? MoveDirection.UP : MoveDirection.DOWN;
        }
        if (direction != null){
            // pawn is in the given position
            if (from.getNeighbours()[direction.index()] == board.getAnotherPlayerPosition(player)){
                if (from.getNeighbours()[direction.index()].getNeighbours()[direction.index()] != null){
                    return true;
                }
            } else {
                // none was jumped over or there is a wall
                return false;
            }
        }

        // if it is not a straight jump
        if (Math.abs(rowFrom - rowTo) == 1 && Math.abs(columnFrom - columnTo) == 1){
            int directionIndex = -1;
            // check if there is pawn near current cell
            for (int i = 0; i < 4; i++){
                if (from.getNeighbours()[i] == board.getAnotherPlayerPosition(player)){
                    directionIndex = i;
                }
            }
            if (directionIndex == -1){
                // no pawn fond
                return false;
            } else {
                // check a wall right behind the pawn
                if (from.getNeighbours()[directionIndex].getNeighbours()[directionIndex] != null){
                    return false;
                } else {
                    // check possible cells
                    for (int i = 0; i < 4; i++){
                        if (from.getNeighbours()[directionIndex].getNeighbours()[i] == to && to != from){
                            return true;
                        }
                    }
                }
            }
        }

        return false;

    }

    @Override
    public void admit() {
        board.setPlayerPosition(player, rowTo, columnTo);
    }

    @Override
    public void rollback() {
        board.setPlayerPosition(player, rowFrom, columnFrom);
    }
}
