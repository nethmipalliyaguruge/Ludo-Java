package com.ludo.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TeleportDestinationTest {
    private Piece red1;

    @BeforeEach
    void setUp() {
        red1 = new Player(Colour.RED).getPieces().getFirst();
        red1.moveToStart(26, Direction.CLOCKWISE);
    }

    @Test
    void alphaBetaAndGammaAreCountedFromTheYellowApproach() {
        assertEquals(7, TeleportDestination.cellFromYellowApproach(9));
        assertEquals(25, TeleportDestination.cellFromYellowApproach(27));
        assertEquals(44, TeleportDestination.cellFromYellowApproach(46));
    }

    @Test
    void approachUsesThePiecesOwnColour() {
        TeleportDestination.APPROACH.sendHere(red1);

        assertEquals(24, red1.getPosition());
    }

    @Test
    void baseSendsThePieceBackToBase() {
        TeleportDestination.BASE.sendHere(red1);

        assertTrue(red1.isInBase());
    }
}