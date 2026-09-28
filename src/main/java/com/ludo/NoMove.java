package com.ludo;

public class NoMove implements Move {
    private final Player player;

    public NoMove(Player player) {
        this.player = player;
    }

    @Override
    public void execute(GameEventListener events) {
        events.onNoMoveAvailable(player);
    }
}