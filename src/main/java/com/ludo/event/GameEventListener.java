package com.ludo.event;

import com.ludo.game.PlayerStatus;
import com.ludo.model.Piece;
import com.ludo.model.Player;

import java.util.List;

public interface GameEventListener {
    default void onPieceEnteredBoard(Player owner, Piece piece) {
    }

    default void onPieceMoved(Piece piece, String from, String to, int steps) {
    }

    default void onCapture(Player attacker, Piece attackingPiece, Piece capturedPiece) {
    }

    default void onNoMoveAvailable(Player player) {
    }

    default void onGameStart(List<Player> players) {

    }

    default void onFirstPlayerRoll(Player player, int roll) {

    }

    default void onRoundOrderDecided(List<Player> order) {

    }

    default void onRoll(Player player, int roll) {

    }

    default void onThirdSixIgnored(Player player) {

    }

    default void onPlayerFinished(Player player, int place) {

    }

    default void onRoundEnd(int round, List<PlayerStatus> statuses) {
    }
}
