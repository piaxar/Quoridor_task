package tests;

import com.company.enums.WallDirection;
import com.company.game.Board;
import com.company.enums.MoveDirection;
import com.company.enums.Player;
import com.company.moves.PawnMove;
import com.company.moves.WallMove;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by dell on 25.03.17.
 */
class PawnMoveTest {

    @Test
    void simpleMoveTest(){
        Board board = new Board();
        PawnMove pm = new PawnMove(Player.FIRST_PLAYER, board, 0, 5);
        assertEquals(true, pm.isValid());

        pm = new PawnMove(Player.FIRST_PLAYER, board, 2, 5);
        assertEquals(false, pm.isValid());

        pm = new PawnMove(Player.FIRST_PLAYER, board, -1, 5);
        assertEquals(false, pm.isValid());

        pm = new PawnMove(Player.FIRST_PLAYER, board, 1, 4);
        assertEquals(true, pm.isValid());

        pm = new PawnMove(Player.FIRST_PLAYER, board, 2, 4);
        assertEquals(false, pm.isValid());

        pm = new PawnMove(Player.FIRST_PLAYER, board, 1, 4);
        pm.admit();
        assertEquals(4,board.getPlayerPosition(Player.FIRST_PLAYER).getColumn());
        assertEquals(1,board.getPlayerPosition(Player.FIRST_PLAYER).getRow());
        pm.rollback();
        assertEquals(4,board.getPlayerPosition(Player.FIRST_PLAYER).getColumn());
        assertEquals(0,board.getPlayerPosition(Player.FIRST_PLAYER).getRow());


        board.getBoard()[0][4].getNeighbours()[MoveDirection.UP.index()] = null;
        pm = new PawnMove(Player.FIRST_PLAYER, board, 1, 4);
        assertEquals(false, pm.isValid());
    }

    @Test
    void bugOccursTest(){
        Board board = new Board();
        board.setPlayerPosition(Player.FIRST_PLAYER, 3, 5);
        board.setPlayerPosition(Player.SECOND_PLAYER, 3, 6);
        WallMove wm = new WallMove(board, Player.FIRST_PLAYER, 3, 6, WallDirection.horizontal);
        wm.admit();
        PawnMove pm = new PawnMove(Player.SECOND_PLAYER, board, 3, 5);
        assertEquals(false, pm.isValid());
    }

    @Test
    void jumpMovesTest(){
        Board board = new Board();
        board.setPlayerPosition(Player.SECOND_PLAYER, 1, 4);
        PawnMove pm = new PawnMove(Player.FIRST_PLAYER, board, 2, 4);
        assertEquals(true, pm.isValid());

        board.setPlayerPosition(Player.SECOND_PLAYER, 1, 4);
        pm = new PawnMove(Player.FIRST_PLAYER, board, 2, 4);
        assertEquals(true, pm.isValid());

        board.getBoard()[1][4].getNeighbours()[MoveDirection.UP.index()] = null;
        assertEquals(false, pm.isValid());

        pm = new PawnMove(Player.FIRST_PLAYER, board, 1, 5);
        assertEquals(true, pm.isValid());

        pm = new PawnMove(Player.FIRST_PLAYER, board, 1, 3);
        assertEquals(true, pm.isValid());

        board.getBoard()[1][4].getNeighbours()[MoveDirection.LEFT.index()] = null;
        pm = new PawnMove(Player.FIRST_PLAYER, board, 1, 3);
        assertEquals(false, pm.isValid());

        pm = new PawnMove(Player.FIRST_PLAYER, board, 0, 6);
        assertEquals(false, pm.isValid());
    }

    @Test
    void gameCaseTest(){
        Board board = new Board();
        board.setPlayerPosition(Player.FIRST_PLAYER, 7, 7);
        WallMove wm = new WallMove(board, Player.FIRST_PLAYER, 8, 6, WallDirection.horizontal);
        wm.admit();
        PawnMove pm = new PawnMove(Player.FIRST_PLAYER, board, 7, 8);
        assertEquals(true, pm.isValid());
    }

}