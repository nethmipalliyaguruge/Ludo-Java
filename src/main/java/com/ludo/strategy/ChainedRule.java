package com.ludo.strategy;

import com.ludo.move.PieceMove;

import java.util.List;
import java.util.Optional;

abstract class ChainedRule implements MoveRule {
    private final MoveRule next;

    protected ChainedRule(MoveRule next) {
        this.next = next;
    }

    @Override
    public final PieceMove choose(List<PieceMove> options) {
        return tryToChoose(options).orElseGet(() -> next.choose(options));
    }

    protected abstract Optional<PieceMove> tryToChoose(List<PieceMove> options);
}