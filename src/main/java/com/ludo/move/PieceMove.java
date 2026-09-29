package com.ludo.move;

import com.ludo.event.GameEventListener;
import com.ludo.model.Board;
import com.ludo.model.Piece;
import com.ludo.model.Player;

public abstract class PieceMove implements Move {
    protected final Player owner;
    protected final Piece piece;
    protected final Board board;

    protected PieceMove(Player owner, Piece piece, Board board) {
        this.owner = owner;
        this.piece = piece;
        this.board = board;
    }

    @Override
    public final void execute(GameEventListener events) {
        String from = piece.describeLocation();
        performMove();
        announceMove(events, from);
        captureOpponentsOnLandingCell(events);
    }

    public Piece getPiece() {
        return piece;
    }

    public boolean capturesOpponent() {
        return landsOnStandardPath()
                && !board.opponentsAt(landingCell(), piece.getColour()).isEmpty();
    }

    public boolean landsOnOwnPiece() {
        return landsOnStandardPath()
                && board.hasOwnPieceAt(landingCell(), piece.getColour());
    }

    public abstract boolean landsOnStandardPath();

    public abstract int landingCell();

    protected abstract void performMove();

    protected abstract void announceMove(GameEventListener events, String from);

    private void captureOpponentsOnLandingCell(GameEventListener events) {
        if (!piece.isOnStandardPath()) {
            return;
        }
        for (Piece opponent : board.opponentsAt(piece.getPosition(), piece.getColour())) {
            opponent.returnToBase();
            piece.recordCapture();
            events.onCapture(owner, piece, opponent);
        }
    }
}
