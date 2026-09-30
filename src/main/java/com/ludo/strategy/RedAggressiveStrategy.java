package com.ludo.strategy;

import com.ludo.model.Player;
import com.ludo.move.PieceMove;

import java.util.List;

public class RedAggressiveStrategy implements PlayerStrategy {
    private final MoveRule rules =
            new CaptureClosestToHomeRule(
                    new EntryRule(
                            ClosestToHomeRule.preferring(move -> !move.createsBlock())));

    @Override
    public PieceMove chooseMove(List<PieceMove> options, Player self) {
        return rules.choose(options);
    }
}