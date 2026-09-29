package com.ludo.strategy;

import com.ludo.model.Player;
import com.ludo.move.PieceMove;

import java.util.List;
import java.util.Optional;

public class GreenBlockingStrategy implements PlayerStrategy {

    @Override
    public PieceMove chooseMove(List<PieceMove> options, Player self) {
        Optional<PieceMove> blockMaker = options.stream().filter(PieceMove::createsBlock).findFirst();
        if (blockMaker.isPresent()) {
            return blockMaker.get();
        }
        Optional<PieceMove> entry = MoveRanking.firstEntry(options);
        if (entry.isPresent()) {
            return entry.get();
        }
        List<PieceMove> keepBlocks = options.stream().filter(move -> !move.breaksBlock()).toList();
        return MoveRanking.closestToHome(keepBlocks.isEmpty() ? options : keepBlocks);
    }
}