package com.ludo.model;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

public class Board {
    public static final int STANDARD_PATH_LENGTH = 52;
    public static final int HOME_PATH_LENGTH = 5;
    private static final int MIN_PIECES_FOR_BLOCK = 2;

    private final List<Player> players;

    public Board(List<Player> players) {
        this.players = List.copyOf(players);
    }

    public List<Piece> piecesAt(int cell) {
        List<Piece> found = new ArrayList<>();
        for (Player player : players) {
            for (Piece piece : player.getPieces()) {
                if (piece.isOnStandardPath() && piece.getPosition() == cell) {
                    found.add(piece);
                }
            }
        }
        return found;
    }

    public boolean hasPiecesOnStandardPath() {
        for (Player player : players) {
            for (Piece piece : player.getPieces()) {
                if (piece.isOnStandardPath()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isBlockAt(int cell) {
        return piecesAt(cell).size() >= MIN_PIECES_FOR_BLOCK;
    }

    public List<List<Piece>> blocksOf(Player player) {
        List<List<Piece>> blocks = new ArrayList<>();
        for (Piece piece : player.getPieces()) {
            if (piece.isOnStandardPath()) {
                List<Piece> together = ownPiecesAt(piece.getPosition(), player);
                if (together.size() >= MIN_PIECES_FOR_BLOCK && !blocks.contains(together)) {
                    blocks.add(together);
                }
            }
        }
        return blocks;
    }

    private List<Piece> ownPiecesAt(int cell, Player player) {
        List<Piece> together = new ArrayList<>();
        for (Piece piece : player.getPieces()) {
            if (piece.isOnStandardPath() && piece.getPosition() == cell) {
                together.add(piece);
            }
        }
        return together;
    }

    public List<Piece> opponentsAt(int cell, Colour ownColour) {
        List<Piece> opponents = new ArrayList<>();
        for (Piece piece : piecesAt(cell)) {
            if (piece.getColour() != ownColour) {
                opponents.add(piece);
            }
        }
        return opponents;
    }

    public boolean hasOwnPieceAt(int cell, Colour ownColour) {
        for (Piece piece : piecesAt(cell)) {
            if (piece.getColour() == ownColour) {
                return true;
            }
        }
        return false;
    }

    public boolean isOpponentBlockAt(int cell, Colour ownColour) {
        // All pieces in a block share one colour, so checking the first piece is enough
        return isBlockAt(cell) && piecesAt(cell).getFirst().getColour() != ownColour;
    }

    // T-3: returns how many steps away the first opponent block is, so the piece can stop in front of it
    public OptionalInt stepsToOpponentBlock(Piece piece, int steps) {
        if (!piece.isOnStandardPath()) {
            return OptionalInt.empty();
        }
        for (int step = 1; step <= steps && piece.staysOnStandardPathAfter(step); step++) {
            if (isOpponentBlockAt(piece.cellAfter(step), piece.getColour())) {
                return OptionalInt.of(step);
            }
        }
        return OptionalInt.empty();
    }
}
