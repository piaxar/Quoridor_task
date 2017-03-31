package com.company.agents;

import com.company.game.Board;
import com.company.enums.Player;
import com.company.enums.WallDirection;
import com.company.moves.Move;
import com.company.moves.PawnMove;
import com.company.moves.WallMove;

import java.util.LinkedList;

public class MinMaxAgent extends GameAgent{

    private Move selected;
    private int depth;

    private enum MinMax{
        MIN, MAX;
    }

    public MinMaxAgent(Board board, Player maximizerPlayer, Player minimizerPlayer, int depth) {
        super(board, maximizerPlayer, minimizerPlayer);
        this.depth = depth;
    }

    @Override
    public Move nextMove(){
        Node root = new Node(null);
        int leaf  = minMaxValue(root, depth, true);
        //System.out.println("selected: "+leaf);
        System.out.println(root.bestMove.toString());
        return root.bestMove;
    }

    public int minMaxValue(Node node, int depth, Boolean maximizingPlayer){

        if (node.move != null) {
            node.move.admit();
            //System.out.println("Im in node with depth "+depth+" move to come here is "+node.move.toString());
        }
        else {
            //System.out.println("Im in node with depth "+depth);
        }
        if (depth == 0){
            int res = node.getHeuristicValue();
            if (node.move != null) node.move.rollback();
            //System.out.println("My heuristic value is" + res);

            return res;
        }

        if (maximizingPlayer){
            int bestValue = Integer.MIN_VALUE;
            for(Node n: node.getChildren(maximizingPlayer)){
                int localBest = minMaxValue(n, depth -1, false);
                if (localBest >= bestValue){
                    //System.out.println("as a maximizer ="+maximizingPlayer.toString()+"new loc best ="+localBest);
                    bestValue = localBest;
                    node.bestMove = n.move;
                }
            }
            if (node.move != null) node.move.rollback();
            return bestValue;

        } else /* Minimizing player */{
            int bestValue = Integer.MAX_VALUE;
            for(Node n: node.getChildren(maximizingPlayer)){
                int localBest = minMaxValue(n, depth -1, true);
                if (localBest <= bestValue){
                    //System.out.println("as a maximizer ="+maximizingPlayer.toString()+"new loc best ="+localBest);
                    bestValue = localBest;
                    node.bestMove = n.move;
                }
            }
            if (node.move != null) node.move.rollback();
            return bestValue;
        }

    }

    class Node{
        LinkedList<Node> children;
        Move move;
        Move bestMove;

        Node(Move move){
            this.move = move;
            children = new LinkedList<>();
        }

        LinkedList<Node> getChildren(Boolean maximizer){
            Player currPlayer = maximizer ? maximizerPlayer:minimizerPlayer;
            for (Move m: getAvailableMoves(currPlayer)){
                if (m.isValid()) children.add(new Node(m));
            }
            return children;
        }

