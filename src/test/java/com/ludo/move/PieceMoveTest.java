package com.ludo.move;

import com.ludo.event.RecordingListener;
import com.ludo.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    void noMoveOnlyAnnouncesThatTheTurnIsPassed() {
        Move move = new NoMove(red);

        move.execute(listener);

        assertEquals(List.of("no move RED"), listener.getEvents());
    }
}