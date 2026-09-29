package com.ludo.strategy;

import com.ludo.model.Player;
import com.ludo.move.PieceMove;

import java.util.List;

public interface PlayerStrategy {
    PieceMove chooseMove(List<PieceMove> options, Player self);
}
