package com.ludo.move;

import com.ludo.event.GameEventListener;
import com.ludo.model.Piece;

import java.util.List;

public interface Move {
    void execute(GameEventListener events);

    default boolean capturesOpponent() {
        return false;
    }

    default boolean ignoresThrow() {
        return false;
    }

    default List<Piece> movedPieces() {
        return List.of();
    }
}
