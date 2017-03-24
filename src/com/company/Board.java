package com.company;

import com.company.enums.Direction;
import com.company.enums.WallDirection;
import com.company.exceptions.InvalidMoveException;
import com.company.exceptions.InvalidWallPlacement;

import java.util.ArrayList;

import static com.company.enums.Direction.*;

/**
 * Created by dell on 08.03.17.
 */
public class Board {

    private Cell[][] board;
    private Cell firstPlayerPosition;
    private Cell secondPlayerPosition;
    private ArrayList<Wall> walls;

    /**
     * Structure that represents wall on the board
     */
    private class Wall {
        public final WallDirection direction;
        public final Cell anchor;

        /**
         * Create new wall. Logic with deleting connections between cells is inside.
         *
         * @param anchor
         * @param direction
         */
        Wall(Cell anchor, WallDirection direction) {
            this.anchor = anchor;
            this.direction = direction;
            int anchorRow = anchor.getRow();
            int anchorColumn = anchor.getColumn();

            if (direction == WallDirection.horizontal) {
                board[anchorRow][anchorColumn].setNeighbour(DOWN, null);
                board[anchorRow][anchorColumn + 1].setNeighbour(DOWN, null);
                board[anchorRow - 1][anchorColumn].setNeighbour(UP, null);
                board[anchorRow - 1][anchorColumn + 1].setNeighbour(UP, null);
            } else {
                board[anchorRow][anchorColumn].setNeighbour(LEFT, null);
                board[anchorRow - 1][anchorColumn].setNeighbour(LEFT, null);
                board[anchorRow][anchorColumn - 1].setNeighbour(RIGHT, null);
                board[anchorRow - 1][anchorColumn - 1].setNeighbour(RIGHT, null);
            }
        }
    }

