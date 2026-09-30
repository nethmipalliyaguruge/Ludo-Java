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
        return options.stream()
                .filter(move -> move.getPiece().getCaptureCount() == 0)
                .filter(PieceMove::capturesOpponent)
                .findFirst();
    }
}