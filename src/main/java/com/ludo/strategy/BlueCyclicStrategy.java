package com.ludo.strategy;

import com.ludo.model.Piece;
import com.ludo.model.Player;
import com.ludo.move.PieceMove;

import java.util.List;

public class BlueCyclicStrategy implements PlayerStrategy {
    private int nextPieceIndex = 0;

    @Override
    public PieceMove chooseMove(List<PieceMove> options, Player self) {
        List<Piece> pieces = self.getPieces();
        for (int offset = 0; offset < pieces.size(); offset++) {
            int index = (nextPieceIndex + offset) % pieces.size();
            for (PieceMove move : options) {
                if (move.getPiece() == pieces.get(index)) {
                    nextPieceIndex = (index + 1) % pieces.size();
                    return move;
                }
            }
        }
        return options.getFirst();
    }
}