        public int getHeuristicValue() {
            int shpathForMax = board.shortestPath(maximizerPlayer);
            int shpathForMin = board.shortestPath(minimizerPlayer);
            //System.out.println("Shortest path for max = "+ shpathForMax+" == "+ maximizerPlayer.toString());
            //System.out.println("Shortest path for min = "+ shpathForMin+" == "+ minimizerPlayer.toString());
            return shpathForMin - shpathForMax;
        }
    }

//    Move getBestMove(){
//        Move bestMove = null;
//        int score = Integer.MIN_VALUE;
//        for (Move m:getAvailableMoves(player)){
//            int nScore = getScore(m, MinMax.MIN, board.getAnotherPlayer(player), 0);
//            if (nScore > score){
//                bestMove = m;
//                score = nScore;
//            }
//        }
//        return bestMove;
//    }
//
//    int getScore(Move move, MinMax minMax, Player player, int depth){
//        move.admit();
//        int score;
//        if (depth > 0) {
//            score = minMax == MinMax.MAX ? Integer.MIN_VALUE : Integer.MAX_VALUE;
//            Player anotherPlayer = board.getAnotherPlayer(player);
//            if (minMax == MinMax.MAX) {
//                for (Move m : getAvailableMoves(player)) {
//                    int newScore = getScore(m, MinMax.MIN, anotherPlayer, depth - 1);
//                    if (newScore > score) {
//                        score = newScore;
//                    }
//                }
//            } else {
//                    for (Move m : getAvailableMoves(player)) {
//                    int newScore = getScore(m, MinMax.MAX, anotherPlayer, depth - 1);
//                    if (newScore < score) {
//                        score = newScore;
//                    }
//                }
//            }
//        } else {
//            score = board.shortestPath(board.getAnotherPlayer(player)) - board.shortestPath(player);
//        }
//        move.rollback();
//        return score;
//    }
//
//    public Move find(){
//        BoardState root = new BoardState(board, null, MinMax.MAX, 2);
//        root.calculateSiblings();
//        return root.bestSibling.move;
//    }
//
//    class BoardState{
//        Board board;
//        LinkedList<BoardState> siblings;
//        BoardState bestSibling;
//        Move move;
//        MinMax function;
//        Player interestedPlayer;
//        int depth;
//        int score;
//
//        public BoardState(Board board, Move move, MinMax function, int depth) {
//            this.board = board;
//            this.move = move;
//            this.function = function;
//            this.depth = depth;
//            siblings = new LinkedList<>();
//            interestedPlayer = function == MinMax.MAX ? player: board.getAnotherPlayer(player);
//            if (depth > 0) {
//                for (Move m : getAvailableMoves(interestedPlayer)) {
//                    siblings.add(new BoardState(board, m, MinMax.getAnoter(function), depth - 1));
//                }
//            }
//        }
//
//        public void calculateSiblings(){
//            if (depth == 0){
//                score = Heuristic.getScore(board, interestedPlayer);
//            } else {
//                if (move != null) move.admit();
//                for (BoardState bs: siblings){
//                    bs.calculateSiblings();
//                }
//                if (function == MinMax.MAX){
//                    int localBest = Integer.MIN_VALUE;
//                    for (BoardState bs: siblings){
//                        if (bs.score > localBest){
//                            score = bs.score;
//                            bestSibling = bs;
//                        }
//                    }
//                } else {
//                    int localBest = Integer.MAX_VALUE;
//                    for (BoardState bs: siblings){
//                        if (bs.score < localBest){
//                            score = bs.score;
//                            bestSibling = bs;
//                        }
//                    }
//                }
//                if (move != null) move.rollback();
//            }
//        }
//    }

    public LinkedList<Move> getAvailableMoves(Player player){
        LinkedList<Move> availableMoves = new LinkedList<>();
        int row = board.getPlayerPosition(player).getRow();
        int column = board.getPlayerPosition(player).getColumn();
        // add all possible pawn moves
        for (int i = -1; i < 2; i++){
            for (int j = -1; j < 2; j++){
                Move nm = new PawnMove(player, board, row + i, column + j);
                if (nm.isValid()){
                    availableMoves.add(nm);
                }
            }
        }
        Move jumpMove = new PawnMove(player, board, row + 2, column);
        if (jumpMove.isValid()) availableMoves.add(jumpMove);
        jumpMove = new PawnMove(player, board, row + -2, column);
        if (jumpMove.isValid()) availableMoves.add(jumpMove);
        jumpMove = new PawnMove(player, board, row, column + 2);
        if (jumpMove.isValid()) availableMoves.add(jumpMove);
        jumpMove = new PawnMove(player, board, row, column - 2);
        if (jumpMove.isValid()) availableMoves.add(jumpMove);

        // add all possible wall moves
        for (int i = 0; i < 9; i++){
            for (int j = 0; j < 9; j++){
                for (WallDirection wd: WallDirection.values()){
                    Move nm = new WallMove(board, player, i, j, wd);
                    if (nm.isValid()){
                        availableMoves.add(nm);
                    }
                }
            }
        }
        return availableMoves;
    }

}
