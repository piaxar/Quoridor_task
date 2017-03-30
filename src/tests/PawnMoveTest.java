package tests;

import com.company.game.Board;
import com.company.enums.MoveDirection;
import com.company.enums.Player;
import com.company.moves.PawnMove;
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

}