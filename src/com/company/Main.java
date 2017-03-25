package com.company;

import com.company.enums.MoveDirection;
import com.company.enums.WallDirection;
import com.company.exceptions.InvalidWallPlacement;
import com.company.moves.Move;
import com.company.moves.PawnMove;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
//        Scanner in = new Scanner(System.in);
//        Game game = new Game("First player", "Second player");
//
//        ArrayList<Move> moves = new ArrayList<>();
//        moves.add(new PawnMove());
//
//        Board board = game.getBoard();
//        board.printBoard();
//        while (!game.isFinished()){
//            System.out.println(game.getCurrentPlayerName()+"'s turn");
//            // ask for type of turn
//            System.out.println("Print \"w\" for wall placement, \"m\" for move: ");
//            char moveType = in.next().charAt(0);
//            if (moveType == 'w'){
//                try {
//                    System.out.println("row = ?, column = ?, direction = v/h?");
//                    int row = in.nextInt();
//                    int column = in.nextInt();
//                    char d = in.next().charAt(0);
//                    WallDirection wd;
//                    if (d == 'v') wd = WallDirection.vertical; else wd = WallDirection.horizontal;
//                    game.nextTurnWall(row, column, wd);
//                } catch (InvalidWallPlacement invalidWallPlacement) {
//                    invalidWallPlacement.printStackTrace();
//                }
//            } else if (moveType == 'm'){
//                // moving
//                try {
//                    MoveDirection md = null;
//                    System.out.println("MoveDirection w/s/a/d ?");
//                    switch (in.next().charAt(0)) {
//                        case 'w':
//                            md = MoveDirection.UP;
//                            break;
//                        case 's':
//                            md = MoveDirection.DOWN;
//                            break;
//                        case 'a':
//                            md = MoveDirection.LEFT;
//                            break;
//                        case 'd':
//                            md = MoveDirection.RIGHT;
//                            break;
//                    }
//                    game.nextTurnMove(md);
//                } catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//            board.printBoard();
//        }
//        in.close();
    }
}
