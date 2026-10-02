package com.ludo.move;

import com.ludo.event.GameEventListener;
import com.ludo.model.Player;

// Null Object: used when no move is possible, so the game never has to check for null
public class NoMove implements Move {
    private final Player player;

    public NoMove(Player player) {
        this.player = player;
    }

    @Override
    public void execute(GameEventListener events) {
        events.onNoMoveAvailable(player);
    }

    @Override
    public boolean ignoresThrow() {
        return true;
    }
}