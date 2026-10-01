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
        redOne.moveToStart(30, Direction.CLOCKWISE);
        PieceMove plainMove = new StepMove(red, redOne, board, 6);
        PieceMove entry =new EnterBoardMove(red, redTwo, board, new FixedCoinToss(Direction.CLOCKWISE));

        PieceMove chosen = strategy.chooseMove(List.of(plainMove, entry), red);

        assertSame(entry, chosen);
    }

    @Test
    void capturesTheOpponentClosestToItsHome() {
        Piece redOne = red.getPieces().get(0);
        Piece redTwo = red.getPieces().get(1);
        redOne.moveToStart(26, Direction.CLOCKWISE);
        redTwo.moveToStart(6, Direction.CLOCKWISE);
        blue.getPieces().get(0).moveToStart(29, Direction.CLOCKWISE);
        blue.getPieces().get(1).moveToStart(9, Direction.CLOCKWISE);
        PieceMove captureFarFromHome = new StepMove(red, redOne, board, 3);
        PieceMove captureNearHome = new StepMove(red, redTwo, board, 3);

        PieceMove chosen = strategy.chooseMove(List.of(captureFarFromHome, captureNearHome), red);

        assertSame(captureNearHome, chosen);
    }

    @Test
    void avoidsCreatingABlockWhenAnotherMoveIsPossible() {
        Piece redOne = red.getPieces().get(0);
        Piece redTwo = red.getPieces().get(1);
        Piece redThree = red.getPieces().get(2);
        redOne.moveToStart(20, Direction.CLOCKWISE);
        redTwo.moveToStart(23, Direction.CLOCKWISE);
        redThree.moveToStart(30, Direction.CLOCKWISE);
        PieceMove createsBlock = new StepMove(red, redOne, board, 3);
        PieceMove noBlock = new StepMove(red, redThree, board, 3);

        PieceMove chosen = strategy.chooseMove(List.of(createsBlock, noBlock), red);

        assertSame(noBlock, chosen);
    }

    @Test
    void doesNotEnterAPieceOntoItsOwnPieceAtTheStart() {
        Piece redOne = red.getPieces().get(0);
        Piece redTwo = red.getPieces().get(1);
        redOne.moveToStart(26, Direction.CLOCKWISE);
        PieceMove entryCreatingBlock = new EnterBoardMove(red, redTwo, board, new FixedCoinToss(Direction.CLOCKWISE));
        PieceMove moveOffTheStart = new StepMove(red, redOne, board, 6);

        PieceMove chosen = strategy.chooseMove(List.of(entryCreatingBlock, moveOffTheStart), red);

        assertSame(moveOffTheStart, chosen);
    }

    @Test
    void createsABlockWhenItIsTheOnlyMove() {
        Piece redOne = red.getPieces().get(0);
        Piece redTwo = red.getPieces().get(1);
        redOne.moveToStart(20, Direction.CLOCKWISE);
        redTwo.moveToStart(23, Direction.CLOCKWISE);
        PieceMove createsBlock = new StepMove(red, redOne, board, 3);

        PieceMove chosen = strategy.chooseMove(List.of(createsBlock), red);

        assertSame(createsBlock, chosen);
    }
}