package com.company;

import com.company.enums.MoveDirection;

/**
 * Created by dell on 08.03.17.
 */
public class Cell {
    private int row;
    private int column;
    private Cell[] neighbours;
    private int cost = Integer.MAX_VALUE;

    Cell(int row, int column){
        this.row = row;
        this.column = column;
        neighbours = new Cell[4];
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setNeighbour(MoveDirection moveDirection, Cell c){
        neighbours[moveDirection.index()] = c;
    }

    public int getRow() {
        return row;
    }
    public int getColumn() {
        return column;
    }

    public Cell[] getNeighbours(){
        return neighbours;
    }
}
