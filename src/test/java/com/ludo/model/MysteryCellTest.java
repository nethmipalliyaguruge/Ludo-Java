package com.ludo.model;

import com.ludo.event.RecordingListener;
import com.ludo.random.FixedPicker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MysteryCellTest {
    private Player red;
    private Board board;
    private RecordingListener listener;

    @BeforeEach
    void setUp() {
        red = new Player(Colour.RED);
        board = new Board(List.of(red));
        listener = new RecordingListener();
    }

    @Test
    void doesNotSpawnWhileNoPieceIsOnTheStandardPath() {
        MysteryCell mysteryCell = new MysteryCell(new FixedPicker(10));

        mysteryCell.endRound(board, listener);
        mysteryCell.endRound(board, listener);

        assertTrue(listener.getEvents().isEmpty());
    }

    @Test
    void spawnsAfterTwoRoundsWithPiecesOnTheStandardPath() {
        red.getPieces().getFirst().moveToStart(26, Direction.CLOCKWISE);
        MysteryCell mysteryCell = new MysteryCell(new FixedPicker(10));

        mysteryCell.endRound(board, listener);
        mysteryCell.endRound(board, listener);

        assertEquals(List.of("mystery spawned at 10"), listener.getEvents());
        assertTrue(mysteryCell.isAt(10));
    }

    @Test
    void cannotSpawnOnACellThatHasAPiece() {
        red.getPieces().getFirst().moveToStart(26, Direction.CLOCKWISE);
        MysteryCell mysteryCell = new MysteryCell(new FixedPicker(26));

        mysteryCell.endRound(board, listener);

        assertThrows(IllegalStateException.class, () -> mysteryCell.endRound(board, listener));
    }

    @Test
    void staysForFourRoundsThenMovesToANewCell() {
        MysteryCell mysteryCell = spawnedAt(10, 30);

        for (int round = 0; round < 4; round++) {
            mysteryCell.endRound(board, listener);
        }

        assertEquals(List.of("mystery spawned at 10", "mystery at 10 for 3", "mystery at 10 for 2",
                "mystery at 10 for 1", "mystery spawned at 30"), listener.getEvents());
    }

    @Test
    void cannotReappearInTheSameCell() {
        MysteryCell mysteryCell = spawnedAt(10, 10);

        for (int round = 0; round < 3; round++) {
            mysteryCell.endRound(board, listener);
        }

        assertThrows(IllegalStateException.class, () -> mysteryCell.endRound(board, listener));
    }

    @Test
    void pieceLandingOnTheMysteryCellIsTeleported() {
        MysteryCell mysteryCell = spawnedAt(10, TeleportDestination.ALPHA, true);
        Piece red1 = red.getPieces().getFirst();
        red1.teleportTo(10);

        mysteryCell.teleportIfLandedOn(red1, listener);

        assertEquals(7, red1.getPosition());
        assertEquals(List.of("mystery spawned at 10", "R1 teleported to Alpha", "R1 energised"),
                listener.getEvents());
    }

    @Test
    void pieceOnAnotherCellIsNotTeleported() {
        MysteryCell mysteryCell = spawnedAt(10);
        Piece red1 = red.getPieces().getFirst();

        mysteryCell.teleportIfLandedOn(red1, listener);

        assertEquals(26, red1.getPosition());
    }

    private MysteryCell spawnedAt(Object... picks) {
        red.getPieces().getFirst().moveToStart(26, Direction.CLOCKWISE);
        MysteryCell mysteryCell = new MysteryCell(new FixedPicker(picks));
        mysteryCell.endRound(board, listener);
        mysteryCell.endRound(board, listener);
        return mysteryCell;
    }
}