    public Board() {
        board = new Cell[9][9];
        walls = new ArrayList<>(20);

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                board[i][j] = new Cell(i, j);
            }
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 9; j++) {
                board[i][j].setNeighbour(UP, board[i + 1][j]);
            }
        }
        for (int i = 1; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                board[i][j].setNeighbour(DOWN, board[i - 1][j]);
            }
        }
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j].setNeighbour(RIGHT, board[i][j + 1]);
            }
        }
        for (int i = 0; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                board[i][j].setNeighbour(LEFT, board[i][j - 1]);
            }
        }

        firstPlayerPosition = board[0][4];
        secondPlayerPosition = board[8][4];
    }

    public Cell getFirstPlayerPosition() {
        return firstPlayerPosition;
    }

    /**
     * Sets coordinates of first player's pawn
     * Checks if coordinates are valid and cell is not occupied
     * No other checking is done
     *
     * @param row    number
     * @param column number
     * @throws InvalidMoveException     if position is occupied
     * @throws IllegalArgumentException if coordinates are invalid ( > 8 or < 0)
     */
    public void setFirstPlayerPosition(int row, int column) throws InvalidMoveException {
        checkValidCell(row, column);
        if (secondPlayerPosition.getRow() == row && secondPlayerPosition.getColumn() == column) {
            throw new InvalidMoveException("Cell " + row + ":" + column + " is occupied by second player");
        }
        firstPlayerPosition = board[row][column];
    }

    public Cell getSecondPlayerPosition() {
        return secondPlayerPosition;
    }

    /**
     * Sets coordinates of second player's pawn
     * Checks if coordinates are valid and cell is not occupied
     * No other checking is done
     *
     * @param row    number
     * @param column number
     * @throws InvalidMoveException     if position is occupied
     * @throws IllegalArgumentException if coordinates are invalid ( > 8 or < 0)
     */
    public void setSecondPlayerPosition(int row, int column) throws InvalidMoveException {
        checkValidCell(row, column);
        if (firstPlayerPosition.getRow() == row && firstPlayerPosition.getColumn() == column) {
            throw new InvalidMoveException("Cell " + row + ":" + column + " is occupied by first player");
        }
        secondPlayerPosition = board[row][column];
    }

    /**
     * Check if coordinates belongs to the board
     * @param row of the cell
     * @param column of the cell
     * @throws IllegalArgumentException if cell doesn't belong to the board
     */
    public void checkValidCell(int row, int column) throws IllegalArgumentException {
        if (row > 8 || row < 0) {
            throw new IllegalArgumentException("Row must be >= 0 and <=8. Found: " + row);
        } else if (column > 8 || column < 0) {
            throw new IllegalArgumentException("Column must be >= 0 and <=8. Found: " + column);
        }
    }

    /**
     * Places the wall at the anchor at coordinates
     * @param row coordinate
     * @param column coordinate
     * @param direction of wall placement
     * @throws InvalidWallPlacement if Wall intersects with another wall
     */
    public void setWall (int row, int column, WallDirection direction) throws InvalidWallPlacement {
        setWall(board[row][column], direction);
    }

    /**
     * Wall is placed on the left and down from given anchor, if Direction.vertical
     * Wall is placed below and to the right from given anchor, if Direction.horizontal
     *
     * @param anchor
     * @param direction
     * @throws InvalidWallPlacement     if Wall intersects with another wall
     * @throws IllegalArgumentException if cells have illegal values to place wall
     */
    public void setWall(Cell anchor, WallDirection direction) throws InvalidWallPlacement,
            IllegalArgumentException {
        // Argument validity checking
        checkValidCell(anchor.getRow(), anchor.getColumn());
        if ((direction == WallDirection.horizontal && anchor.getColumn() > 7) ||
                (direction == WallDirection.vertical && anchor.getRow() < 1)) {
            throw new IllegalArgumentException("Can't place wall from given position " +
                    anchor.getRow() + ":" + anchor.getColumn());
        }
        // We can't place walls on the border
        if ((direction == WallDirection.horizontal && anchor.getRow() < 1) ||
                (direction == WallDirection.vertical && anchor.getColumn() < 1)){
            throw  new InvalidWallPlacement("Can't place wall on border of the board");
        }
        // Intersections checking
        int nRow = anchor.getRow();
        int nCol = anchor.getColumn();
        for (Wall w : walls) {
            int wRow = w.anchor.getRow();
            int wCol = w.anchor.getColumn();

            if (anchor == w.anchor && direction == w.direction) {
                // Wall is placed on the already existing wall
                throw new InvalidWallPlacement("Such wall already exists");
            } else if (w.direction == direction) {
                // Wall is overlapped already placed wall
                if (direction == WallDirection.horizontal && nRow == wRow && Math.abs(nCol - wCol) == 1) {
                    throw new InvalidWallPlacement(getWallPlacementErrorMessage(nRow, nCol, direction, w));
                } else if (direction == WallDirection.vertical &&nCol == wCol && Math.abs(nRow - wRow) == 1) {
                    throw new InvalidWallPlacement(getWallPlacementErrorMessage(nRow, nCol, direction, w));
                }
            } else if (nRow == wRow && Math.abs(nCol - wCol) == 1) {
                // Wall intersects already existing wall
                if (nCol > wCol && direction == WallDirection.vertical) {
                    throw new InvalidWallPlacement(getWallPlacementErrorMessage(nRow, nCol, direction, w));
                } else if (nCol < wCol && direction == WallDirection.horizontal){
                    throw new InvalidWallPlacement(getWallPlacementErrorMessage(nRow, nCol, direction, w));
                }
            }
        }
        // everything is fine; Place wall and add it
        Wall w = new Wall(anchor, direction);
        walls.add(w);
    }

    /**
     * Delete last placed wall. Class board doesn't check if after placement there is a path
     * for pawn, but Game class does. So if wall was placed wrong, delete it.
     */
    public void deleteLastWall(){
        Wall w = walls.remove(walls.size() -1);
        int anchorRow = w.anchor.getRow();
        int anchorColumn = w.anchor.getColumn();
        if (w.direction == WallDirection.horizontal){
            board[anchorRow][anchorColumn].setNeighbour(DOWN, board[anchorRow - 1][anchorColumn]);
            board[anchorRow][anchorColumn + 1].setNeighbour(DOWN, board[anchorRow - 1][anchorColumn + 1]);
            board[anchorRow - 1][anchorColumn].setNeighbour(UP, board[anchorRow][anchorColumn]);
            board[anchorRow - 1][anchorColumn + 1].setNeighbour(UP, board[anchorRow][anchorColumn + 1]);
        } else {
            board[anchorRow][anchorColumn].setNeighbour(LEFT, board[anchorRow][anchorColumn - 1]);
            board[anchorRow - 1][anchorColumn].setNeighbour(LEFT,  board[anchorRow - 1][anchorColumn - 1]);
            board[anchorRow][anchorColumn - 1].setNeighbour(RIGHT, board[anchorRow][anchorColumn]);
            board[anchorRow - 1][anchorColumn - 1].setNeighbour(RIGHT, board[anchorRow - 1][anchorColumn]);
        }
    }

    /**
     * Generates error message output
     * @param nrow
     * @param ncol
     * @param direction
     * @param w
     * @return error message
     */
    private String getWallPlacementErrorMessage(int nrow, int ncol, WallDirection direction, Wall w){
        return "Wall "+nrow+":"+ncol+":"+direction.toString()+
                " intersects with "+w.anchor.getRow()+":"+w.anchor.getColumn()+":"+w.direction.toString();
    }

    /**
     * Print board
     */
    public void printBoard(){
        System.out.println(" |=================|");
        for (int i= 8; i >= 0; i--){
            System.out.print(i+"|");
            for (int j = 0; j <9; j++){
                if (board[i][j] == firstPlayerPosition){
                    System.out.print("1");
                } else if (board[i][j] == secondPlayerPosition) {
                    System.out.print("2");
                } else {
                    System.out.print("*");
                }
                if (board[i][j].getNeighbours()[Direction.RIGHT.index()] == null){
                    System.out.print("|");
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println();
            System.out.print(" |");
            for (int j = 0; j < 9; j++){
                if (j == 8){
                    if (board[i][j].getNeighbours()[Direction.DOWN.index()] == null){
                        System.out.print("=");
                    } else {
                        System.out.print(" ");
                    }
                } else {
                    if (board[i][j].getNeighbours()[Direction.DOWN.index()] == null) {
                        System.out.print("= ");
                    } else {
                        System.out.print("  ");
                    }
                }
            }
            System.out.println("|");
        }
        System.out.println("  0 1 2 3 4 5 6 7 8 ");
    }


}
