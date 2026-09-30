package com.ludo.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    @Test
    void newPlayerHasAllFourPiecesInBase() {
        Player player = new Player(Colour.RED);
        assertEquals(4, player.countPiecesInBase());
    }

    @Test
    void piecesAreNamedWithColourLetterAndNumbersOneToFour() {
        Player player = new Player(Colour.RED);
        List<Piece> pieces = player.getPieces();
        assertEquals("R1", pieces.get(0).getName());
        assertEquals("R4", pieces.get(3).getName());
    }

    @Test
    void movingOnePieceToStartLeavesThreeInBaseAndOneOnBoard() {
        Player player = new Player(Colour.GREEN);
        Piece firstPiece = player.getPieces().getFirst();

        firstPiece.moveToStart(Colour.GREEN.getStartCell(), Direction.CLOCKWISE);
        assertEquals(3, player.countPiecesInBase());
        assertEquals(1, player.countPiecesOnBoard());
    }

    @Test
    void piecesListCannotBeChangedFromOutside() {
        Player player = new Player(Colour.BLUE);
        assertThrows(UnsupportedOperationException.class, () -> player.getPieces().clear());

    }

    @Test
    void newPlayerIsTwoHundredAndTwentyEightStepsFromHome() {
        Player player = new Player(Colour.RED);

        assertEquals(228, player.totalStepsToHome());
    }
}
