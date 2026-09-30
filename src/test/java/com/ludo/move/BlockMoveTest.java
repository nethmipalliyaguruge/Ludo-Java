package com.ludo.move;

import com.ludo.event.RecordingListener;
import com.ludo.model.Board;
import com.ludo.model.Colour;
import com.ludo.model.Direction;
import com.ludo.model.Piece;
import com.ludo.model.Player;
import com.ludo.random.FixedCoinToss;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BlockMoveTest {
    private Player red;
    private Player green;
    private Piece red1;
    private Piece red2;
    private MoveGenerator generator;

    @BeforeEach
    void setUp() {
        red = new Player(Colour.RED);
        green = new Player(Colour.GREEN);
        red1 = red.getPieces().get(0);
        red2 = red.getPieces().get(1);
        red1.moveToStart(10, Direction.CLOCKWISE);
        red2.moveToStart(10, Direction.COUNTER_CLOCKWISE);
        generator = new MoveGenerator(new Board(List.of(red, green)), new FixedCoinToss(Direction.CLOCKWISE));
    }

    @Test
    void mixedBlockMovesTogetherByTheRollDividedByItsSize() {
        blockMove(6).execute(new RecordingListener());

        assertEquals(7, red1.getPosition());
        assertEquals(7, red2.getPosition());
    }

    @Test
    void blockMovesInTheDirectionOfThePieceFurthestFromHome() {
        BlockMove move = blockMove(6);

        assertSame(red2, move.getPiece());
    }

    @Test
    void blockOfPiecesGoingTheSameWayDoesNotMoveAsAUnit() {
        red1.turnCounterClockwise();

        assertTrue(blockMoves(6).isEmpty());
    }

    @Test
    void piecesKeepTheirOwnDirectionAfterMovingWithTheBlock() {
        blockMove(6).execute(new RecordingListener());

        assertEquals(Direction.CLOCKWISE, red1.getDirection());
    }

    @Test
    void rollTooSmallToShareGivesNoBlockMove() {
        assertTrue(blockMoves(1).isEmpty());
    }

    @Test
    void blockCannotJumpOverAnOpponentBlock() {
        placeGreenPieces(8, 2);

        assertTrue(blockMoves(6).isEmpty());
    }

    @Test
    void blockadeCapturesABlockadeOfTheSameSize() {
        placeGreenPieces(7, 2);

        blockMove(6).execute(new RecordingListener());

        assertEquals(4, green.countPiecesInBase());
        assertEquals(1, red1.getCaptureCount());
        assertEquals(1, red2.getCaptureCount());
    }

    @Test
    void blockadeCannotLandOnABiggerBlockade() {
        placeGreenPieces(7, 3);

        assertTrue(blockMoves(6).isEmpty());
    }

    private void placeGreenPieces(int cell, int count) {
        for (int i = 0; i < count; i++) {
            green.getPieces().get(i).moveToStart(cell, Direction.CLOCKWISE);
        }
    }

    private List<PieceMove> blockMoves(int roll) {
        return generator.legalMoves(red, roll).stream().filter(move -> move instanceof BlockMove).toList();
    }

    private BlockMove blockMove(int roll) {
        return (BlockMove) blockMoves(roll).getFirst();
    }
}