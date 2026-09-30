package com.ludo.strategy;

import com.ludo.model.*;
import com.ludo.move.BlockMove;
import com.ludo.move.EnterBoardMove;
import com.ludo.move.PieceMove;
import com.ludo.move.StepMove;
import com.ludo.random.FixedCoinToss;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;

class GreenBlockingStrategyTest {
    private Player green;
    private Board board;
    private final PlayerStrategy strategy = new GreenBlockingStrategy();

    @BeforeEach
    void setUp() {
        green = new Player(Colour.GREEN);
        board = new Board(List.of(green));
    }

    @Test
    void entersANewPieceOnSixToKeepTheBaseEmpty() {
        Piece greenOne = green.getPieces().get(0);
        Piece greenTwo = green.getPieces().get(1);
        greenOne.moveToStart(40, Direction.CLOCKWISE);
        PieceMove plainMove = new StepMove(green, greenOne, board, 6);
        PieceMove entry = new EnterBoardMove(green, greenTwo, board, new FixedCoinToss(Direction.CLOCKWISE));

        PieceMove chosen = strategy.chooseMove(List.of(plainMove, entry), green);

        assertSame(entry, chosen);
    }

    @Test
    void movesAPieceOutsideTheBlockRatherThanBreakingIt() {
        Piece greenOne = green.getPieces().get(0);
        Piece greenTwo = green.getPieces().get(1);
        Piece greenThree = green.getPieces().get(2);
        greenOne.moveToStart(39, Direction.CLOCKWISE);
        greenTwo.moveToStart(39, Direction.CLOCKWISE);
        greenThree.moveToStart(10, Direction.CLOCKWISE);
        PieceMove breakTheBlockMove = new StepMove(green, greenOne, board, 2);
        PieceMove freePieceMove = new StepMove(green, greenThree, board, 2);

        PieceMove chosen = strategy.chooseMove(List.of(breakTheBlockMove, freePieceMove), green);

        assertSame(freePieceMove, chosen);
    }

    @Test
    void prefersMovingForwardWithTheWholeBlock() {
        Piece greenOne = green.getPieces().get(0);
        Piece greenTwo = green.getPieces().get(1);
        Piece greenThree = green.getPieces().get(2);
        greenOne.moveToStart(39, Direction.CLOCKWISE);
        greenTwo.moveToStart(39, Direction.COUNTER_CLOCKWISE);
        greenThree.moveToStart(10, Direction.CLOCKWISE);
        PieceMove freePiece = new StepMove(green, greenThree, board, 4);
        PieceMove wholeBlock = new BlockMove(green, List.of(greenOne, greenTwo), board, 4);

        PieceMove chosen = strategy.chooseMove(List.of(freePiece, wholeBlock), green);

        assertSame(wholeBlock, chosen);
    }
}
