package com.ludo.model;

import com.ludo.event.GameEventListener;
import com.ludo.random.Picker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MysteryCellMockitoTest {
    @Mock
    private Picker picker;
    @Mock
    private GameEventListener events;

    private Piece redOne;
    private Board board;
    private MysteryCell mysteryCell;

    @BeforeEach
    void setUp() {
        Player red = new Player(Colour.RED);
        redOne = red.getPieces().getFirst();
        redOne.moveToStart(26, Direction.CLOCKWISE);
        board = new Board(List.of(red));
        mysteryCell = new MysteryCell(picker);
    }

    @Test
    void pieceAwayFromTheMysteryCellIsNeverTeleported() {
        when(picker.pickOneOf(anyList())).thenReturn(10);
        spawnTheMysteryCell();

        mysteryCell.teleportIfLandedOn(redOne, events);

        verify(events, never()).onTeleported(any(), any());
        verify(picker, times(1)).pickOneOf(anyList());
    }

    @Test
    void pieceOnTheMysteryCellIsTeleportedAndAnnounced() {
        when(picker.pickOneOf(anyList())).thenReturn(10, TeleportDestination.X);
        spawnTheMysteryCell();
        redOne.teleportTo(10);

        mysteryCell.teleportIfLandedOn(redOne, events);

        verify(events).onTeleported(redOne, TeleportDestination.X);
        assertEquals(26, redOne.getPosition());
    }

    private void spawnTheMysteryCell() {
        mysteryCell.endRound(board, events);
        mysteryCell.endRound(board, events);
    }
}