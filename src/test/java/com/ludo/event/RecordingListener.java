package com.ludo.event;

import com.ludo.model.Piece;
import com.ludo.model.Player;

import java.util.ArrayList;
import java.util.List;

public class RecordingListener implements GameEventListener {
    private final List<String> events = new ArrayList<>();

    public List<String> getEvents() {
        return events;
    }

    @Override
    public void onPieceMoved(Piece piece, String from, String to, int steps) {
        events.add("moved " + piece.getName() + " " + from + "->" + to);
    }

    @Override
    public void onCapture(Player attacker, Piece attackingPiece, Piece capturedPiece) {
        events.add(attackingPiece.getName() + " captured " + capturedPiece.getName());
    }

    @Override
    public void onNoMoveAvailable(Player player) {
        events.add("no move " + player.getColour());
    }
}