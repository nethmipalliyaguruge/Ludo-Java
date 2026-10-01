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
    void pieceInHomePathHasNoMoveIfRollOvershootsHome() {
        Piece redOne = red.getPieces().getFirst();
        redOne.moveToStart(24, Direction.CLOCKWISE);
        redOne.recordCapture();
        redOne.move(4);

        List<PieceMove> moves = generator.legalMoves(red, 5);

        assertTrue(moves.isEmpty());
    }

    @Test
    void landingOnOwnPieceIsAllowedAndFormsABlock() {
        red.getPieces().get(0).moveToStart(26, Direction.CLOCKWISE);
        red.getPieces().get(1).moveToStart(29, Direction.CLOCKWISE);

        List<PieceMove> moves = generator.legalMoves(red, 3);

        assertEquals(2, moves.size());
        assertTrue(moves.getFirst().createsBlock());
    }

    @Test
    void breakingATwoPieceBlockadeMovesOnePieceSixUnits() {
        Player red = new Player(Colour.RED);
        red.getPieces().get(0).moveToStart(30, Direction.CLOCKWISE);
        red.getPieces().get(1).moveToStart(30, Direction.CLOCKWISE);
        MoveGenerator breaker = new MoveGenerator(new Board(List.of(red)), new FixedCoinToss(Direction.CLOCKWISE));

        List<PieceMove> moves = breaker.blockadeBreakingMoves(red);

        assertEquals(1, moves.size());
        assertEquals(36, moves.getFirst().landingCell());
    }

    @Test
    void breakingAThreePieceBlockadeSharesTheSixUnits() {
        Player red = new Player(Colour.RED);
        red.getPieces().get(0).moveToStart(30, Direction.CLOCKWISE);
        red.getPieces().get(1).moveToStart(30, Direction.CLOCKWISE);
        red.getPieces().get(2).moveToStart(30, Direction.CLOCKWISE);
        MoveGenerator breaker = new MoveGenerator(new Board(List.of(red)), new FixedCoinToss(Direction.CLOCKWISE));

        List<PieceMove> moves = breaker.blockadeBreakingMoves(red);

        assertEquals(2, moves.size());
        assertEquals(33, moves.getFirst().landingCell());
    }

    @Test
    void pieceCanJumpOverASingleOpponentPiece() {
        Player blue = new Player(Colour.BLUE);
        MoveGenerator twoPlayers = new MoveGenerator(new Board(List.of(red, blue)), new FixedCoinToss(Direction.CLOCKWISE));
        red.getPieces().getFirst().moveToStart(26, Direction.CLOCKWISE);
        blue.getPieces().getFirst().moveToStart(28, Direction.CLOCKWISE);

        List<PieceMove> moves = twoPlayers.legalMoves(red, 4);

        assertEquals(30, moves.getFirst().landingCell());
    }

    @Test
    void pieceInBriefingIsNotForcedOutOfABlockade() {
        red.getPieces().get(0).moveToStart(30, Direction.CLOCKWISE);
        red.getPieces().get(1).moveToStart(30, Direction.CLOCKWISE);
        red.getPieces().get(1).applyCondition(new BriefingCondition(4, 0));

        assertTrue(generator.blockadeBreakingMoves(red).isEmpty());
    }
}
