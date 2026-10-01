package com.ludo.move;

import com.ludo.model.Board;
import com.ludo.model.Direction;
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
            Optional<PieceMove> move = candidateMove(player, piece, roll);
            if (move.isPresent()) {
                moves.add(move.get());
            }
        }
        for (List<Piece> block : board.blocksOf(player)) {
            Optional<PieceMove> move = blockMove(player, block, roll);
            if (move.isPresent()) {
                moves.add(move.get());
            }
        }
        if (moves.isEmpty()) {
            Optional<PieceMove> move = blockedMove(player, roll);
            if (move.isPresent()) {
                moves.add(move.get());
            }
        }
        return moves;
    }

    public List<PieceMove> blockadeBreakingMoves(Player player) {
        List<PieceMove> moves = new ArrayList<>();
        for (List<Piece> block : board.blocksOf(player)) {
            List<Piece> leaving = block.subList(1, block.size());
            int steps = UNITS_TO_BREAK_BLOCKADE / leaving.size();
            for (Piece piece : leaving) {
                boolean allowedToMove = piece.stepsFor(steps) > 0;
                if (allowedToMove && canWalkFreely(piece, steps)) {
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
        if (canWalkFreely(piece, steps)) {
            return Optional.of(new StepMove(player, piece, board, steps));
        }
        return Optional.empty();
    }

    private Optional<PieceMove> blockedMove(Player player, int roll) {
        for (Piece piece : player.getPieces()) {
            int steps = piece.stepsFor(roll);
            OptionalInt stepsToBlock = board.stepsToOpponentBlock(piece, steps);
            if (canWalk(piece, steps) && stepsToBlock.isPresent()) {
                int stepsBeforeBlock = stepsToBlock.getAsInt() - 1;
                return Optional.of(new BlockedMove(player, piece, board, steps, stepsBeforeBlock));
            }
        }
        return Optional.empty();
    }

    private Optional<PieceMove> blockMove(Player player, List<Piece> block, int roll) {
        if (!allFreeToMove(block, roll)) {
            return Optional.empty();
        }
        BlockMove move = new BlockMove(player, block, board, roll);
        boolean movesAsAUnit = hasOppositeDirections(block) || move.capturesBlockade();
        if (movesAsAUnit && move.canTravel()) {
            return Optional.of(move);
        }
        return Optional.empty();
    }

    private boolean hasOppositeDirections(List<Piece> block) {
        Direction firstDirection = block.getFirst().getDirection();
        for (Piece piece : block) {
            if (piece.getDirection() != firstDirection) {
                return true;
            }
        }
        return false;
    }

    private boolean allFreeToMove(List<Piece> block, int roll) {
        for (Piece piece : block) {
            if (piece.stepsFor(roll) == 0) {
                return false;
            }
        }
        return true;
    }

    private boolean canWalkFreely(Piece piece, int steps) {
        return canWalk(piece, steps) && board.stepsToOpponentBlock(piece, steps).isEmpty();
    }

    private boolean canWalk(Piece piece, int steps) {
        return steps > 0 && piece.canMoveBy(steps);
    }

    private boolean startIsBlocked(Piece piece) {
        return board.isOpponentBlockAt(piece.getColour().getStartCell(), piece.getColour());
    }
}
