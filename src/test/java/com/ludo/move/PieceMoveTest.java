package com.ludo.move;

import com.ludo.event.RecordingListener;
import com.ludo.model.*;
import com.ludo.random.FixedCoinToss;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PieceMoveTest {
    private Player red;
    private Player blue;
    private Board board;
    private RecordingListener listener;

    @BeforeEach
    void setUp() {
        red = new Player(Colour.RED);
        blue = new Player(Colour.BLUE);
        board = new Board(List.of(red, blue));
        listener = new RecordingListener();
    }

    @Test
    void landingOnOpponentSendsItBackToBase() {
        Piece redOne = red.getPieces().getFirst();
        Piece blueOne = blue.getPieces().getFirst();
        redOne.moveToStart(26, Direction.CLOCKWISE);
        blueOne.moveToStart(30, Direction.CLOCKWISE);

        new StepMove(red, redOne, board, 4).execute(listener);

        assertTrue(blueOne.isInBase());
        assertEquals(1, redOne.getCaptureCount());
    }

    @Test
    void stepMoveCanBeInspectedBeforeItRuns() {
        Piece redOne = red.getPieces().getFirst();
        Piece blueOne = blue.getPieces().getFirst();
        redOne.moveToStart(26, Direction.CLOCKWISE);
        blueOne.moveToStart(30, Direction.CLOCKWISE);

        PieceMove move = new StepMove(red, redOne, board, 4);

        assertTrue(move.capturesOpponent());
        assertEquals(26, redOne.getPosition());
    }

    @Test
    void enterBoardMoveUsesTheCoinTossDirection() {
        Piece redOne = red.getPieces().getFirst();
        Move move = new EnterBoardMove(red, redOne, board,
                new FixedCoinToss(Direction.COUNTER_CLOCKWISE));

        move.execute(listener);

        assertEquals(26, redOne.getPosition());
        assertEquals(Direction.COUNTER_CLOCKWISE, redOne.getDirection());
    }

    @Test
    void noMoveOnlyAnnouncesThatTheTurnIsPassed() {
        Move move = new NoMove(red);

        move.execute(listener);

        assertEquals(List.of("no move RED"), listener.getEvents());
    }
}