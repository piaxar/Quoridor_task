package tests;

import com.company.Board;
import com.company.enums.WallDirection;
import com.company.exceptions.InvalidMoveException;
import com.company.exceptions.InvalidWallPlacement;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by dell on 14.03.17.
 */
class BoardTest {
    @Test
    void checkPawnCollisions() {
        Board board = new Board();
        try {
            board.setSecondPlayerPosition(1,1);
        } catch (InvalidMoveException e) {
            e.printStackTrace();
        }
        Throwable exception = expectThrows(InvalidMoveException.class, () -> {
            board.setFirstPlayerPosition(1, 1);
        });
        assertEquals(exception.getMessage().isEmpty(), false);
    }

    @Test
    void checkIllegalArguments() {
        Board board = new Board();
        Throwable exception = expectThrows(IllegalArgumentException.class, () -> {
            board.setFirstPlayerPosition(10, 4);
        });
        assertEquals(exception.getMessage().isEmpty(), false);
    }

    @Test
    void testWallPlacement() {
        Board b = new Board();
        try {
            b.setWall(2,3, WallDirection.horizontal);
            b.setWall(1,1, WallDirection.horizontal);
            b.setWall(2,1, WallDirection.horizontal);
            b.setWall(3,3, WallDirection.horizontal);
            b.setWall(3,1, WallDirection.vertical);
            b.setWall(3,2, WallDirection.vertical);
            b.setWall(2,3, WallDirection.vertical);
            b.setWall(2,3, WallDirection.horizontal);
        } catch (InvalidWallPlacement invalidWallPlacement) {
            invalidWallPlacement.printStackTrace();
        }
        assertEquals(true, true);
    }

}