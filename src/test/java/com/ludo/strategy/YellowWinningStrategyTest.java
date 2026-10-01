package com.ludo.strategy;

import com.ludo.model.*;
import com.ludo.move.EnterBoardMove;
import com.ludo.move.PieceMove;
import com.ludo.move.StepMove;
import com.ludo.random.FixedCoinToss;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;

class YellowWinningStrategyTest {
    private Player yellow;
    private Player red;
    private Board board;
    private final PlayerStrategy strategy = new YellowWinningStrategy();

    @BeforeEach
    void setUp() {
        yellow = new Player(Colour.YELLOW);
        red = new Player(Colour.RED);
        board = new Board(List.of(yellow, red));
    }

    @Test
    void pieceThatStillNeedsACaptureTakesIt() {
        Piece yellowOne = yellow.getPieces().get(0);
        Piece yellowTwo = yellow.getPieces().get(1);
        yellowOne.moveToStart(40, Direction.CLOCKWISE);
        yellowTwo.moveToStart(10, Direction.CLOCKWISE);
        red.getPieces().getFirst().moveToStart(13, Direction.CLOCKWISE);
        PieceMove plainMove = new StepMove(yellow, yellowOne, board, 3);
        PieceMove capture = new StepMove(yellow, yellowTwo, board, 3);

        PieceMove chosen = strategy.chooseMove(List.of(plainMove, capture), yellow);

        assertSame(capture, chosen);
    }

    @Test
    void entersAPieceOnSixBeforeLookingForCaptures() {
        Piece yellowOne = yellow.getPieces().get(0);
        Piece yellowTwo = yellow.getPieces().get(1);
        yellowOne.moveToStart(10, Direction.CLOCKWISE);
        red.getPieces().getFirst().moveToStart(16, Direction.CLOCKWISE);
        PieceMove capture = new StepMove(yellow, yellowOne, board, 6);
        PieceMove entry = new EnterBoardMove(yellow, yellowTwo, board, new FixedCoinToss(Direction.CLOCKWISE));

        PieceMove chosen = strategy.chooseMove(List.of(capture, entry), yellow);

        assertSame(entry, chosen);
    }

    @Test
    void withoutACaptureMovesThePieceClosestToHome() {
        Piece yellowOne = yellow.getPieces().get(0);
        Piece yellowTwo = yellow.getPieces().get(1);
        yellowOne.moveToStart(10, Direction.CLOCKWISE);
        yellowTwo.moveToStart(40, Direction.CLOCKWISE);
        PieceMove farFromHome = new StepMove(yellow, yellowOne, board, 2);
        PieceMove nearHome = new StepMove(yellow, yellowTwo, board, 2);

        PieceMove chosen = strategy.chooseMove(List.of(farFromHome, nearHome), yellow);

        assertSame(nearHome, chosen);
    }

    @Test
    void pieceThatAlreadyCapturedDoesNotChaseAnotherCapture() {
        Piece yellowOne = yellow.getPieces().get(0);
        Piece yellowTwo = yellow.getPieces().get(1);
        yellowOne.moveToStart(10, Direction.CLOCKWISE);
        yellowOne.recordCapture();
        yellowTwo.moveToStart(40, Direction.CLOCKWISE);
        red.getPieces().getFirst().moveToStart(13, Direction.CLOCKWISE);
        PieceMove extraCapture = new StepMove(yellow, yellowOne, board, 3);
        PieceMove nearHome = new StepMove(yellow, yellowTwo, board, 3);

        PieceMove chosen = strategy.chooseMove(List.of(extraCapture, nearHome), yellow);

        assertSame(nearHome, chosen);
    }
}
