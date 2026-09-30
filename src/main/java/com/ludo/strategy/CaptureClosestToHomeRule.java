package com.ludo.strategy;

import com.ludo.model.Piece;
import com.ludo.move.PieceMove;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

class CaptureClosestToHomeRule extends ChainedRule {

    CaptureClosestToHomeRule(MoveRule next) {
        super(next);
    }

    @Override
    protected Optional<PieceMove> tryToChoose(List<PieceMove> options) {
        return options.stream()
                .filter(PieceMove::capturesOpponent)
                .min(Comparator.comparingInt(CaptureClosestToHomeRule::closestCapturedOpponent));
    }

    private static int closestCapturedOpponent(PieceMove move) {
        return move.opponentsCaptured().stream()
                .mapToInt(Piece::stepsToHome)
                .min()
                .orElse(Integer.MAX_VALUE);
    }
}