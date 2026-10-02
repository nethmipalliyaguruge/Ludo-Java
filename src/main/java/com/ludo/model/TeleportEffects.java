package com.ludo.model;

import com.ludo.event.GameEventListener;
import com.ludo.random.Picker;

import java.util.List;

public class TeleportEffects {
    private final Picker picker;

    public TeleportEffects(Picker picker) {
        this.picker = picker;
    }

    public void applyOnArrival(Piece piece, TeleportDestination destination, GameEventListener events) {
        switch (destination) {
            case ALPHA -> energiseOrSicken(piece, events);
            case BETA -> sendToBriefing(piece, events);
            case GAMMA -> clarifyDirection(piece, events);
            default -> {
                // Base, X and Approach have no special effect.
            }
        }
    }

    private void energiseOrSicken(Piece piece, GameEventListener events) {
        // T-12: energised or sick, each with equal chance
        boolean energised = picker.pickOneOf(List.of(true, false));
        if (energised) {
            piece.applyCondition(new EnergisedCondition(PieceCondition.ROUNDS_OF_EFFECT));
            events.onEnergised(piece);
        } else {
            piece.applyCondition(new SickCondition(PieceCondition.ROUNDS_OF_EFFECT));
            events.onSick(piece);
        }
    }

    private void sendToBriefing(Piece piece, GameEventListener events) {
        piece.applyCondition(new BriefingCondition(PieceCondition.ROUNDS_OF_EFFECT, 0));
        events.onBriefing(piece);
    }

    private void clarifyDirection(Piece piece, GameEventListener events) {
        if (piece.getDirection() == Direction.CLOCKWISE) {
            piece.turnCounterClockwise();
            events.onTurnedCounterClockwise(piece);
        } else {
            // T-14: a counter-clockwise piece is sent on to Beta and gets its briefing
            TeleportDestination.BETA.sendHere(piece);
            events.onSentFromGammaToBeta(piece);
            sendToBriefing(piece, events);
        }
    }
}