package com.company.agents;

import com.company.enums.Player;
import com.company.game.Board;
import com.company.moves.Move;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

/**
 * Created by dell on 05.04.17.
 */
public class MonteCarloAgent extends GameAgent {

    final long GIVEN_TIME_MILLISECONDS;
    long finishTime;
    Random random;

    public MonteCarloAgent(Board board, Player maximizerPlayer, Player minimizerPlayer, long given_time_milliseconds) {
        super(board, maximizerPlayer, minimizerPlayer);
        GIVEN_TIME_MILLISECONDS = given_time_milliseconds;
        random = new Random();
    }

    @Override
    public Move nextMove() {

        finishTime = System.currentTimeMillis() + GIVEN_TIME_MILLISECONDS;
        MCST mcst = new MCST();
        while (finishTime > System.currentTimeMillis()){
            mcst.makeIteration();
        }
        System.out.println("total nodes: "+mcst.N);
        return mcst.getBestMove();
    }



    class MCST{
        private final double C_COEFFICIENT = 2.0;
        private int N = 0; // total number of times nodes have been visited
        private Node root;

        private class Node{
            Node parent;
            ArrayList<Node> children;
            ArrayList<Move> addedMoves;
            Player currentPlayer;
            Double wins = 0.0;
            Double games = 0.0;
            int n = 0; // number of times node has been visited
            Move move; // move to get to this node

            Node(Node parent, Move move, Player currentPlayer){
                this.parent = parent;
                this.move = move;
                this.currentPlayer = currentPlayer;
                children = new ArrayList<>();
                addedMoves = new ArrayList<>();
            }
        }

        private double UCB(Node node){
            return (node.wins/node.games) + C_COEFFICIENT * Math.sqrt(Math.log(N) / node.n);
        }

        MCST(){
            root = new Node(null, null, maximizerPlayer);
        }

        public Move getBestMove(){
            double maxUCB = -Double.MAX_VALUE;
            Node bestChild = null;
            for (Node n: root.children){
                double localUCB = UCB(n);
                if(localUCB > maxUCB){
                    bestChild = n;
                    maxUCB = localUCB;
                }
            }
            return bestChild.move;
        }

        public void makeIteration(){
            Node chosenNode = selection(root);
            chosenNode = expansion(chosenNode);
            chosenNode = simulation(chosenNode);
            backpropagation(chosenNode);

        }

        public Node selection(Node node){
            if (node.children.size() == 0) return node;
            Node bestChild = null;
            double maxUCB = -Double.MAX_VALUE;
            for (Node n: node.children){
                double localUCB = UCB(n);
                if(localUCB > maxUCB){
                    bestChild = n;
                    maxUCB = localUCB;
                }
            }
            bestChild.move.admit();
            bestChild = selection(bestChild);
            return bestChild;
        }

        public Node expansion(Node node){
            LinkedList<Move> moves = board.getAvailableMoves(node.currentPlayer);
            Move best = null;
            double bestValue = -Double.MAX_VALUE;
            for (Move m: moves){
                if (!node.addedMoves.contains(m)) {
                    m.admit();
                    node.addedMoves.add(m);
                    double localBest = evaluate();
                    if (bestValue < localBest) {
                        bestValue = localBest;
                        best = m;
                    }
                    m.rollback();
                }
            }
            Node child = new Node(node, best, board.getAnotherPlayer(node.currentPlayer));
            node.children.add(child);
            child.move.admit();
            return child;
        }

        private Double evaluate(){
            // if isMaxNode - maximizer
            if (board.inWinningPosition(maximizerPlayer)) return 1000.0;
            if (board.inWinningPosition(minimizerPlayer)) return -1000.0;
            return 1.2/(board.shortestPath(maximizerPlayer)) - 0.9/(board.shortestPath(minimizerPlayer));
        }

        public Node simulation(Node node){
            int shMax = board.shortestPath(maximizerPlayer);
            int shMin = board.shortestPath(minimizerPlayer);
            if (shMax == shMin && node.currentPlayer == maximizerPlayer){
                node.wins++;
            } else if (shMax > shMin) {
                node.wins++;
            } // else lose
            node.games++;
            return node;
        }

        public void backpropagation(Node node){
            node.games++;
            node.n++;
            node.move.rollback();
            if (node.wins == 1){
                propagateUp(node.parent, true);
            } else {
                propagateUp(node.parent, false);
            }
        }

        private void propagateUp(Node node, boolean win){
            if (win) node.wins++;
            if (node.move != null) node.move.rollback();
            node.n++;
            node.games++;
            if (node.parent != null){
                propagateUp(node.parent, win);
            } else {
                N++;
            }
        }
    }
}
