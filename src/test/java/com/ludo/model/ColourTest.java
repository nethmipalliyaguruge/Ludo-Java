package com.ludo.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ColourTest {
    @Test
    void yellowStartsAtCellZero() {
        assertEquals(0, Colour.YELLOW.getStartCell());
    }

    @Test
    void eachApproachCellIsTwoCellsBeforeItsStart() {
        for (Colour colour : Colour.values()) {
            int twoCellsBeforeStart = Math.floorMod(colour.getStartCell() - 2, BoardTest.STANDARD_PATH_LENGTH);
            assertEquals(twoCellsBeforeStart, colour.getApproachCell());
        }
    }

    @Test
    void everyStartCellIsOnTheStandardPath() {
        for (Colour colour : Colour.values()) {
            int startCell = colour.getStartCell();
            assertTrue(startCell >= 0 && startCell < BoardTest.STANDARD_PATH_LENGTH);
        }
    }
}
