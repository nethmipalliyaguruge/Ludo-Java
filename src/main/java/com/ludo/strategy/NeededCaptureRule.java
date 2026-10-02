package com.ludo.strategy;

import com.ludo.move.PieceMove;

import java.util.List;
import java.util.Optional;

class NeededCaptureRule extends ChainedRule {

    NeededCaptureRule(MoveRule next) {
        super(next);
    }

    @Override
    protected Optional<PieceMove> tryToChoose(List<PieceMove> options) {
        for (PieceMove move : options) {
            // T-7: a piece without a capture cannot enter its home straight
            boolean stillNeedsACapture = move.getPiece().getCaptureCount() == 0;
            if (stillNeedsACapture && move.capturesOpponent()) {
                return Optional.of(move);
            }
        }
        return Optional.empty();
    }
}
