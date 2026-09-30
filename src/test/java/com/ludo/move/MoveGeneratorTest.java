package com.ludo.move;

import com.ludo.model.*;
import com.ludo.random.FixedCoinToss;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MoveGeneratorTest {
    private Player red;
    private MoveGenerator generator;

    @BeforeEach
    void setUp() {
        red = new Player(Colour.RED);
        Board board = new Board(List.of(red));
        generator = new MoveGenerator(board, new FixedCoinToss(Direction.CLOCKWISE));
    }

    @Test
    void piecesInBaseHaveNoMovesWithoutASix() {
        List<PieceMove> moves = generator.legalMoves(red, 5);

        assertTrue(moves.isEmpty());
    }

    @Test
    void rollingSixOffersEveryPieceInBaseAnEntryMove() {
        List<PieceMove> moves = generator.legalMoves(red, 6);

        assertEquals(4, moves.size());
        assertInstanceOf(EnterBoardMove.class, moves.getFirst());
    }

    @Test
    void moveLandingOnOwnPieceIsNotAllowed() {
        red.getPieces().get(0).moveToStart(26, Direction.CLOCKWISE);
        red.getPieces().get(1).moveToStart(29, Direction.CLOCKWISE);

        List<PieceMove> moves = generator.legalMoves(red, 3);

        assertEquals(1, moves.size());
        assertEquals("R2", moves.getFirst().getPiece().getName());
    }

    @Test
    void pieceInHomePathHasNoMoveIfRollOvershootsHome() {
        Piece redOne = red.getPieces().getFirst();
        redOne.moveToStart(24, Direction.CLOCKWISE);
        redOne.recordCapture();
        redOne.move(4);

        List<PieceMove> moves = generator.legalMoves(red, 5);

        assertTrue(moves.isEmpty());
    }
}