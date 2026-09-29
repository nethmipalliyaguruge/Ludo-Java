package com.ludo.strategy;

import com.ludo.model.Player;
import com.ludo.move.PieceMove;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class RedAggressiveStrategy implements PlayerStrategy {

    @Override
    public PieceMove chooseMove(List<PieceMove> options, Player self) {
        Optional<PieceMove> capture = bestCapture(options);
        if (capture.isPresent()) {
            return capture.get();
        }
        Optional<PieceMove> entry = MoveRanking.firstEntry(options);
        if (entry.isPresent()) {
            return entry.get();
        }
        return MoveRanking.closestToHome(avoidBlocksIfPossible(options));
    }

    private Optional<PieceMove> bestCapture(List<PieceMove> options) {
        return options.stream()
                .filter(PieceMove::capturesOpponent)
                .min(Comparator.comparingInt(MoveRanking::closestCapturedOpponent));
    }

    private List<PieceMove> avoidBlocksIfPossible(List<PieceMove> options) {
        List<PieceMove> noBlocks = options.stream().filter(move -> !move.createsBlock()).toList();
        return noBlocks.isEmpty() ? options : noBlocks;
    }
}