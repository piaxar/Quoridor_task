package tests;

import com.company.Board;
import com.company.enums.WallDirection;
import com.company.moves.WallMove;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by dell on 25.03.17.
 */
class WallMoveTest {

    @Test
    void validWallPlacement(){
        Board board = new Board();
        WallMove.clearAdmittedWallMoves();

        WallMove wm = new WallMove(board, 5, 5, WallDirection.horizontal);
        assertEquals(true, wm.isValid());

        wm = new WallMove(board, 8, 7, WallDirection.horizontal);
        assertEquals(true, wm.isValid());

        wm = new WallMove(board, 8, 8, WallDirection.horizontal);
        assertEquals(false, wm.isValid());

        wm = new WallMove(board, 5, 5, WallDirection.vertical);
        assertEquals(true, wm.isValid());
    }

    @Test
    void intersectionTesting(){
        Board board = new Board();
        WallMove.clearAdmittedWallMoves();

        WallMove wm = new WallMove(board, 2, 1, WallDirection.horizontal);
        wm.admit();
        wm = new WallMove(board, 2, 3, WallDirection.horizontal);
        wm.admit();
        wm = new WallMove(board, 2, 3, WallDirection.vertical);
        assertEquals(true, wm.isValid());
        wm.admit();

        wm = new WallMove(board, 3, 1, WallDirection.horizontal);
        assertEquals(true, wm.isValid());

        wm = new WallMove(board, 3, 1, WallDirection.vertical);
        assertEquals(true, wm.isValid());

        wm = new WallMove(board, 2, 4, WallDirection.horizontal);
        assertEquals(false, wm.isValid());

        wm = new WallMove(board, 2, 3, WallDirection.horizontal);
        assertEquals(false, wm.isValid());
    }

    @Test
    void noPathTesting(){
        Board board = new Board();
        WallMove.clearAdmittedWallMoves();

        WallMove wm = new WallMove(board, 5, 0, WallDirection.horizontal);
        assertEquals(true, wm.isValid());
        wm.admit();
        wm = new WallMove(board, 5, 2, WallDirection.horizontal);
        assertEquals(true, wm.isValid());
        wm.admit();
        wm = new WallMove(board, 5, 4, WallDirection.horizontal);
        assertEquals(true, wm.isValid());
        wm.admit();
        wm = new WallMove(board, 5, 6, WallDirection.horizontal);
        assertEquals(true, wm.isValid());
        wm.admit();
        wm = new WallMove(board, 4, 8, WallDirection.vertical);
        assertEquals(true, wm.isValid());
        wm.admit();
        wm = new WallMove(board, 3, 7, WallDirection.horizontal);
        assertEquals(false, wm.isValid());
    }

}