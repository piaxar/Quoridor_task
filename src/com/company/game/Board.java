package com.company.game;

import com.company.enums.MoveDirection;
import com.company.enums.Player;
import com.company.enums.WallDirection;
import com.company.moves.Move;
import com.company.moves.PawnMove;
import com.company.moves.WallMove;

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
    private int[] wallsLeft;
    private final int WALLS = 10;

    public Board() {
        board = new Cell[9][9];
        playerPositions = new Cell[2];
        wallsLeft = new int[]{WALLS, WALLS};

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

    public boolean inWinningPosition(Player player){
        if (player == FIRST_PLAYER && getPlayerPosition(FIRST_PLAYER).getRow() == 8) return true;
        if (player == SECOND_PLAYER && getPlayerPosition(SECOND_PLAYER).getRow() == 0) return true;
        return false;
    }

    public Cell[][] getBoard(){
        return board;
    }

    /**
     * As we have same board for all findingPath trials, we need to clear weights in all cells
     */
    private void clearWeights(){
        for (Cell[] c: board){
            for (Cell cc: c){
                cc.setCost(Integer.MAX_VALUE);
            }
        }
    }

    public void setPlayerPosition(Player player, int row, int column){
        playerPositions[player.index()] = board[row][column];
    }


    public LinkedList<PawnMove> getAvailablePawnMoves(Player player){
        LinkedList<PawnMove> availableMoves = new LinkedList<>();

        int row = getPlayerPosition(player).getRow();
        int column = getPlayerPosition(player).getColumn();
        // add all possible pawn moves
        for (int i = -1; i < 2; i++){
            for (int j = -1; j < 2; j++){
                PawnMove nm = new PawnMove(player, this, row + i, column + j);
                if (nm.isValid()){
                    availableMoves.add(nm);
                }
            }
        }
        PawnMove jumpMove = new PawnMove(player, this, row + 2, column);
        if (jumpMove.isValid()) availableMoves.add(jumpMove);
        jumpMove = new PawnMove(player, this, row + -2, column);
        if (jumpMove.isValid()) availableMoves.add(jumpMove);
        jumpMove = new PawnMove(player, this, row, column + 2);
        if (jumpMove.isValid()) availableMoves.add(jumpMove);
        jumpMove = new PawnMove(player, this, row, column - 2);
        if (jumpMove.isValid()) availableMoves.add(jumpMove);

        return availableMoves;

    }
    public LinkedList<WallMove> getAvailableWallMoves(Player player){
        LinkedList<WallMove> availableMoves = new LinkedList<>();

        // add all possible wall moves
        for (int i = 0; i < 9; i++){
            for (int j = 0; j < 9; j++){
                for (WallDirection wd: WallDirection.values()){
                    WallMove nm = new WallMove(this, player, i, j, wd);
                    if (nm.isValid()){
                        availableMoves.add(nm);
                    }
                }
            }
        }
        return availableMoves;
    }
    public LinkedList<Move> getAvailableMoves(Player player){
        LinkedList<Move> moves = new LinkedList<>();
        moves.addAll(getAvailablePawnMoves(player));
        moves.addAll(getAvailableWallMoves(player));
        return moves;
    }

    public int getWallsLeft(Player player){
        return wallsLeft[player.index()];
    }

    public void incrementWalls(Player player){
        wallsLeft[player.index()]++;
    }

    public void decrementWalls(Player player){
        wallsLeft[player.index()]--;
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
        return (shortestPath(FIRST_PLAYER) != -1 && shortestPath(SECOND_PLAYER) != -1);
    }

    public int shortestPath(Player p){
        clearWeights();
        int winningRow = p == FIRST_PLAYER ? 8:0;
        if (playerPositions[p.index()].getRow() == winningRow){
            return 0;
        }
        Queue<Cell> openCells = new LinkedList<>();
        Cell startingPoint = getPlayerPosition(p);
        startingPoint.setCost(0);
        openCells.add(startingPoint);
        while (openCells.size() > 0){
            Cell toGo = openCells.poll();
            Cell[] neighbours = toGo.getNeighbours();

            for (Cell c: neighbours){
                if (c!= null){

                    if (c.getRow() == winningRow){
                        return toGo.getCost() + 1;

                    } else {
                        if (c.getCost() == Integer.MAX_VALUE) {
                            c.setCost(toGo.getCost() + 1);
                            openCells.add(c);
                        }
                    }
                }
            }
        }
        return -1;
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


    public Player getAnotherPlayer(Player currentPlayer) {
        return currentPlayer == FIRST_PLAYER ? SECOND_PLAYER:FIRST_PLAYER;
    }
}
