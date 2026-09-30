package com.ludo.output;

import com.ludo.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConsoleReporterTest {
    private ByteArrayOutputStream output;
    private ConsoleReporter reporter;

    @BeforeEach
    void setUp() {
        output = new ByteArrayOutputStream();
        reporter = new ConsoleReporter(new PrintStream(output));
    }

    @Test
    void rollIsReportedInTheBriefsFormat() {
        reporter.onRoll(new Player(Colour.RED), 6);

        assertEquals("Red player rolled 6.", output.toString().trim());
    }

    @Test
    void moveIsReportedWithLocationsStepsAndDirection() {
        Piece greenTwo = new Player(Colour.GREEN).getPieces().get(1);
        greenTwo.moveToStart(39, Direction.COUNTER_CLOCKWISE);

        reporter.onPieceMoved(greenTwo, "39", "35", 4);

        assertEquals("Green moves piece G2 from location 39 to 35 by 4 units in counter-clockwise direction.",
                output.toString().trim());
    }

    @Test
    void stalemateMessageShowsTheRound() {
        reporter.onStalemate(120);

        assertTrue(output.toString().contains("Stalemate after round 120"));
    }

    @Test
    void mysteryCellSpawnIsReportedInTheBriefsFormat() {
        reporter.onMysteryCellSpawned(12, 4);

        assertEquals("A mystery cell has spawned in location 12 and will be at this location for the next four rounds.",
                output.toString().trim());
    }

    @Test
    void teleportIsReportedWithTheNewLocationAndDestination() {
        Piece redOne = new Player(Colour.RED).getPieces().getFirst();
        redOne.moveToStart(7, Direction.CLOCKWISE);

        reporter.onTeleported(redOne, TeleportDestination.ALPHA);

        assertEquals("Red player lands on a mystery cell and is teleported to 7."
                + System.lineSeparator() + "Red piece R1 teleported to Alpha.", output.toString().trim());
    }

    @Test
    void energisedPieceIsReportedInTheBriefsFormat() {
        Piece blueTwo = new Player(Colour.BLUE).getPieces().get(1);

        reporter.onEnergised(blueTwo);

        assertEquals("Blue piece B2 feels energized, and movement speed doubles.", output.toString().trim());
    }

    @Test
    void blockedPieceIsReportedWithTheBlockingPiece() {
        Piece greenOne = new Player(Colour.GREEN).getPieces().getFirst();
        Piece redOne = new Player(Colour.RED).getPieces().getFirst();

        reporter.onPieceBlocked(greenOne, "0", "6", redOne);

        assertEquals("Green piece G1 is blocked from moving from 0 to 6 by Red piece R1.", output.toString().trim());
    }
}