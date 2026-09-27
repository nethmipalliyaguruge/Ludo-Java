package com.ludo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player {
    private static final int PIECES_PER_PLAYER = 4;

    private final Colour colour;
    private final List<Piece> pieces;

    public Player(Colour colour) {
        this.colour = colour;
        this.pieces = new ArrayList<>();
        for (int number = 1; number <= PIECES_PER_PLAYER; number++) {
            pieces.add(new Piece(colour, number));
        }
    }

    public Colour getColour() {
        return colour;
    }

    public List<Piece> getPieces() {
        return Collections.unmodifiableList(pieces);
    }

    public int countPiecesInBase() {
        int count = 0;
        for (Piece piece : pieces) {
            if (piece.isInBase()) {
                count++;
            }
        }
        return count;
    }

    public int countPiecesOnBoard() {
        return PIECES_PER_PLAYER - countPiecesInBase() - countPiecesHome();
    }

    public boolean hasWon() {
        return countPiecesHome() == PIECES_PER_PLAYER;
    }

    private int countPiecesHome() {
        int count = 0;
        for (Piece piece : pieces) {
            if (piece.isHome()) {
                count++;
            }
        }
        return count;
    }
}
