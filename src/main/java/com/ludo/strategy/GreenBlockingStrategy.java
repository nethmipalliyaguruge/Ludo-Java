package com.ludo.strategy;

import com.ludo.model.Player;
import com.ludo.move.PieceMove;

import java.util.List;

public class GreenBlockingStrategy implements PlayerStrategy {
    private final MoveRule rules =
            new BlockMakerRule(
                    new EntryRule(
                            new NeededCaptureRule(
                                    new WholeBlockRule(
                                            ClosestToHomeRule.preferring(move -> !move.breaksBlock())))));

    @Override
    public PieceMove chooseMove(List<PieceMove> options, Player self) {
        return rules.choose(options);
    }
}