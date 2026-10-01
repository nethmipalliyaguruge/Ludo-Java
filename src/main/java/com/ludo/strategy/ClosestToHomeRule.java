package com.ludo.strategy;

import com.ludo.move.PieceMove;

import java.util.List;

class ClosestToHomeRule implements MoveRule {

    @Override
    public PieceMove choose(List<PieceMove> options) {
        return closestToHome(options);
    }

    static PieceMove closestToHome(List<PieceMove> moves) {
        PieceMove closest = moves.getFirst();
        for (PieceMove move : moves) {
            if (move.getPiece().stepsToHome() < closest.getPiece().stepsToHome()) {
                closest = move;
            }
        }
        return closest;
    }
}
