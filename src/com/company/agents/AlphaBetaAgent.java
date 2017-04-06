package com.company.agents;

import com.company.enums.Player;
import com.company.game.Board;
import com.company.moves.Move;

/**
 * Created by dell on 04.04.17.
 */
public class AlphaBetaAgent extends MinMaxAgent {

    public AlphaBetaAgent(Board board, Player maximizerPlayer, Player minimizerPlayer, int depth) {
        super(board, maximizerPlayer, minimizerPlayer, depth);
    }

    @Override
    public Move nextMove() {
        Node root = new Node(false, true, null);
        alphaBeta(root, depth, -Double.MAX_VALUE, Double.MAX_VALUE);
        return root.selectedMove;
    }

    double alphaBeta(Node n, int d, double min, double max) {
        if (n.move != null) n.move.admit();
        double result;

        if (n.isLeaf || d == 0)
            result = n.evaluate();
        else {
            if (n.isMaxNode) { // maximizer
                double v = min;
                for (Node child : n.children(maximizerPlayer)) {
                    double localValue = alphaBeta(child, d - 1, v, max);
                    if (localValue > v) {
                        n.selectedMove = child.move;
                        v = localValue;
                    }
                    if (v > max){
                        v = max;
                        break;
                    }
                }
                result = v;
            } else { // minimizer
                double v = max;
                for (Node child : n.children(minimizerPlayer)) {
                    double localValue = alphaBeta(child, d - 1, min, v);
                    if (localValue < v) {
                        n.selectedMove = child.move;
                        v = localValue;
                    }
                    if (v < min){
                        v = min;
                        break;
                    }
                }
                result = v;
            }
        }
        if (n.move != null) n.move.rollback();
        return result;
    }
}
