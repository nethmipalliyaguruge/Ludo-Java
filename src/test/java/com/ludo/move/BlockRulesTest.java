package com.ludo.move;

import com.ludo.model.*;
import com.ludo.random.FixedCoinToss;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class BlockRulesTest {
    private Player green;
    private Player red;
    private MoveGenerator generator;

    @BeforeEach
    void setUp() {
        green = new Player(Colour.GREEN);
        red = new Player(Colour.RED);
        Board board = new Board(List.of(green, red));
        generator = new MoveGenerator(board, new FixedCoinToss(Direction.CLOCKWISE));
        red.getPieces().get(0).moveToStart(4, Direction.CLOCKWISE);
        red.getPieces().get(1).moveToStart(4, Direction.CLOCKWISE);
    }

    @Test
    void pieceCannotJumpOverAnOpponentBlockWhenAnotherPieceCanMove() {
        green.getPieces().get(0).moveToStart(0, Direction.CLOCKWISE);
        green.getPieces().get(1).moveToStart(20, Direction.CLOCKWISE);

        List<PieceMove> moves = generator.legalMoves(green, 5);

        assertEquals(1, moves.size());
        assertEquals("G2", moves.getFirst().getPiece().getName());
    }

    @Test
    void ownBlockDoesNotStopOwnPieces() {
        Piece redThree = red.getPieces().get(2);
        redThree.moveToStart(1, Direction.CLOCKWISE);

        List<PieceMove> moves = generator.legalMoves(red, 6);

        assertInstanceOf(StepMove.class, moves.getFirst());
    }
}