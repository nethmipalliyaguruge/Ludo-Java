package com.ludo.strategy;

import com.ludo.model.Player;
import com.ludo.move.PieceMove;

import java.util.List;

public class RedAggressiveStrategy implements PlayerStrategy {
    // Capture first, then enter without making a block, then move closest to home avoiding blocks
    private final MoveRule rules =
            new CaptureClosestToHomeRule(
                    new EntryWithoutBlockRule(
                            new ClosestToHomeWithoutBlockRule(
                                    new ClosestToHomeRule())));

    @Override
    public PieceMove chooseMove(List<PieceMove> options, Player self) {
        return rules.choose(options);
    }
}
