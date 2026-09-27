package com.ludo;

import java.util.ArrayList;
import java.util.List;

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

    public boolean isBlockAt(int cell) {
        return piecesAt(cell).size() >= MIN_PIECES_FOR_BLOCK;
    }
}
