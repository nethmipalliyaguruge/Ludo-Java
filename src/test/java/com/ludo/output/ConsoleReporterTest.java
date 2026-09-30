package com.ludo.output;

import com.ludo.model.Colour;
import com.ludo.model.Direction;
import com.ludo.model.Piece;
import com.ludo.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

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
}