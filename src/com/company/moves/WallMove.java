package com.company.moves;

import com.company.game.Board;
import com.company.game.Cell;
import com.company.enums.Player;
import com.company.enums.WallDirection;

import java.util.ArrayList;

import static com.company.enums.MoveDirection.*;

/**
 * Created by dell on 24.03.17.
 */
public class WallMove extends Move {
    private Board board;
    private int anchorRow;
    private int anchorColumn;
    private WallDirection direction;
    private Player player;
    private static ArrayList<WallMove> admittedWallMoves;

    public static void clearAdmittedWallMoves(){
        admittedWallMoves = new ArrayList<>();
    }

    public WallMove(Board board, Player player, int anchorRow, int anchorColumn, WallDirection direction) {
        this.anchorRow = anchorRow;
        this.anchorColumn = anchorColumn;
        this.direction = direction;
        this.board = board;
        this.player = player;

        if (admittedWallMoves == null){
            admittedWallMoves = new ArrayList<>(20);
        }
    }

    /**
     * WallPlacementMove validity check
     * @return true if is valid, false otherwise
     */
    @Override
    public Boolean isValid(){
        if (board.getWallsLeft(player) == 0) return false;
        if (!Board.cellValid(anchorRow, anchorColumn)){
            return false;
        }
        // check if wall intersects the outline of the board
        if ((direction == WallDirection.horizontal && anchorColumn > 7) ||
                (direction == WallDirection.vertical && anchorRow < 1)) {
            //System.out.println("Wall intersects the board line");
            return false;
        }
        // check if wall doesn't place on the board
        if ((direction == WallDirection.horizontal && anchorRow < 1) ||
                (direction == WallDirection.vertical && anchorColumn < 1)){
            //System.out.println("Can't place wall on border of the board");
            return false;
        }
        // intersections checking:
        for (WallMove w : admittedWallMoves) {
            int wRow = w.anchorRow;
            int wCol = w.anchorColumn;

            if (anchorRow == wRow && anchorColumn == wCol && direction == w.direction) {
                // Wall is placed on the already existing wall
                //System.out.println("Such wall already exists");
                return false;
            } else if (w.direction == direction) {
                // Wall is overlapped already placed wall
                if (direction == WallDirection.horizontal && anchorRow == wRow && Math.abs(anchorColumn - wCol) == 1) {
                    return false;
                } else if (direction == WallDirection.vertical &&anchorColumn == wCol && Math.abs(anchorRow - wRow) == 1) {
                    return false;
                }
            } else if (anchorRow == wRow && Math.abs(anchorColumn - wCol) == 1) {
                // Wall intersects already existing wall
                if (anchorColumn > wCol && direction == WallDirection.vertical) {
                    return false;
                } else if (anchorColumn < wCol && direction == WallDirection.horizontal){
                    return false;
                }
            }
        }

        admit();
        if (!board.pathExists()){
            rollback();
            return false;
        }
        rollback();
        // everything is fine
        return true;
    }

    @Override
    public void admit(){
        Cell[][] mBoard = board.getBoard();
        if (direction == WallDirection.horizontal) {
            mBoard[anchorRow][anchorColumn].setNeighbour(DOWN, null);
            mBoard[anchorRow][anchorColumn + 1].setNeighbour(DOWN, null);
            mBoard[anchorRow - 1][anchorColumn].setNeighbour(UP, null);
            mBoard[anchorRow - 1][anchorColumn + 1].setNeighbour(UP, null);
        } else {
            mBoard[anchorRow][anchorColumn].setNeighbour(LEFT, null);
            mBoard[anchorRow - 1][anchorColumn].setNeighbour(LEFT, null);
            mBoard[anchorRow][anchorColumn - 1].setNeighbour(RIGHT, null);
            mBoard[anchorRow - 1][anchorColumn - 1].setNeighbour(RIGHT, null);
        }
        admittedWallMoves.add(this);
        board.decrementWall(player);
    }

    @Override
    public void rollback(){
        Cell[][] mBoard = board.getBoard();
        if (direction == WallDirection.horizontal){
            mBoard[anchorRow][anchorColumn].setNeighbour(DOWN, mBoard[anchorRow - 1][anchorColumn]);
            mBoard[anchorRow][anchorColumn + 1].setNeighbour(DOWN, mBoard[anchorRow - 1][anchorColumn + 1]);
            mBoard[anchorRow - 1][anchorColumn].setNeighbour(UP, mBoard[anchorRow][anchorColumn]);
            mBoard[anchorRow - 1][anchorColumn + 1].setNeighbour(UP, mBoard[anchorRow][anchorColumn + 1]);
        } else {
            mBoard[anchorRow][anchorColumn].setNeighbour(LEFT, mBoard[anchorRow][anchorColumn - 1]);
            mBoard[anchorRow - 1][anchorColumn].setNeighbour(LEFT,  mBoard[anchorRow - 1][anchorColumn - 1]);
            mBoard[anchorRow][anchorColumn - 1].setNeighbour(RIGHT, mBoard[anchorRow][anchorColumn]);
            mBoard[anchorRow - 1][anchorColumn - 1].setNeighbour(RIGHT, mBoard[anchorRow - 1][anchorColumn]);
        }
        admittedWallMoves.remove(this);
        board.incrementWalls(player);
    }
}
