package com.ludo.strategy;

import com.ludo.model.*;
import com.ludo.move.PieceMove;
import com.ludo.move.StepMove;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;

class BlueCyclicStrategyTest {
    private Player blue;
    private Board board;
    private final PlayerStrategy strategy = new BlueCyclicStrategy();

    @BeforeEach
    void setUp(){
        blue = new Player(Colour.BLUE);
        board = new Board(List.of(blue));
    }

    @Test
    void movesB1FirstThenB2OnItsNextTurn(){
        Piece blueOne = blue.getPieces().get(0);
        Piece blueTwo = blue.getPieces().get(1);
        Piece blueThree = blue.getPieces().get(2);
        Piece blueFour = blue.getPieces().get(3);
        blueOne.moveToStart(13, Direction.CLOCKWISE);
        blueTwo.moveToStart(18, Direction.CLOCKWISE);
        blueThree.moveToStart(23, Direction.CLOCKWISE);
        blueFour.moveToStart(28, Direction.CLOCKWISE);
        PieceMove blueOneMove = new StepMove(blue, blueOne, board, 1);
        PieceMove blueTwoMove = new StepMove(blue, blueTwo, board, 1);
        PieceMove blueThreeMove = new StepMove(blue, blueThree, board, 1);
        PieceMove blueFourMove = new StepMove(blue, blueFour, board, 1);
        List<PieceMove> allMoves = List.of(blueOneMove, blueTwoMove, blueThreeMove, blueFourMove);

        PieceMove firstChoice = strategy.chooseMove(allMoves, blue);
        PieceMove secondChoice = strategy.chooseMove(allMoves, blue);

        assertSame(blueOneMove, firstChoice);
        assertSame(blueTwoMove, secondChoice);
    }
}
