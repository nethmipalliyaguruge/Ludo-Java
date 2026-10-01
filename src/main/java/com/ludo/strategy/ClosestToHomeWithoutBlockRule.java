package com.ludo.strategy;

import com.ludo.move.PieceMove;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class ClosestToHomeWithoutBlockRule extends ChainedRule {

    ClosestToHomeWithoutBlockRule(MoveRule next) {
        super(next);
    }

    @Override
    protected Optional<PieceMove> tryToChoose(List<PieceMove> options) {
        List<PieceMove> movesWithoutBlock = new ArrayList<>();
        for (PieceMove move : options) {
            if (!move.createsBlock()) {
                movesWithoutBlock.add(move);
            }
        }
        if (movesWithoutBlock.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(ClosestToHomeRule.closestToHome(movesWithoutBlock));
    }
}
