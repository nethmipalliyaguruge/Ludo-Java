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

class RedAggressiveStrategyTest {
    private Player red;
    private Player blue;
    private Board board;
    private final PlayerStrategy strategy = new RedAggressiveStrategy();

    @BeforeEach
    void setUp() {
        red = new Player(Colour.RED);
        blue = new Player(Colour.BLUE);
        board = new Board(List.of(red, blue));
    }

    @Test
    void captureIsChosenOverAPlainMove() {
        Piece redOne = red.getPieces().get(0);
        Piece redTwo = red.getPieces().get(1);
        redOne.moveToStart(26, Direction.CLOCKWISE);
        redTwo.moveToStart(40, Direction.CLOCKWISE);
        blue.getPieces().getFirst().moveToStart(29, Direction.CLOCKWISE);
        PieceMove plainMove = new StepMove(red, redTwo, board, 3);
        PieceMove capture = new StepMove(red, redOne, board, 3);

        PieceMove chosen = strategy.chooseMove(List.of(plainMove, capture), red);

        assertSame(capture, chosen);
    }

    @Test
    void entersANewPieceOnSixWhenNothingCanBeCaptured() {
        Piece redOne = red.getPieces().get(0);
        Piece redTwo = red.getPieces().get(1);
        redOne.moveToStart(26, Direction.CLOCKWISE);
        PieceMove plainMove = new StepMove(red, redOne, board, 6);
        PieceMove entry = new EnterBoardMove(red, redTwo, board, new FixedCoinToss(Direction.CLOCKWISE));

        PieceMove chosen = strategy.chooseMove(List.of(plainMove, entry), red);

        assertSame(entry, chosen);
    }
}