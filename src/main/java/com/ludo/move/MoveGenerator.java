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
    private static final int UNITS_TO_BREAK_BLOCKADE = 6;

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
        for (List<Piece> block : board.blocksOf(player)) {
            blockMove(player, block, roll).ifPresent(moves::add);
        }
        if (moves.isEmpty()) {
            blockedMove(player, roll).ifPresent(moves::add);
        }
        return moves;
    }

    public List<PieceMove> blockadeBreakingMoves(Player player) {
        List<PieceMove> moves = new ArrayList<>();
        for (List<Piece> block : board.blocksOf(player)) {
            List<Piece> leaving = block.subList(1, block.size());
            int steps = UNITS_TO_BREAK_BLOCKADE / leaving.size();
            for (Piece piece : leaving) {
                if (canWalk(piece, steps) && board.stepsToOpponentBlock(piece, steps).isEmpty()) {
                    moves.add(new StepMove(player, piece, board, steps));
                }
            }
        }
        return moves;
    }

    private Optional<PieceMove> candidateMove(Player player, Piece piece, int roll) {
        if (piece.isInBase() && roll == ENTRY_ROLL && !startIsBlocked(piece)) {
            return Optional.of(new EnterBoardMove(player, piece, board, coin));
        }
        int steps = piece.stepsFor(roll);
        if (canWalk(piece, steps) && board.stepsToOpponentBlock(piece, steps).isEmpty()) {
            return Optional.of(new StepMove(player, piece, board, steps));
        }
        return Optional.empty();
    }

    private Optional<PieceMove> blockedMove(Player player, int roll) {
        for (Piece piece : player.getPieces()) {
            int steps = piece.stepsFor(roll);
            OptionalInt stepsToBlock = board.stepsToOpponentBlock(piece, steps);
            if (canWalk(piece, steps) && stepsToBlock.isPresent()) {
                return Optional.of(new BlockedMove(player, piece, board, steps, stepsToBlock.getAsInt() - 1));
            }
        }
        return Optional.empty();
    }

    private Optional<PieceMove> blockMove(Player player, List<Piece> block, int roll) {
        if (!hasOppositeDirections(block) || !allFreeToMove(block, roll)) {
            return Optional.empty();
        }
        BlockMove move = new BlockMove(player, block, board, roll);
        return move.canTravel() ? Optional.of(move) : Optional.empty();
    }

    private boolean hasOppositeDirections(List<Piece> block) {
        return block.stream().map(Piece::getDirection).distinct().count() > 1;
    }

    private boolean allFreeToMove(List<Piece> block, int roll) {
        return block.stream().allMatch(piece -> piece.stepsFor(roll) > 0);
    }

    private boolean canWalk(Piece piece, int steps) {
        return steps > 0 && piece.canMoveBy(steps);
    }

    private boolean startIsBlocked(Piece piece) {
        return board.isOpponentBlockAt(piece.getColour().getStartCell(), piece.getColour());
    }
}