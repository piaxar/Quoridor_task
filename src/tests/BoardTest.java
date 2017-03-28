package tests;

import com.company.Board;
import com.company.enums.Player;
import com.company.enums.WallDirection;
import com.company.moves.WallMove;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by dell on 28.03.17.
 */
class BoardTest {

    @Test
    void shortestPathFindingTest(){
        Board board = new Board();
        WallMove wm = new WallMove(board, 1, 3, WallDirection.horizontal);
        assertEquals(8,board.shortestPath(Player.FIRST_PLAYER));
        wm.admit();
        assertEquals(9,board.shortestPath(Player.FIRST_PLAYER));
    }

    @Test
    void shortestPathLongTest(){
        Board board = new Board();
        WallMove wm = new WallMove(board, 1, 5, WallDirection.vertical);
        wm.admit();
        wm = new WallMove(board, 2, 3, WallDirection.horizontal);
        wm.admit();
        wm = new WallMove(board, 1, 2, WallDirection.vertical);
        wm.admit();
        wm = new WallMove(board, 3, 2, WallDirection.vertical);
        wm.admit();
        wm = new WallMove(board, 3, 3, WallDirection.vertical);
        wm.admit();
        wm = new WallMove(board, 5, 2, WallDirection.vertical);
        wm.admit();
        wm = new WallMove(board, 5, 3, WallDirection.horizontal);
        wm.admit();
        wm = new WallMove(board, 6, 2, WallDirection.horizontal);
        wm.admit();
        wm = new WallMove(board, 7, 4, WallDirection.vertical);
        wm.admit();
        wm = new WallMove(board, 8, 4, WallDirection.horizontal);
        wm.admit();
        assertEquals(14,board.shortestPath(Player.FIRST_PLAYER));
        assertEquals(10,board.shortestPath(Player.SECOND_PLAYER));
    }
}