package com.ludo.move;

import com.ludo.model.Board;
import com.ludo.model.Piece;
import com.ludo.model.Player;
import com.ludo.random.CoinToss;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

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
            candidateMove(player, piece, roll).ifPresent(moves::add);
        }
        if (moves.isEmpty()) {
            blockedMove(player, roll).ifPresent(moves::add);
        }
        return moves;
    }

    private Optional<PieceMove> candidateMove(Player player, Piece piece, int roll) {
        if (piece.isInBase() && roll == ENTRY_ROLL && !startIsBlocked(piece)) {
            return Optional.of(new EnterBoardMove(player, piece, board, coin));
        }
        if (piece.canMoveBy(roll) && board.stepsToOpponentBlock(piece, roll).isEmpty()) {
            return Optional.of(new StepMove(player, piece, board, roll));
        }
        return Optional.empty();
    }

    private Optional<PieceMove> blockedMove(Player player, int roll) {
        for (Piece piece : player.getPieces()) {
            OptionalInt stepsToBlock = board.stepsToOpponentBlock(piece, roll);
            if (piece.canMoveBy(roll) && stepsToBlock.isPresent()) {
                return Optional.of(new BlockedMove(player, piece, board, roll, stepsToBlock.getAsInt() - 1));
            }
        }
        return Optional.empty();
    }

    private boolean startIsBlocked(Piece piece) {
        return board.isOpponentBlockAt(piece.getColour().getStartCell(), piece.getColour());
    }
}