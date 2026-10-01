package com.ludo.model;

import com.ludo.exception.IllegalMoveException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PieceTest {

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
    void pieceOnItsStartCellIsFiftySixStepsFromHome() {
        Piece piece = new Piece(Colour.RED, 1);
        piece.moveToStart(26, Direction.CLOCKWISE);

        assertEquals(56, piece.stepsToHome());
    }

    @Test
    void counterClockwisePieceOnItsStartCellIsSixtyStepsFromHome() {
        Piece piece = new Piece(Colour.RED, 1);
        piece.moveToStart(26, Direction.COUNTER_CLOCKWISE);

        assertEquals(60, piece.stepsToHome());
    }

    @Test
    void counterClockwisePieceThatPassedItsApproachNoLongerNeedsAnExtraLap() {
        Piece piece = new Piece(Colour.RED, 1);
        piece.moveToStart(26, Direction.COUNTER_CLOCKWISE);
        piece.move(3);

        assertEquals(57, piece.stepsToHome());
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

    @Test
    void counterClockwisePieceLandingOnApproachForTheFirstTimeMustGoRoundAgain() {
        Piece red = new Piece(Colour.RED, 1);
        red.moveToStart(26, Direction.COUNTER_CLOCKWISE);
        red.recordCapture();
        red.move(2);

        red.move(1);

        assertEquals("23", red.describeLocation());
    }

    @Test
    void counterClockwisePieceLandingOnApproachForTheSecondTimeEntersHomePathNext() {
        Piece red = new Piece(Colour.RED, 1);
        red.moveToStart(26, Direction.COUNTER_CLOCKWISE);
        red.recordCapture();
        red.move(3);
        for (int lap = 0; lap < 8; lap++) {
            red.move(6);
        }
        red.move(3);

        red.move(1);

        assertEquals("redhomepath0", red.describeLocation());
    }

    @Test
    void counterClockwisePieceThatHasNotCapturedKeepsCirclingPastItsApproach() {
        Piece red = new Piece(Colour.RED, 1);
        red.moveToStart(26, Direction.COUNTER_CLOCKWISE);
        red.move(3);
        for (int lap = 0; lap < 8; lap++) {
            red.move(6);
        }

        red.move(5);

        assertEquals("22", red.describeLocation());
    }
}
