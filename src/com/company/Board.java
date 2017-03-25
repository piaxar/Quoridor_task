package com.company;

import com.company.enums.MoveDirection;
import com.company.enums.Player;
import com.company.exceptions.InvalidMoveException;
import com.company.moves.WallMove;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import static com.company.enums.MoveDirection.*;
import static com.company.enums.Player.*;

/**
 * Created by dell on 08.03.17.
 */
public class Board {

    private Cell[][] board;
    private Cell[] playerPositions;

    public Board() {
        board = new Cell[9][9];
        playerPositions = new Cell[2];

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

        playerPositions[FIRST_PLAYER.index()] = board[0][4];
        playerPositions[SECOND_PLAYER.index()] = board[8][4];
    }

    public Cell[][] getBoard(){
        return board;
    }

    public void setPlayerPosition(Player player, int row, int column){
        playerPositions[player.index()] = board[row][column];
    }

    public void setPlayerPosition(Player player, Cell position){
        playerPositions[player.index()] = position;
    }

    /**
     *
     * @param player
     * @return Cell - position of provided player
     */
    public Cell getPlayerPosition(Player player){
        return playerPositions[player.index()];
    }

    /**
     *
     * @param player
     * @return Cell - position of another player
     */
    public Cell getAnotherPlayerPosition(Player player){
        if (player == FIRST_PLAYER){
            return getPlayerPosition(SECOND_PLAYER);
        }
        return getPlayerPosition(FIRST_PLAYER);
    }

    /**
     * Check if coordinates belongs to the board
     * @param row of the cell
     * @param column of the cell
     * @return true if cell belongs to board, false otherwise
     */
    public static Boolean cellValid(int row, int column) {
        if (row > 8 || row < 0) {
            return false;
        } else if (column > 8 || column < 0) {
            return false;
        }
        return true;
    }

    public boolean pathExists(){
        if (!pathExistsFor(FIRST_PLAYER, 8)){
            return false;
        }
        if (!pathExistsFor(SECOND_PLAYER, 0)){
            return false;
        }
        return true;
    }

    private boolean pathExistsFor(Player p, int winningRow){
        Queue<Cell> openCells = new LinkedList<>();
        LinkedList<Cell> visitedCells = new LinkedList();
        openCells.add(getPlayerPosition(p));
        while (openCells.size() > 0){
            Cell toGo = openCells.poll();
            visitedCells.add(toGo);
            Cell[] neighbours = toGo.getNeighbours();
            for (Cell c: neighbours){
                if (c!= null){
                    if (c.getRow() == winningRow){
                        return true;
                    } else {
                        if (!visitedCells.contains(c)) {
                            openCells.add(c);
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Print board
     */
    public void printBoard(){
        System.out.println(" |=================|");
        for (int i= 8; i >= 0; i--){
            System.out.print(i+"|");
            for (int j = 0; j <9; j++){
                if (board[i][j] == playerPositions[FIRST_PLAYER.index()]){
                    System.out.print("1");
                } else if (board[i][j] == playerPositions[SECOND_PLAYER.index()]) {
                    System.out.print("2");
                } else {
                    System.out.print("*");
                }
                if (board[i][j].getNeighbours()[MoveDirection.RIGHT.index()] == null){
                    System.out.print("|");
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println();
            System.out.print(" |");
            for (int j = 0; j < 9; j++){
                if (j == 8){
                    if (board[i][j].getNeighbours()[MoveDirection.DOWN.index()] == null){
                        System.out.print("=");
                    } else {
                        System.out.print(" ");
                    }
                } else {
                    if (board[i][j].getNeighbours()[MoveDirection.DOWN.index()] == null) {
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
