package com.ludo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MoveGenerator {
    private static final int ENTRY_ROLL = 6;

    private final Board board;
    private final CoinToss coin;

    public MoveGenerator(Board board, CoinToss coin) {
        this.board = board;
        this.coin = coin;
    }

    public List<PieceMove> legalMoves(Player player, int roll) {
        List<PieceMove> moves = new ArrayList<>();
        for (Piece piece : player.getPieces()) {
            Optional<PieceMove> candidate = candidateMove(player, piece, roll);
            if (candidate.isPresent() && !candidate.get().landsOnOwnPiece()) {
                moves.add(candidate.get());
            }
        }
        return moves;
    }

    private Optional<PieceMove> candidateMove(Player player, Piece piece, int roll) {
        if (piece.isInBase() && roll == ENTRY_ROLL) {
            return Optional.of(new EnterBoardMove(player, piece, board, coin));
        }
        if (piece.canMoveBy(roll)) {
            return Optional.of(new StepMove(player, piece, board, roll));
        }
        return Optional.empty();
    }
}