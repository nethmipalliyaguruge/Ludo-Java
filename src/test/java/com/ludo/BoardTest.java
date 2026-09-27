package com.ludo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    public static final int STANDARD_PATH_LENGTH = 52;
    public static final int RED_START = Colour.RED.getStartCell();
    private Player red;
    private Board board;

    @BeforeEach
    void setUP() {
        red = new Player(Colour.RED);
        board = new Board(List.of(red));
    }

    @Test
    void pieceOnStandardPathIsFoundAtItsCell() {
        Piece redOne = red.getPieces().getFirst();
        redOne.moveToStart(RED_START, Direction.CLOCKWISE);

        List<Piece> pieces = board.piecesAt(RED_START);

        assertEquals(List.of(redOne), pieces);
    }

    @Test
    void piecesInBaseAreNotOnAnyCell() {
        for (int cell = 0; cell < Board.STANDARD_PATH_LENGTH; cell++) {
            assertTrue(board.piecesAt(cell).isEmpty());
        }
    }

    @Test
    void twoPiecesOfSameColourOnOneCellFormABlock() {
        red.getPieces().get(0).moveToStart(RED_START, Direction.CLOCKWISE);
        red.getPieces().get(1).moveToStart(RED_START, Direction.CLOCKWISE);
        assertTrue(board.isBlockAt(RED_START));
    }

    @Test
    void singlePieceOnACellIsNotABlock() {
        red.getPieces().getFirst().moveToStart(RED_START, Direction.CLOCKWISE);
        assertFalse(board.isBlockAt(RED_START));
    }
}
