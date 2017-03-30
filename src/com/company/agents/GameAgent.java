package com.company.agents;

import com.company.game.Board;
import com.company.enums.Player;
import com.company.moves.Move;

/**
 * Created by dell on 28.03.17.
 */
public abstract class GameAgent {
    protected Board board;
    protected Player player;
    protected int wallsLeft = 10;

    public GameAgent(Board board, Player player){
        this.board = board;
        this.player = player;
    }
    public abstract Move nextMove();
}
