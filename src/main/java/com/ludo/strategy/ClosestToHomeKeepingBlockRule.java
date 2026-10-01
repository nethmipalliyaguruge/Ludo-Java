package com.ludo.strategy;

import com.ludo.move.PieceMove;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class ClosestToHomeKeepingBlockRule extends ChainedRule {

    ClosestToHomeKeepingBlockRule(MoveRule next) {
        super(next);
    }

    @Override
    protected Optional<PieceMove> tryToChoose(List<PieceMove> options) {
        List<PieceMove> movesKeepingBlocks = new ArrayList<>();
        for (PieceMove move : options) {
            if (!move.breaksBlock()) {
                movesKeepingBlocks.add(move);
            }
        }
        if (movesKeepingBlocks.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(ClosestToHomeRule.closestToHome(movesKeepingBlocks));
    }
}
