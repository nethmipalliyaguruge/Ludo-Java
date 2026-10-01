package com.ludo.move;

import com.ludo.event.GameEventListener;
import com.ludo.model.Board;
import com.ludo.model.Colour;
import com.ludo.model.Direction;
import com.ludo.model.Piece;
import com.ludo.model.Player;
import com.ludo.random.CoinToss;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MoveMockitoTest {
    @Mock
    private CoinToss coin;
    @Mock
    private GameEventListener events;

    private Player red;
    private Player blue;
    private Piece redOne;
    private Board board;
    private MoveGenerator generator;

    @BeforeEach
    void setUp() {
        red = new Player(Colour.RED);
        blue = new Player(Colour.BLUE);
        redOne = red.getPieces().getFirst();
        board = new Board(List.of(red, blue));
        generator = new MoveGenerator(board, coin);
    }

    @Test
    void enteringAPieceTossesTheCoinExactlyOnce() {
        when(coin.toss()).thenReturn(Direction.COUNTER_CLOCKWISE);

        new EnterBoardMove(red, redOne, board, coin).execute(events);

        verify(coin, times(1)).toss();
        assertEquals(Direction.COUNTER_CLOCKWISE, redOne.getDirection());
    }

    @Test
    void listingTheLegalMovesNeverTossesTheCoin() {
        generator.legalMoves(red, 6);

        verify(coin, never()).toss();
    }

    @Test
    void normalMoveNeverTossesTheCoin() {
        redOne.moveToStart(26, Direction.CLOCKWISE);

        generator.legalMoves(red, 3).getFirst().execute(events);

        verify(coin, never()).toss();
    }

    @Test
    void captureIsAnnouncedRightAfterTheMove() {
        Piece blueOne = blue.getPieces().getFirst();
        redOne.moveToStart(26, Direction.CLOCKWISE);
        blueOne.moveToStart(29, Direction.CLOCKWISE);

        new StepMove(red, redOne, board, 3).execute(events);

        InOrder inOrder = inOrder(events);
        inOrder.verify(events).onPieceMoved(redOne, "26", "29", 3);
        inOrder.verify(events).onCapture(red, redOne, blueOne);
    }
}