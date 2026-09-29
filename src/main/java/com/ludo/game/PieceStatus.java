package com.ludo.game;

import com.ludo.model.Colour;
import com.ludo.model.Piece;
import com.ludo.model.Player;

import java.util.ArrayList;
import java.util.List;

public record PieceStatus(String name, String location) {
    public record PlayerStatus(Colour colour, int piecesOnBoard, int piecesInBase, List<PieceStatus> pieces) {

        public static PlayerStatus of(Player player) {
            List<PieceStatus> pieces = new ArrayList<>();
            for (Piece piece : player.getPieces()) {
                pieces.add(new PieceStatus(piece.getName(), piece.describeLocation()));
            }
            return new PlayerStatus(player.getColour(), player.countPiecesOnBoard(), player.countPiecesInBase(), List.copyOf(pieces));
        }
    }
}
