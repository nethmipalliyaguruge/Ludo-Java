package com.ludo.event;

import com.ludo.model.Piece;
import com.ludo.model.Player;

public interface GameEventListener {
    default void onPieceEnteredBoard(Player owner, Piece piece) {
    }

    default void onPieceMoved(Piece piece, String from, String to, int steps) {
    }

    default void onCapture(Player attacker, Piece attackingPiece, Piece capturedPiece) {
    }

    default void onNoMoveAvailable(Player player) {
    }
}
