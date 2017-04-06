package com.company.moves;

import com.company.game.Board;
import com.company.game.Cell;
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
    public Boolean isLeaf;

    private Cell from;
    private Cell to;

    @Override
    public String toString() {
        return player.toString() + " from "+rowFrom+":"+columnFrom+" to "+rowTo+":"+columnTo;
    }

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

        if (player == Player.FIRST_PLAYER && rowTo == 8) isLeaf = true; else
        if (player == Player.SECOND_PLAYER && rowTo == 0) isLeaf = true; else
            isLeaf = false;
    }

    @Override
    public Boolean isValid() {
        // validity checking
        if (!Board.cellValid(rowTo, columnTo)){
            return false;
        }
        // occupation checking
        if (rowTo == board.getAnotherPlayerPosition(player).getRow() &&
                columnTo == board.getAnotherPlayerPosition(player).getColumn()){
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

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!PawnMove.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final PawnMove other = (PawnMove) obj;
        if (this.rowFrom != other.rowFrom ||
                this.rowTo != other.rowTo ||
                this.columnFrom != other.columnFrom ||
                this.columnTo != other.columnTo) {
            return false;
        }
        if (this.player != other.player) {
            return false;
        }
        return true;
    }
}
