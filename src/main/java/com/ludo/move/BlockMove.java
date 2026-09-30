package com.ludo.move;

import com.ludo.event.GameEventListener;
import com.ludo.model.Board;
import com.ludo.model.Direction;
import com.ludo.model.Piece;
import com.ludo.model.Player;

import java.util.Comparator;
import java.util.List;

public class BlockMove extends PieceMove {
    private final List<Piece> block;
    private final Direction direction;
    private final int steps;

    public BlockMove(Player owner, List<Piece> block, Board board, int roll) {
        super(owner, pieceFurthestFromHome(block), board);
        this.block = List.copyOf(block);
        this.direction = piece.getDirection();
        this.steps = roll / block.size();
    }

    public boolean canTravel() {
        if (steps == 0) {
            return false;
        }
        for (int step = 1; step < steps; step++) {
            if (board.isOpponentBlockAt(cellAfter(step), piece.getColour())) {
                return false;
            }
        }
        return !board.isOpponentBlockAt(landingCell(), piece.getColour())
                || board.piecesAt(landingCell()).size() == block.size();
    }

    @Override
    public List<Piece> movedPieces() {
        return block;
    }

    @Override
    public boolean landsOnStandardPath() {
        return true;
    }

    @Override
    public int landingCell() {
        return cellAfter(steps);
    }

    @Override
    protected void performMove() {
        int destination = landingCell();
        for (Piece member : block) {
            member.moveWithBlockTo(destination);
        }
    }

    @Override
    protected void announceMove(GameEventListener events, String from) {
        events.onBlockMoved(owner, block, from, piece.describeLocation(), steps, direction);
    }

    private int cellAfter(int step) {
        return direction.move(piece.getPosition(), step);
    }

    private static Piece pieceFurthestFromHome(List<Piece> block) {
        return block.stream().max(Comparator.comparingInt(Piece::stepsToHome)).orElseThrow();
    }

    public boolean isEntry() {
        return piece.isInBase();
    }

    public boolean movesWholeBlock() {
        return false;
    }

    @Override
    public boolean breaksBlock() {
        return true;
    }
}