package com.ludo.event;

import com.ludo.model.Direction;
import com.ludo.model.Piece;
import com.ludo.model.Player;
import com.ludo.model.TeleportDestination;

import java.util.ArrayList;
import java.util.List;

public class RecordingListener implements GameEventListener {
    private final List<String> events = new ArrayList<>();

    public List<String> getEvents() {
        return events;
    }

    @Override
    public void onRoll(Player player, int roll) {
        events.add(player.getColour() + " rolled " + roll);
    }

    @Override
    public void onPieceEnteredBoard(Player owner, Piece piece) {
        events.add("entered " + piece.getName());
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

    @Override
    public void onThirdSixIgnored(Player player) {
        events.add("third six " + player.getColour());
    }

    @Override
    public void onMysteryCellSpawned(int cell, int rounds) {
        events.add("mystery spawned at " + cell);
    }

    @Override
    public void onMysteryCellCountdown(int cell, int roundsLeft) {
        events.add("mystery at " + cell + " for " + roundsLeft);
    }

    @Override
    public void onTeleported(Piece piece, TeleportDestination destination) {
        events.add(piece.getName() + " teleported to " + destination.getLabel());
    }

    @Override
    public void onEnergised(Piece piece) {
        events.add(piece.getName() + " energised");
    }

    @Override
    public void onSick(Piece piece) {
        events.add(piece.getName() + " sick");
    }

    @Override
    public void onBriefing(Piece piece) {
        events.add(piece.getName() + " in briefing");
    }

    @Override
    public void onSentToBaseFromBriefing(Piece piece) {
        events.add(piece.getName() + " sent to base from briefing");
    }

    @Override
    public void onTurnedCounterClockwise(Piece piece) {
        events.add(piece.getName() + " turned counter-clockwise");
    }

    @Override
    public void onSentFromGammaToBeta(Piece piece) {
        events.add(piece.getName() + " sent from Gamma to Beta");
    }

    @Override
    public void onBlockMoved(Player owner, List<Piece> block, String from, String to, int steps, Direction direction) {
        events.add("block moved " + from + "->" + to);
    }

    @Override
    public void onBlockadeBroken(Player player) {
        events.add("blockade broken " + player.getColour());
    }

    @Override
    public void onPieceBlocked(Piece piece, String from, String to, Piece blocker) {
        events.add(piece.getName() + " blocked " + from + "->" + to + " by " + blocker.getName());
    }

    @Override
    public void onMovedUpToBlock(Piece piece) {
        events.add(piece.getName() + " moved up to block");
    }

    @Override
    public void onBlockedThrowIgnored(Piece piece) {
        events.add(piece.getName() + " blocked throw ignored");
    }
}