package com.company.enums;

/**
 * Created by dell on 24.03.17.
 */
public enum Player {
    FIRST_PLAYER(0), SECOND_PLAYER(1);

    private final int index;

    Player(int index) {
        this.index = index;
    }

    public int index(){
        return index;
    }
}
