package com.company.agents;

import com.company.game.Board;
import com.company.enums.Player;
import com.company.enums.WallDirection;
import com.company.moves.Move;
import com.company.moves.PawnMove;
import com.company.moves.WallMove;

import java.util.Scanner;

import static com.company.enums.WallDirection.horizontal;
import static com.company.enums.WallDirection.vertical;

public class UserAgent extends GameAgent {

    Scanner scanner;

    public UserAgent(Board board, Player player) {
        super(board, player);
        scanner = new Scanner(System.in);
    }

    @Override
    public Move nextMove() {
        System.out.println(player.toString()+ " move");
        System.out.println("M for move and W for wall");
        char moveType = scanner.next().charAt(0);
        int row;
        int column;
        Move move = null;
        switch (moveType){
            case 'm':
                System.out.println("Insert coordinates for move: row column");
                row = scanner.nextInt();
                column = scanner.nextInt();
                move = new PawnMove(player, board, row, column);
                break;
            case 'w':
                System.out.println("Insert coordinates for wall: row column v/h (direction)");
                row = scanner.nextInt();
                column = scanner.nextInt();
                char direction = scanner.next().charAt(0);
                WallDirection wd = direction == 'v' ? vertical:horizontal;
                move = new WallMove(board, player, row, column, wd);
                break;
        }
        if (move != null && move.isValid()){
            return move;
        } else {
            System.out.println("Move is invalid");
            return nextMove();
        }
    }
}
