package com.ludo.output;

import com.ludo.game.PlayerStatus;
import com.ludo.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

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

    @Test
    void gameStartNamesEachPlayersFourPieces() {
        reporter.onGameStart(List.of(new Player(Colour.RED)));

        assertEquals("The red player has four (04) pieces named R1, R2, R3, and R4.", output.toString().trim());
    }

    @Test
    void roundOrderNamesTheStarterAndTheOrder() {
        List<Player> order = List.of(new Player(Colour.RED), new Player(Colour.GREEN),
                new Player(Colour.YELLOW), new Player(Colour.BLUE));

        reporter.onRoundOrderDecided(order);

        assertEquals(List.of("Red player has the highest roll and will begin the game.",
                "The order of a single round is Red, Green, Yellow, and Blue."), printedLines());
    }

    @Test
    void enteringPieceIsReportedWithPieceCounts() {
        Player red = new Player(Colour.RED);
        Piece redOne = red.getPieces().getFirst();
        redOne.moveToStart(26, Direction.CLOCKWISE);

        reporter.onPieceEnteredBoard(red, redOne);

        assertEquals(List.of("Red player moves piece R1 to the starting point.",
                "Red player now has 1/4 pieces on the board and 3/4 pieces on the base."), printedLines());
    }

    @Test
    void captureIsReportedInTheBriefsFormat() {
        Player red = new Player(Colour.RED);
        Piece redOne = red.getPieces().getFirst();
        Piece blueOne = new Player(Colour.BLUE).getPieces().getFirst();
        redOne.moveToStart(29, Direction.CLOCKWISE);

        reporter.onCapture(red, redOne, blueOne);

        assertEquals("Red piece R1 lands on square 29, captures Blue piece B1, and returns it to the base.",
                printedLines().getFirst());
    }

    @Test
    void roundEndShowsEachPlayersPieceLocations() {
        Player red = new Player(Colour.RED);
        red.getPieces().getFirst().moveToStart(26, Direction.CLOCKWISE);

        reporter.onRoundEnd(3, List.of(PlayerStatus.of(red)));

        List<String> lines = printedLines();
        assertTrue(lines.contains("Red player now has 1/4 pieces on the board and 3/4 pieces on the base."));
        assertTrue(lines.contains("Location of pieces Red"));
        assertTrue(lines.contains("Piece R1 -> 26"));
        assertTrue(lines.contains("Piece R2 -> Base"));
    }

    @Test
    void firstPlayerToFinishWins() {
        reporter.onPlayerFinished(new Player(Colour.GREEN), 1);

        assertEquals("Green player wins!!!", output.toString().trim());
    }

    @Test
    void mysteryCellCountdownShowsRoundsLeft() {
        reporter.onMysteryCellCountdown(12, 3);

        assertEquals("The mystery cell is at 12 and will be at that location for the next 3 rounds.",
                output.toString().trim());
    }

    @Test
    void sickPieceIsReportedInTheBriefsFormat() {
        reporter.onSick(new Player(Colour.YELLOW).getPieces().getFirst());

        assertEquals("Yellow piece Y1 feels sick, and movement speed halves.", output.toString().trim());
    }

    @Test
    void briefingIsReportedInTheBriefsFormat() {
        reporter.onBriefing(new Player(Colour.RED).getPieces().getFirst());

        assertEquals("Red piece R1 attends briefing and cannot move for four rounds.", output.toString().trim());
    }

    @Test
    void briefingEscapeIsReportedInTheBriefsFormat() {
        reporter.onSentToBaseFromBriefing(new Player(Colour.RED).getPieces().getFirst());

        assertEquals("Red piece R1 is movement-restricted and has rolled three consecutively."
                + " Teleporting piece R1 to base.", output.toString().trim());
    }

    @Test
    void gammaDirectionChangeIsReportedInTheBriefsFormat() {
        reporter.onTurnedCounterClockwise(new Player(Colour.BLUE).getPieces().getFirst());

        assertEquals("The Blue piece B1, which was moving clockwise, has changed to moving counterclockwise.",
                output.toString().trim());
    }

    @Test
    void gammaToBetaIsReportedInTheBriefsFormat() {
        reporter.onSentFromGammaToBeta(new Player(Colour.BLUE).getPieces().getFirst());

        assertEquals("The Blue piece B1 is moving in a counterclockwise direction. Teleporting to Beta from Gamma.",
                output.toString().trim());
    }

    @Test
    void moveUpToTheBlockIsReportedInTheBriefsFormat() {
        Piece greenOne = new Player(Colour.GREEN).getPieces().getFirst();
        greenOne.moveToStart(3, Direction.CLOCKWISE);

        reporter.onMovedUpToBlock(greenOne);

        assertEquals("Green does not have other pieces in the board to move instead of the blocked piece."
                + " Moved the piece to square 3 which is the cell before the block.", output.toString().trim());
    }

    @Test
    void ignoredBlockedThrowIsReportedInTheBriefsFormat() {
        reporter.onBlockedThrowIgnored(new Player(Colour.GREEN).getPieces().getFirst());

        assertEquals("Green does not have other pieces in the board to move instead of the blocked piece."
                + " Ignoring the throw and moving on to the next player.", output.toString().trim());
    }

    @Test
    void blockMoveNamesEveryPieceInTheBlock() {
        Player red = new Player(Colour.RED);
        List<Piece> block = red.getPieces().subList(0, 2);

        reporter.onBlockMoved(red, block, "10", "7", 3, Direction.COUNTER_CLOCKWISE);

        assertEquals("Red moves the block of R1 and R2 from location 10 to 7 by 3 units in counter-clockwise direction.",
                output.toString().trim());
    }

    @Test
    void finalResultListsEveryPlace() {
        List<Player> ranking = List.of(new Player(Colour.GREEN), new Player(Colour.RED));

        reporter.onGameOver(ranking);

        List<String> lines = printedLines();
        assertTrue(lines.contains("Place 1: Green"));
        assertTrue(lines.contains("Place 2: Red"));
    }

    @Test
    void firstPlayerRollIsReportedInTheBriefsFormat() {
        reporter.onFirstPlayerRoll(new Player(Colour.YELLOW), 4);

        assertEquals("Yellow rolls 4", output.toString().trim());
    }

    @Test
    void noMoveAvailableIgnoresTheThrow() {
        reporter.onNoMoveAvailable(new Player(Colour.RED));

        assertEquals("Red has no piece that can move with this roll. Ignoring the throw and moving on to the next player.",
                output.toString().trim());
    }

    @Test
    void thirdSixIsReportedAsIgnored() {
        reporter.onThirdSixIgnored(new Player(Colour.BLUE));

        assertEquals("Blue rolled a six three times in a row. The roll is ignored and the dice passes to the next player.",
                output.toString().trim());
    }

    @Test
    void brokenBlockadeIsReported() {
        reporter.onBlockadeBroken(new Player(Colour.GREEN));

        assertTrue(output.toString().startsWith("Green rolled a six three times in a row while holding a blockade."));
    }

    private List<String> printedLines() {
        return output.toString().trim().lines().toList();
    }
}