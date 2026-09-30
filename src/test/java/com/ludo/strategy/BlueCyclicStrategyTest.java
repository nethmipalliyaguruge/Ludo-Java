package com.ludo.strategy;

import com.ludo.event.RecordingListener;
import com.ludo.model.*;
import com.ludo.move.PieceMove;
import com.ludo.move.StepMove;
import com.ludo.random.FixedPicker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class BlueCyclicStrategyTest {
    private Player blue;
    private Board board;
    private PlayerStrategy strategy;
    private List<PieceMove> moveForEachPiece;

    @BeforeEach
    void setUp() {
        blue = new Player(Colour.BLUE);
        board = new Board(List.of(blue));
        strategy = new BlueCyclicStrategy(new MysteryCell(new FixedPicker()));
        blue.getPieces().get(0).moveToStart(13, Direction.CLOCKWISE);
        blue.getPieces().get(1).moveToStart(18, Direction.CLOCKWISE);
        blue.getPieces().get(2).moveToStart(23, Direction.CLOCKWISE);
        blue.getPieces().get(3).moveToStart(28, Direction.CLOCKWISE);
        moveForEachPiece = List.of(
                new StepMove(blue, blue.getPieces().get(0), board, 1),
                new StepMove(blue, blue.getPieces().get(1), board, 1),
                new StepMove(blue, blue.getPieces().get(2), board, 1),
                new StepMove(blue, blue.getPieces().get(3), board, 1));
    }

    @Test
    void movesB1FirstThenB2OnItsNextTurn() {
        PieceMove firstChoice = strategy.chooseMove(moveForEachPiece, blue);
        PieceMove secondChoice = strategy.chooseMove(moveForEachPiece, blue);

        assertSame(moveForEachPiece.get(0), firstChoice);
        assertSame(moveForEachPiece.get(1), secondChoice);
    }

    @Test
    void counterClockwisePiecePrefersLandingOnTheMysteryCell() {
        Piece blue3 = blue.getPieces().get(2);
        blue3.moveToStart(23, Direction.COUNTER_CLOCKWISE);
        strategy = new BlueCyclicStrategy(mysteryCellAt(22));

        PieceMove chosen = strategy.chooseMove(moveForEachPiece, blue);

        assertSame(blue3, chosen.getPiece());
    }

    @Test
    void clockwisePieceAvoidsLandingOnTheMysteryCell() {
        strategy = new BlueCyclicStrategy(mysteryCellAt(14));

        PieceMove chosen = strategy.chooseMove(moveForEachPiece, blue);

        assertEquals("B2", chosen.getPiece().getName());
    }

    private MysteryCell mysteryCellAt(int cell) {
        MysteryCell mysteryCell = new MysteryCell(new FixedPicker(cell));
        mysteryCell.endRound(board, new RecordingListener());
        mysteryCell.endRound(board, new RecordingListener());
        return mysteryCell;
    }
}