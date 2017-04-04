package com.company.agents;

import com.company.game.Board;
import com.company.enums.Player;
import com.company.moves.Move;

/**
 * Created by dell on 28.03.17.
 */
public abstract class GameAgent {
    protected Board board;
    protected Player maximizerPlayer, minimizerPlayer;

    public GameAgent(Board board, Player maximizerPlayer, Player minimizerPlayer){
        this.board = board;
        this.maximizerPlayer = maximizerPlayer;
        this.minimizerPlayer = minimizerPlayer;
    }
    public abstract Move nextMove();
}
