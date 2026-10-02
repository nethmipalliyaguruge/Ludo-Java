package com.ludo.model;

import com.ludo.event.GameEventListener;
import com.ludo.random.Picker;

import java.util.ArrayList;
import java.util.List;

public class MysteryCell {
    public static final int ROUNDS_AT_ONE_CELL = 4;
    private static final int ROUNDS_BEFORE_FIRST_SPAWN = 2;

    private final Picker picker;
    private final TeleportEffects effects;
    private boolean onBoard = false;
    private int cell;
    private int roundsLeft;
    private int roundsWithPiecesOnBoard = 0;

    public MysteryCell(Picker picker) {
        this.picker = picker;
        this.effects = new TeleportEffects(picker);
    }

    public boolean isAt(int position) {
        return onBoard && cell == position;
    }

    public void endRound(Board board, GameEventListener events) {
        if (onBoard) {
            countDown(board, events);
        } else {
            waitForFirstSpawn(board, events);
        }
    }

    public void teleportIfLandedOn(Piece piece, GameEventListener events) {
        if (!piece.isOnStandardPath() || !isAt(piece.getPosition())) {
            return;
        }
        TeleportDestination destination = picker.pickOneOf(List.of(TeleportDestination.values()));
        destination.sendHere(piece);
        events.onTeleported(piece, destination);
        effects.applyOnArrival(piece, destination, events);
    }

    private void waitForFirstSpawn(Board board, GameEventListener events) {
        if (board.hasPiecesOnStandardPath()) {
            roundsWithPiecesOnBoard++;
        }
        // T-10: the first mystery cell appears after two rounds with pieces on the standard path
        if (roundsWithPiecesOnBoard == ROUNDS_BEFORE_FIRST_SPAWN) {
            spawn(board, events);
        }
    }

    private void countDown(Board board, GameEventListener events) {
        roundsLeft--;
        if (roundsLeft == 0) {
            spawn(board, events);
        } else {
            events.onMysteryCellCountdown(cell, roundsLeft);
        }
    }

    private void spawn(Board board, GameEventListener events) {
        cell = picker.pickOneOf(emptyCellsExceptCurrent(board));
        onBoard = true;
        roundsLeft = ROUNDS_AT_ONE_CELL;
        events.onMysteryCellSpawned(cell, roundsLeft);
    }

    private List<Integer> emptyCellsExceptCurrent(Board board) {
        List<Integer> cells = new ArrayList<>();
        for (int candidate = 0; candidate < Board.STANDARD_PATH_LENGTH; candidate++) {
            // T-10: it must spawn on an empty cell and never in the same place twice in a row
            if (!isAt(candidate) && board.piecesAt(candidate).isEmpty()) {
                cells.add(candidate);
            }
        }
        return cells;
    }
}