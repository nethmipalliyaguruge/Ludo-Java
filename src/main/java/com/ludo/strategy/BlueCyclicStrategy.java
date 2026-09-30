package com.ludo.strategy;

import com.ludo.model.Direction;
import com.ludo.model.MysteryCell;
import com.ludo.model.Piece;
import com.ludo.model.Player;
import com.ludo.move.PieceMove;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class BlueCyclicStrategy implements PlayerStrategy {
    private final MysteryCell mysteryCell;
    private int nextPieceIndex = 0;

    public BlueCyclicStrategy(MysteryCell mysteryCell) {
        this.mysteryCell = mysteryCell;
    }

    @Override
    public PieceMove chooseMove(List<PieceMove> options, Player self) {
        List<PieceMove> inTurnOrder = inCyclicOrder(options, self);
        PieceMove chosen = firstMatching(inTurnOrder, this::landsOnMysteryCounterClockwise)
                .or(() -> firstMatching(inTurnOrder, move -> !landsOnMysteryClockwise(move)))
                .orElse(inTurnOrder.getFirst());
        nextPieceIndex = (self.getPieces().indexOf(chosen.getPiece()) + 1) % self.getPieces().size();
        return chosen;
    }

    private List<PieceMove> inCyclicOrder(List<PieceMove> options, Player self) {
        List<Piece> pieces = self.getPieces();
        List<PieceMove> ordered = new ArrayList<>();
        for (int offset = 0; offset < pieces.size(); offset++) {
            Piece turnPiece = pieces.get((nextPieceIndex + offset) % pieces.size());
            for (PieceMove move : options) {
                if (move.getPiece() == turnPiece) {
                    ordered.add(move);
                }
            }
        }
        return ordered;
    }

    private Optional<PieceMove> firstMatching(List<PieceMove> moves, Predicate<PieceMove> condition) {
        return moves.stream().filter(condition).findFirst();
    }

    private boolean landsOnMysteryCounterClockwise(PieceMove move) {
        return landsOnMysteryCell(move) && move.getPiece().getDirection() == Direction.COUNTER_CLOCKWISE;
    }

    private boolean landsOnMysteryClockwise(PieceMove move) {
        return landsOnMysteryCell(move) && move.getPiece().getDirection() == Direction.CLOCKWISE;
    }

    private boolean landsOnMysteryCell(PieceMove move) {
        return move.landsOnStandardPath() && mysteryCell.isAt(move.landingCell());
    }
}