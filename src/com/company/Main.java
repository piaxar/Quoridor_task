package com.company;

import com.company.enums.Direction;
import com.company.enums.WallDirection;
import com.company.exceptions.InvalidWallPlacement;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Game game = new Game("First player", "Second player");

        Board board = game.getBoard();
        board.printBoard();
        while (!game.isFinished()){
            System.out.println(game.getCurrentPlayerName()+"'s turn");
            // ask for type of turn
            System.out.println("Print \"w\" for wall placement, \"m\" for move: ");
            char moveType = in.next().charAt(0);
            if (moveType == 'w'){
                try {
                    System.out.println("row = ?, column = ?, direction = v/h?");
                    int row = in.nextInt();
                    int column = in.nextInt();
                    char d = in.next().charAt(0);
                    WallDirection wd;
                    if (d == 'v') wd = WallDirection.vertical; else wd = WallDirection.horizontal;
                    game.nextTurnWall(row, column, wd);
                } catch (InvalidWallPlacement invalidWallPlacement) {
                    invalidWallPlacement.printStackTrace();
                }
            } else if (moveType == 'm'){
                // moving
                try {
                    Direction md = null;
                    System.out.println("Direction w/s/a/d ?");
                    switch (in.next().charAt(0)) {
                        case 'w':
                            md = Direction.UP;
                            break;
                        case 's':
                            md = Direction.DOWN;
                            break;
                        case 'a':
                            md = Direction.LEFT;
                            break;
                        case 'd':
                            md = Direction.RIGHT;
                            break;
                    }
                    game.nextTurnMove(md);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
            board.printBoard();
        }
        in.close();
    }
}
