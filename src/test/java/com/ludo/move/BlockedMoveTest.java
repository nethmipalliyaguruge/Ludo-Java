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

class BlockedMoveTest {
    private Player green;
    private Piece greenOne;
    private Board board;
    private RecordingListener listener;

    @BeforeEach
    void setUp() {
        green = new Player(Colour.GREEN);
        Player red = new Player(Colour.RED);
        greenOne = green.getPieces().getFirst();
        board = new Board(List.of(green, red));
        listener = new RecordingListener();
        red.getPieces().get(0).moveToStart(4, Direction.CLOCKWISE);
        red.getPieces().get(1).moveToStart(4, Direction.CLOCKWISE);
    }

    @Test
    void pieceStopsOnTheCellBeforeTheBlock() {
        greenOne.moveToStart(0, Direction.CLOCKWISE);

        new BlockedMove(green, greenOne, board, 6, 3).execute(listener);

        assertEquals(3, greenOne.getPosition());
    }

    @Test
    void blockIsReportedWithTheIntendedCellAndTheBlockingPiece() {
        greenOne.moveToStart(0, Direction.CLOCKWISE);

        new BlockedMove(green, greenOne, board, 6, 3).execute(listener);

        assertEquals(List.of("G1 blocked 0->6 by R1", "G1 moved up to block"), listener.getEvents());
    }

    @Test
    void pieceRightBehindTheBlockStaysAndTheThrowIsIgnored() {
        greenOne.moveToStart(3, Direction.CLOCKWISE);

        new BlockedMove(green, greenOne, board, 2, 0).execute(listener);

        assertEquals(3, greenOne.getPosition());
        assertTrue(listener.getEvents().contains("G1 blocked throw ignored"));
    }

    @Test
    void onlyAPieceThatCannotMoveAtAllIgnoresTheThrow() {
        greenOne.moveToStart(0, Direction.CLOCKWISE);
        BlockedMove movesUpToTheBlock = new BlockedMove(green, greenOne, board, 6, 3);
        greenOne.moveToStart(3, Direction.CLOCKWISE);
        BlockedMove cannotMoveAtAll = new BlockedMove(green, greenOne, board, 2, 0);

        assertFalse(movesUpToTheBlock.ignoresThrow());
        assertTrue(cannotMoveAtAll.ignoresThrow());
    }
}
