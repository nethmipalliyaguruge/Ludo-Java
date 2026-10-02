package com.ludo.move;

import com.ludo.event.GameEventListener;
import com.ludo.model.Board;
import com.ludo.model.Piece;
import com.ludo.model.Player;

import java.util.List;

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
    // Template Method: every move follows the same steps; subclasses only fill in performMove and announceMove
    public final void execute(GameEventListener events) {
        String from = piece.describeLocation();
        performMove();
        announceMove(events, from);
        captureOpponentsOnLandingCell(events);
    }

    public abstract boolean landsOnStandardPath();

    public abstract int landingCell();

    protected abstract void performMove();

    protected abstract void announceMove(GameEventListener events, String from);

    public Piece getPiece() {
        return piece;
    }

    @Override
    public List<Piece> movedPieces() {
        return List.of(piece);
    }

    public List<Piece> opponentsCaptured() {
        if (!landsOnStandardPath()) {
            return List.of();
        }
        return board.opponentsAt(landingCell(), piece.getColour());
    }

    @Override
    public boolean capturesOpponent() {
        return !opponentsCaptured().isEmpty();
    }

    public boolean createsBlock() {
        return landsOnStandardPath() && board.hasOwnPieceAt(landingCell(), piece.getColour());
    }

    public boolean isEntry() {
        return piece.isInBase();
    }

    public boolean movesWholeBlock() {
        return false;
    }

    public boolean breaksBlock() {
        return piece.isOnStandardPath() && board.isBlockAt(piece.getPosition());
    }

    private void captureOpponentsOnLandingCell(GameEventListener events) {
        if (!piece.isOnStandardPath()) {
            return;
        }
        List<Piece> captured = board.opponentsAt(piece.getPosition(), piece.getColour());
        for (Piece opponent : captured) {
            opponent.returnToBase();
            events.onCapture(owner, piece, opponent);
        }
        if (!captured.isEmpty()) {
            for (Piece mover : movedPieces()) {
                mover.recordCapture();
            }
        }
    }
}
