package com.ludo.move;

import com.ludo.event.GameEventListener;
import com.ludo.model.Player;

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