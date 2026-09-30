package com.ludo.model;

import com.ludo.exception.IllegalMoveException;
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
        piece.moveToStart(0, Direction.CLOCKWISE);
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

    @Test
    void returningToBaseResetsTheCaptureCount() {
        Piece piece = new Piece(Colour.RED, 3);
        piece.moveToStart(26, Direction.CLOCKWISE);
        piece.recordCapture();
        piece.returnToBase();
        assertEquals(0, piece.getCaptureCount());
    }

    @Test
    void piecePassingApproachCellEntersHomePath() {
        Piece piece = new Piece(Colour.RED, 2);
        piece.moveToStart(22, Direction.CLOCKWISE);
        piece.recordCapture();
        piece.move(4);
        assertEquals("redhomepath1", piece.describeLocation());
    }

    @Test
    void pieceLandingExactlyOnApproachCellStaysOnStandardPath() {
        Piece piece = new Piece(Colour.RED, 2);
        piece.moveToStart(22, Direction.CLOCKWISE);
        piece.move(2);
        assertEquals("24", piece.describeLocation());
    }

    @Test
    void pieceInHomePathReachesHomeWithExactRoll() {
        Piece piece = new Piece(Colour.RED, 2);
        piece.moveToStart(24, Direction.CLOCKWISE);
        piece.recordCapture();
        piece.move(4);
        piece.move(2);
        assertTrue(piece.isHome());
    }

    @Test
    void movingPieceInBaseThrowsIllegalMoveException() {
        Piece piece = new Piece(Colour.RED, 4);
        assertThrows(IllegalMoveException.class, () -> piece.move(3));
    }

    @Test
    void pieceOnItsStartCellIsFiftySixStepsFromHome(){
        Piece piece = new Piece(Colour.RED,1);
        piece.moveToStart(26,Direction.CLOCKWISE);

        assertEquals(56,piece.stepsToHome());
    }

    @Test
    void counterClockwisePieceEntersHomePathOnSecondPass() {
        Piece red = new Piece(Colour.RED, 1);
        red.moveToStart(26, Direction.COUNTER_CLOCKWISE);
        red.recordCapture();
        red.move(3);
        for (int lap = 0; lap < 8; lap++) {
            red.move(6);
        }

        red.move(5);

        assertEquals("redhomepath1", red.describeLocation());
    }
}
