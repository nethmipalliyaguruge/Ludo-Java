package com.ludo.game;

import com.ludo.model.Colour;
import com.ludo.model.Direction;
import com.ludo.model.Piece;
import com.ludo.model.Player;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerStatusTest {
    @Test
    void statusIsASnapshotOfThePlayersPieces(){
        Player red = new Player(Colour.RED);
        red.getPieces().getFirst().moveToStart(26,Direction.CLOCKWISE);
        PlayerStatus status = PlayerStatus.of(red);
        assertEquals(1,status.piecesOnBoard());
        assertEquals(3,status.piecesInBase());
        assertEquals(new PieceStatus("R1","26"), status.pieces().getFirst());

    }
    @Test
    void laterMovesDoNotChangeAnEarlierSnapshot(){
        Player red = new Player(Colour.RED);
        PlayerStatus status = PlayerStatus.of(red);
        red.getPieces().getFirst().moveToStart(26, Direction.CLOCKWISE);
        assertEquals(4,status.piecesInBase());

    }
}
