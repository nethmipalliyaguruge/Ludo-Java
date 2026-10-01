package com.ludo.move;

import com.ludo.event.RecordingListener;
import com.ludo.model.Board;
import com.ludo.model.Colour;
import com.ludo.model.Direction;
import com.ludo.model.Piece;
import com.ludo.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StepMoveTest {
    private Player red;
    private Piece redOne;
    private Board board;
    private RecordingListener listener;

    @BeforeEach
    void setUp() {
        red = new Player(Colour.RED);
        redOne = red.getPieces().getFirst();
        board = new Board(List.of(red));
        listener = new RecordingListener();
    }

    @Test
    void landingCellIsTheRollAwayInThePiecesDirection() {
        redOne.moveToStart(26, Direction.COUNTER_CLOCKWISE);

        StepMove move = new StepMove(red, redOne, board, 4);

        assertEquals(22, move.landingCell());
    }

    @Test
    void movingIntoTheHomePathDoesNotLandOnTheStandardPath() {
        redOne.moveToStart(22, Direction.CLOCKWISE);
        redOne.recordCapture();

        assertFalse(new StepMove(red, redOne, board, 4).landsOnStandardPath());
    }

    @Test
    void executingMovesThePieceAndReportsBothLocations() {
        redOne.moveToStart(26, Direction.CLOCKWISE);

        new StepMove(red, redOne, board, 5).execute(listener);

        assertEquals(31, redOne.getPosition());
        assertEquals(List.of("moved R1 26->31"), listener.getEvents());
    }

    @Test
    void movingOnePieceOutOfABlockBreaksIt() {
        Piece redTwo = red.getPieces().get(1);
        redOne.moveToStart(26, Direction.CLOCKWISE);
        redTwo.moveToStart(26, Direction.CLOCKWISE);

        assertTrue(new StepMove(red, redOne, board, 3).breaksBlock());
    }
}
