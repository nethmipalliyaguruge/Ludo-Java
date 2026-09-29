package com.ludo.move;

import com.ludo.event.GameEventListener;
import com.ludo.model.Board;
import com.ludo.model.Piece;
import com.ludo.model.Player;
import com.ludo.random.CoinToss;

public class EnterBoardMove extends PieceMove {
    private final CoinToss coin;

    public EnterBoardMove(Player owner, Piece piece, Board board, CoinToss coin) {
        super(owner, piece, board);
        this.coin = coin;
    }

    @Override
    public boolean landsOnStandardPath() {
        return true;
    }

    @Override
    public int landingCell() {
        return piece.getColour().getStartCell();
    }

    @Override
    protected void performMove() {
        piece.moveToStart(landingCell(), coin.toss());
    }

    @Override
    protected void announceMove(GameEventListener events, String from) {
        events.onPieceEnteredBoard(owner, piece);
    }
}