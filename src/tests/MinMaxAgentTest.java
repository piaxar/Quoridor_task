package tests;

import com.company.agents.MinMaxAgent;
import com.company.enums.Player;
import com.company.game.Board;
import com.company.moves.Move;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by dell on 30.03.17.
 */
class MinMaxAgentTest {

    @Test
    void availableMovesTest(){
        Board board = new Board();
        MinMaxAgent agent = new MinMaxAgent(board, Player.FIRST_PLAYER, Player.SECOND_PLAYER, 2);

        for (Move m:agent.getAvailableMoves(Player.FIRST_PLAYER)){
            System.out.println(m.toString());
        }

    }

}