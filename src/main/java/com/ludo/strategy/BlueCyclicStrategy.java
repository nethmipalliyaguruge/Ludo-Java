package com.ludo.strategy;

import com.ludo.model.Direction;
import com.ludo.model.MysteryCell;
import com.ludo.model.Piece;
import com.ludo.model.Player;
import com.ludo.move.PieceMove;

import java.util.ArrayList;
import java.util.List;

public class BlueCyclicStrategy implements PlayerStrategy {
    private final MysteryCell mysteryCell;
    private int nextPieceIndex = 0;

    public BlueCyclicStrategy(MysteryCell mysteryCell) {
        this.mysteryCell = mysteryCell;
    }

    @Override
    public PieceMove chooseMove(List<PieceMove> options, Player self) {
        List<PieceMove> inTurnOrder = inCyclicOrder(options, self);
        PieceMove chosen = choosePreferredMove(inTurnOrder);
        List<Piece> pieces = self.getPieces();
        nextPieceIndex = (pieces.indexOf(chosen.getPiece()) + 1) % pieces.size();
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

    private PieceMove choosePreferredMove(List<PieceMove> inTurnOrder) {
        for (PieceMove move : inTurnOrder) {
            if (landsOnMysteryCell(move) && isMoving(move, Direction.COUNTER_CLOCKWISE)) {
                return move;
            }
        }
        for (PieceMove move : inTurnOrder) {
            boolean clockwiseOntoMysteryCell = landsOnMysteryCell(move) && isMoving(move, Direction.CLOCKWISE);
            if (!clockwiseOntoMysteryCell) {
                return move;
            }
        }
        return inTurnOrder.getFirst();
    }

    private boolean landsOnMysteryCell(PieceMove move) {
        return move.landsOnStandardPath() && mysteryCell.isAt(move.landingCell());
    }

    private boolean isMoving(PieceMove move, Direction direction) {
        return move.getPiece().getDirection() == direction;
    }
}
