package com.ludo.strategy;

import com.ludo.move.PieceMove;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

class EntryRule extends ChainedRule {
    private final Predicate<PieceMove> allowed;

    EntryRule(MoveRule next) {
        this(move -> true, next);
    }

    EntryRule(Predicate<PieceMove> allowed, MoveRule next) {
        super(next);
        this.allowed = allowed;
    }

    @Override
    protected Optional<PieceMove> tryToChoose(List<PieceMove> options) {
        return options.stream().filter(PieceMove::isEntry).filter(allowed).findFirst();
    }
}
