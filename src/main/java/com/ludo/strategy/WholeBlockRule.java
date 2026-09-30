package com.ludo.strategy;

import com.ludo.move.PieceMove;

import java.util.List;
import java.util.Optional;

class WholeBlockRule extends ChainedRule {

    WholeBlockRule(MoveRule next) {
        super(next);
    }

    @Override
    protected Optional<PieceMove> tryToChoose(List<PieceMove> options) {
        return options.stream().filter(PieceMove::movesWholeBlock).findFirst();
    }
}