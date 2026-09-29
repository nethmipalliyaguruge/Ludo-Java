package com.ludo.move;

import com.ludo.event.GameEventListener;

public interface Move {
    void execute(GameEventListener events);
}
