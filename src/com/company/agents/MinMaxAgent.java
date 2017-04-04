package com.company.agents;

import com.company.enums.Player;
import com.company.game.Board;
import com.company.moves.Move;
import com.company.moves.PawnMove;
import com.company.moves.WallMove;

import java.util.LinkedList;

/**
 * Created by dell on 03.04.17.
 */
public class MinMaxAgent extends GameAgent {

    int depth;

    public MinMaxAgent(Board board, Player maximizerPlayer, Player minimizerPlayer, int depth) {
        super(board, maximizerPlayer, minimizerPlayer);
        this.depth = depth;
    }

    @Override
    public Move nextMove() {
        Node root = new Node(false, true, null);
        minimax(root, depth);
        System.out.println(root.selectedMove.toString());
        return root.selectedMove;
    }

    /**
     * the minimax value of n, searched to depth d
     */
    double minimax(Node n, int d) {
        if (n.move != null) n.move.admit();
        double result;

        if (n.isLeaf || d == 0)
            result = n.evaluate();
        else {
            if (n.isMaxNode) { // maximizer
                double v = -Double.MAX_VALUE;
                for (Node child : n.children(maximizerPlayer)) {
                    double localValue = minimax(child, d - 1);
                    if (localValue > v) {
                        n.selectedMove = child.move;
                        v = localValue;
                    }
                }
                result = v;
            } else { // minimizer
                double v = Double.MAX_VALUE;
                for (Node child : n.children(minimizerPlayer)) {
                    double localValue = minimax(child, d - 1);
                    if (localValue < v) {
                        n.selectedMove = child.move;
                        v = localValue;
                    }
                }
                result = v;
            }
        }
        if (n.move != null) n.move.rollback();
        return result;
    }

    class Node{
        protected boolean isLeaf, isMaxNode;
        protected Move move;
        protected Move selectedMove;

        public Node(boolean isLeaf, boolean isMaxNode, Move move) {
            this.isLeaf = isLeaf;
            this.isMaxNode = isMaxNode;
            this.move = move;
        }

        LinkedList<Node> children(Player player){
            LinkedList<Node> children = new LinkedList<>();
            for (PawnMove m: board.getAvailablePawnMoves(player)){
                children.add(new Node(m.isLeaf, !isMaxNode, m));
            }
            for (WallMove m: board.getAvailableWallMoves(player)){
                children.add(new Node(false, !isMaxNode, m));
            }

            return children;
        }

        Double evaluate(){
            // if isMaxNode - maximizer
            if (isLeaf && board.inWinningPosition(maximizerPlayer)) return 1000.0;
            if (isLeaf && board.inWinningPosition(minimizerPlayer)) return -1000.0;
            return 1.2/(board.shortestPath(maximizerPlayer)) - 0.9/(board.shortestPath(minimizerPlayer));
        }
    }
}
