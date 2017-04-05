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
//        System.out.println(System.currentTimeMillis());
//        System.out.println(finishTime);
        MCST mcst = new MCST();
        while (finishTime > System.currentTimeMillis()){
            mcst.makeIteration();
            //System.out.println("time left: " + Long.toString(finishTime-System.currentTimeMillis()));
        }
        System.out.println("total nodes: "+mcst.N);
        return mcst.getBestMove();
    }



    class MCST{
        private final double C_COEFFICIENT = 1.0;
        private int N = 0; // total number of times nodes have been visited
        private Node root;

        private class Node{
            Node parent;
            ArrayList<Node> children;
            Player currentPlayer;
            int wins = 0;
            int games = 0;
            int n = 0; // number of times node has been visited
            Move move; // move to get to this node

            Node(Node parent, Move move, Player currentPlayer){
                this.parent = parent;
                this.move = move;
                this.currentPlayer = currentPlayer;
                children = new ArrayList<>();
            }
        }

        private double UCB(Node node){
            return  (node.wins/node.games) + C_COEFFICIENT * Math.sqrt(Math.log(N) / node.n);
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
            //System.out.println("selection");
            Node chosenNode = selection(root);
            //System.out.println("expansion");
            chosenNode = expansion(chosenNode);
            //System.out.println("simulation");
            chosenNode = simulation(chosenNode);
            //System.out.println("backpropagation");
            backpropagation(chosenNode);

        }

        public Node selection(Node node){
            if (node.children.size() == 0) return node;
            // searching for the biggest UCB
            Node bestChild = null;
            double maxUCB = -Double.MAX_VALUE;
            for (Node n: node.children){
                double localUCB = UCB(n);
                if(localUCB > maxUCB){
                    bestChild = n;
                    maxUCB = localUCB;
                }
            }
            // admit move
            // do not forget to rollback on backpropagation
            bestChild.move.admit();
            bestChild = selection(bestChild);
            //System.out.println(N);
            return bestChild;
        }

        public Node expansion(Node node){
            //System.out.println(node.currentPlayer.toString());
            LinkedList<Move> moves = board.getAvailableMoves(node.currentPlayer);
            // TODO: add heuristic function to chose move
            Move m = moves.get(random.nextInt(moves.size()));
            Node child = new Node(node, m, board.getAnotherPlayer(node.currentPlayer));
            node.children.add(child);
            child.move.admit();
            return child;
        }

        public Node simulation(Node node){
            // TODO: add cleverer simulation
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
