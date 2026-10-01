package com.ludo.model;

import com.ludo.event.RecordingListener;
import com.ludo.random.FixedPicker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TeleportEffectsTest {
    private Piece red1;
    private RecordingListener listener;

    @BeforeEach
    void setUp() {
        red1 = new Player(Colour.RED).getPieces().getFirst();
        red1.moveToStart(26, Direction.CLOCKWISE);
        listener = new RecordingListener();
    }

    @Test
    void alphaCanEnergiseThePiece() {
        TeleportEffects effects = new TeleportEffects(new FixedPicker(true));

        effects.applyOnArrival(red1, TeleportDestination.ALPHA, listener);

        assertEquals(12, red1.stepsFor(6));
        assertEquals(List.of("R1 energised"), listener.getEvents());
    }

    @Test
    void alphaCanMakeThePieceSick() {
        TeleportEffects effects = new TeleportEffects(new FixedPicker(false));

        effects.applyOnArrival(red1, TeleportDestination.ALPHA, listener);

        assertEquals(3, red1.stepsFor(6));
    }

    @Test
    void betaSendsThePieceToABriefing() {
        TeleportEffects effects = new TeleportEffects(new FixedPicker());

        effects.applyOnArrival(red1, TeleportDestination.BETA, listener);

        assertEquals(0, red1.stepsFor(6));
    }

    @Test
    void gammaTurnsAClockwisePieceCounterClockwise() {
        TeleportEffects effects = new TeleportEffects(new FixedPicker());

        effects.applyOnArrival(red1, TeleportDestination.GAMMA, listener);

        assertEquals(Direction.COUNTER_CLOCKWISE, red1.getDirection());
    }

    @Test
    void gammaSendsACounterClockwisePieceToBetaForABriefing() {
        red1.turnCounterClockwise();
        TeleportEffects effects = new TeleportEffects(new FixedPicker());

        effects.applyOnArrival(red1, TeleportDestination.GAMMA, listener);

        assertEquals(25, red1.getPosition());
        assertEquals(0, red1.stepsFor(6));
        assertEquals(List.of("R1 sent from Gamma to Beta", "R1 in briefing"), listener.getEvents());
    }

    @Test
    void otherDestinationsHaveNoEffect() {
        TeleportEffects effects = new TeleportEffects(new FixedPicker());

        effects.applyOnArrival(red1, TeleportDestination.X, listener);

        assertEquals(6, red1.stepsFor(6));
        assertTrue(listener.getEvents().isEmpty());
    }

    @Test
    void pieceTurnedCounterClockwiseMustPassItsApproachAgain() {
        red1.moveToStart(22, Direction.CLOCKWISE);
        red1.move(4);
        TeleportEffects effects = new TeleportEffects(new FixedPicker());

        effects.applyOnArrival(red1, TeleportDestination.GAMMA, listener);

        assertEquals(60, red1.stepsToHome());
    }
}
