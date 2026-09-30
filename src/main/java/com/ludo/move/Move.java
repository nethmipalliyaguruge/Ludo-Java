package com.ludo.move;

import com.ludo.event.GameEventListener;
import com.ludo.model.Piece;

import java.util.Optional;

public interface Move {
    void execute(GameEventListener events);

    default boolean capturesOpponent() {
        return false;
    }

    default Optional<Piece> movedPiece() {
        return Optional.empty();
    }
}
