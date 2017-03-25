package com.company.enums;

/**
 * Created by dell on 14.03.17.
 */
public enum MoveDirection {
    UP(0), DOWN(1), LEFT(2), RIGHT(3);

    private final int index;

    MoveDirection(int index) {
        this.index = index;
    }

    public int index(){
        return index;
    }
}
