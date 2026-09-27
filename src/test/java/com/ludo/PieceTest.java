package com.ludo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PieceTest {
    @Test
    void newPieceStartsInBase() {
        Piece piece = new Piece(Colour.RED, 1);
        assertTrue(piece.isInBase());
    }

    @Test
    void pieceNameIsColourLetterFollowedByNumber() {
        Piece piece = new Piece(Colour.GREEN, 3);
        assertEquals("G3", piece.getName());
    }

    @Test
    void capturedPieceReturnsToBase() {
        Piece piece = new Piece(Colour.BLUE, 2);
        piece.moveToStart(0,Direction.CLOCKWISE);
        piece.returnToBase();
        assertTrue(piece.isInBase());
    }

    @Test
    void pieceMovedToStartIsNotInBase() {
        Piece piece = new Piece(Colour.YELLOW, 4);
        piece.moveToStart(0, Direction.CLOCKWISE);
        assertFalse(piece.isInBase());
    }
    @Test
    void clockwisePieceMovesForwardByRollValue() {
        Piece piece = new Piece(Colour.RED, 1);
        piece.moveToStart(0, Direction.CLOCKWISE);

        piece.move(5);

        assertEquals(5, piece.getPosition());
    }

    @Test
    void counterClockwisePieceWrapsPastCellZero() {
        Piece piece = new Piece(Colour.RED, 1);
        piece.moveToStart(3, Direction.COUNTER_CLOCKWISE);

        piece.move(5);

        assertEquals(50, piece.getPosition());
    }
}
