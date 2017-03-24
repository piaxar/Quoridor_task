package com.company;

import com.company.enums.Direction;
import com.company.enums.WallDirection;
import com.company.exceptions.InvalidMoveException;
import com.company.exceptions.InvalidWallPlacement;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by dell on 14.03.17.
 */
public class Game {

    private final int INITIAL_WALLS_NUMBER = 10;
    private Boolean isFinished;
    private Boolean firstPlayerTurn;
    private int movesCount;
    private Player firstPlayer, secondPlayer, winner;
    private Board board;

    private class Player{
        protected String name;
        protected int walls;

        public Player(String name) {
            this.name = name;
            walls = INITIAL_WALLS_NUMBER;
        }

        public Cell getPosition() {
            return board.getFirstPlayerPosition();
        }
    }

    /**
     *
     * @return true if first player turn, false is second
     */
    public boolean isFirstPlayerTurn(){
        return firstPlayerTurn;
    }

    /**
     * Moves pawn of the player, which turn is now.
     * @param direction of moving
     * @throws InvalidMoveException if move is invalid
     */
    public void nextTurnMove(Direction direction) throws InvalidMoveException {
        if (firstPlayerTurn == true){
            player1Move(direction);
        } else {
            player2Move(direction);
        }
        firstPlayerTurn = !firstPlayerTurn;
    }

    /**
     * Place a wall and decrement wall count from current player
     * @param row
     * @param column
     * @param direction
     * @throws InvalidWallPlacement
     */
    public void nextTurnWall(int row, int column, WallDirection direction) throws InvalidWallPlacement {
        if (firstPlayerTurn == true){
            player1PlaceWall(row, column, direction);
        } else {
            player2PlaceWall(row, column, direction);
        }
        firstPlayerTurn = !firstPlayerTurn;
    }

    public Game(String fName, String sName) {
        board = new Board();
        firstPlayer = new Player(fName);
        secondPlayer = new Player(sName);
        firstPlayerTurn = true;
        isFinished = false;
    }

    /**
     * Flag of finishing the game
     * @return true, if game is finished, false otherwise;
     */
    public boolean isFinished(){
        return isFinished;
    }

    /**
     * Checks if there is a winner
     * If Yes - set winner
     */
    private void checkWinning(){
        System.out.println("in chesk winning "+ firstPlayer.getPosition().getRow()+" " + firstPlayer.getPosition().getColumn());
        if (firstPlayer.getPosition().getRow() == 8){
            System.out.println("first wins");
            winner = firstPlayer;
            isFinished = true;
        } else if (secondPlayer.getPosition().getRow() == 0){
            System.out.println("second wins");
            winner = secondPlayer;
            isFinished = true;
        }
    }

    /**
     * Get winner
     * @return winner's name
     */
    public String getWinner() {
        return winner.name;
    }

    /**
     * Move first player's pawn in given direction
     * @param direction
     * @throws InvalidMoveException if move cannot be done
     */
    public void player1Move (Direction direction) throws InvalidMoveException {
        // check if can move in that direction
        Cell supposedCell = board.getFirstPlayerPosition().getNeighbours()[direction.index()];
        if (supposedCell != null){
            if(supposedCell != board.getSecondPlayerPosition()){
                // cell is available
                board.setFirstPlayerPosition(supposedCell.getRow(), supposedCell.getColumn());
            } else {
                // cell is occupied => jump over it
                supposedCell = supposedCell.getNeighbours()[direction.index()];
                // if there is a wall or end of the board
                if (supposedCell == null) throw new InvalidMoveException("Can't jump over another pawn");
                board.setFirstPlayerPosition(supposedCell.getRow(), supposedCell.getColumn());
            }
        } else {
            throw new InvalidMoveException("Can't move pawn in given direction");
        }
        checkWinning();
    }

    /**
     * Move second player's pawn in given direction
     * @param direction
     * @throws InvalidMoveException if move cannot be done
     */
    public void player2Move(Direction direction) throws InvalidMoveException {
        // check if can move in that direction
        Cell supposedCell = board.getSecondPlayerPosition().getNeighbours()[direction.index()];
        if (supposedCell != null){
            if(supposedCell != board.getFirstPlayerPosition()){
                // cell is available
                board.setSecondPlayerPosition(supposedCell.getRow(), supposedCell.getColumn());
            } else {
                // cell is occupied => jump over it
                supposedCell = supposedCell.getNeighbours()[direction.index()];
                // if there is a wall or end of the board
                if (supposedCell == null) throw new InvalidMoveException("Can't jump over another pawn");
                board.setSecondPlayerPosition(supposedCell.getRow(), supposedCell.getColumn());
            }
        } else {
            throw new InvalidMoveException("Can't move pawn in given direction");
        }
        checkWinning();
    }

    /**
     * Place a wall by first player
     * @throws InvalidWallPlacement if wall can't be placed
     */
    public void player1PlaceWall(int row, int column, WallDirection direction) throws InvalidWallPlacement {
        if(firstPlayer.walls > 0){
            board.setWall(row, column, direction);
            if (!pathExists()){
                board.deleteLastWall();
                throw new InvalidWallPlacement("Wall placement causes absence of path");
            } else {
                firstPlayer.walls--;
            }
        } else {
            throw new InvalidWallPlacement("Not enough walls");
        }

    }

    /**
     * Place a wall by second player
     * @throws InvalidWallPlacement if wall can't be placed
     */
    public void player2PlaceWall(int row, int column, WallDirection direction) throws InvalidWallPlacement {
        if(secondPlayer.walls > 0){
            board.setWall(row, column, direction);
            if (!pathExists()){
                board.deleteLastWall();
                throw new InvalidWallPlacement("Wall placement causes absence of path");
            } else {
                secondPlayer.walls--;
            }
        } else {
            throw new InvalidWallPlacement("Not enough walls");
        }
    }

    public boolean pathExists(){
        if (!pathExistsFor(firstPlayer, 8)){
            return false;
        }
        if (!pathExistsFor(secondPlayer, 0)){
            return false;
        }
        return true;
    }

    private boolean pathExistsFor(Player p, int winningRow){
        Queue<Cell> openCells = new LinkedList<>();
        LinkedList<Cell> visitedCells = new LinkedList();
        openCells.add(p.getPosition());
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

    public Board getBoard(){
        return board;
    }

    public String getCurrentPlayerName(){
        if (isFirstPlayerTurn()){
            return firstPlayer.name;
        } else {
            return secondPlayer.name;
        }
    }
}
