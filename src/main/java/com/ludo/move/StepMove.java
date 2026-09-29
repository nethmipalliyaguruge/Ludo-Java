package com.ludo.move;

import com.ludo.event.GameEventListener;
import com.ludo.model.Board;
import com.ludo.model.Piece;
import com.ludo.model.Player;

public class StepMove extends PieceMove {
    private final int steps;

    public StepMove(Player owner, Piece piece, Board board, int steps) {
        super(owner, piece, board);
        this.steps = steps;
    }

    @Override
    public boolean landsOnStandardPath() {
        return piece.isOnStandardPath() && piece.staysOnStandardPathAfter(steps);
    }

    @Override
    public int landingCell() {
        return piece.cellAfter(steps);
    }

    @Override
    protected void performMove() {
        piece.move(steps);
    }

    @Override
    protected void announceMove(GameEventListener events, String from) {
        events.onPieceMoved(piece, from, piece.describeLocation(), steps);
    }
}
