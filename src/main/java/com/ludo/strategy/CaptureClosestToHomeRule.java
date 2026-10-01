package com.ludo.strategy;

import com.ludo.model.Piece;
import com.ludo.move.PieceMove;

import java.util.List;
import java.util.Optional;

class CaptureClosestToHomeRule extends ChainedRule {

    CaptureClosestToHomeRule(MoveRule next) {
        super(next);
    }

    @Override
    protected Optional<PieceMove> tryToChoose(List<PieceMove> options) {
        PieceMove bestCapture = null;
        int bestDistance = Integer.MAX_VALUE;
        for (PieceMove move : options) {
            for (Piece opponent : move.opponentsCaptured()) {
                if (opponent.stepsToHome() < bestDistance) {
                    bestDistance = opponent.stepsToHome();
                    bestCapture = move;
                }
            }
        }
        return Optional.ofNullable(bestCapture);
    }
}
