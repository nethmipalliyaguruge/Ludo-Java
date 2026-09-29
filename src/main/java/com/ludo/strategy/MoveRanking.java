package com.ludo.strategy;

import com.ludo.model.Piece;
import com.ludo.move.PieceMove;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

final class MoveRanking {

    private MoveRanking() {
    }

    static Optional<PieceMove> firstEntry(List<PieceMove> options) {
        return options.stream().filter(PieceMove::isEntry).findFirst();
    }

    static PieceMove closestToHome(List<PieceMove> options) {
        return options.stream()
                .min(Comparator.comparingInt(move -> move.getPiece().stepsToHome()))
                .orElseThrow();
    }

    static int closestCapturedOpponent(PieceMove move) {
        return move.opponentsCaptured().stream()
                .mapToInt(Piece::stepsToHome)
                .min()
                .orElse(Integer.MAX_VALUE);
    }
}