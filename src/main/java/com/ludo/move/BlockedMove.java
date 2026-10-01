package com.ludo.move;

import com.ludo.event.GameEventListener;
import com.ludo.model.Board;
import com.ludo.model.Piece;
import com.ludo.model.Player;

public class BlockedMove extends PieceMove {
    private final int stepsBeforeBlock;
    private final String intendedCell;
    private final Piece blocker;

    public BlockedMove(Player owner, Piece piece, Board board, int roll, int stepsBeforeBlock) {
        super(owner, piece, board);
        this.stepsBeforeBlock = stepsBeforeBlock;
        this.intendedCell = String.valueOf(piece.cellAfter(roll));
        this.blocker = board.piecesAt(piece.cellAfter(stepsBeforeBlock + 1)).getFirst();
    }

    @Override
    public boolean landsOnStandardPath() {
        return true;
    }

    @Override
    public boolean ignoresThrow() {
        return stepsBeforeBlock == 0;
    }

    @Override
    public int landingCell() {
        return piece.cellAfter(stepsBeforeBlock);
    }

    @Override
    protected void performMove() {
        if (stepsBeforeBlock > 0) {
            piece.move(stepsBeforeBlock);
        }
    }

    @Override
    protected void announceMove(GameEventListener events, String from) {
        events.onPieceBlocked(piece, from, intendedCell, blocker);
        if (stepsBeforeBlock > 0) {
            events.onMovedUpToBlock(piece);
        } else {
            events.onBlockedThrowIgnored(piece);
        }
    }
}