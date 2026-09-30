package com.ludo.strategy;

import com.ludo.move.PieceMove;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

class ClosestToHomeRule implements MoveRule {
    private final Predicate<PieceMove> preferred;

    private ClosestToHomeRule(Predicate<PieceMove> preferred) {
        this.preferred = preferred;
    }

    static ClosestToHomeRule anyMove() {
        return new ClosestToHomeRule(move -> true);
    }

    static ClosestToHomeRule preferring(Predicate<PieceMove> preferred) {
        return new ClosestToHomeRule(preferred);
    }

    @Override
    public PieceMove choose(List<PieceMove> options) {
        List<PieceMove> preferredMoves = options.stream().filter(preferred).toList();
        List<PieceMove> candidates = preferredMoves.isEmpty() ? options : preferredMoves;
        return candidates.stream()
                .min(Comparator.comparingInt(move -> move.getPiece().stepsToHome()))
                .orElseThrow();
    }
}