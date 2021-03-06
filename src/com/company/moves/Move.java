package com.company.moves;

import com.company.game.Board;

/**
 * Created by dell on 24.03.17.
 */
public abstract class Move {
    private Board board;
    public abstract Boolean isValid();
    public abstract void admit();
    public abstract void rollback();

    @Override
    public abstract boolean equals(Object obj);
}
