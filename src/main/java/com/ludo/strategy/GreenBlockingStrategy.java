package com.ludo.strategy;

import com.ludo.model.Player;
import com.ludo.move.PieceMove;

import java.util.List;

public class GreenBlockingStrategy implements PlayerStrategy {
    // Make a block, keep the base empty, take a needed capture, move the whole block, then avoid breaking blocks
    private final MoveRule rules =
            new BlockMakerRule(
                    new EntryRule(
                            new NeededCaptureRule(
                                    new WholeBlockRule(
                                            new ClosestToHomeKeepingBlockRule(
                                                    new ClosestToHomeRule())))));

    @Override
    public PieceMove chooseMove(List<PieceMove> options, Player self) {
        return rules.choose(options);
    }
}
