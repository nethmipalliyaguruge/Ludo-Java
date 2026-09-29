package com.ludo.strategy;

import com.ludo.model.Player;
import com.ludo.move.PieceMove;

import java.util.List;
import java.util.Optional;

public class YellowWinningStrategy implements PlayerStrategy {

    @Override
    public PieceMove chooseMove(List<PieceMove> options, Player self) {
        Optional<PieceMove> entry = MoveRanking.firstEntry(options);
        if (entry.isPresent()) {
            return entry.get();
        }
        Optional<PieceMove> neededCapture = options.stream()
                .filter(move -> move.getPiece().getCaptureCount() == 0)
                .filter(PieceMove::capturesOpponent)
                .findFirst();
        if (neededCapture.isPresent()) {
            return neededCapture.get();
        }
        return MoveRanking.closestToHome(options);
    }
}