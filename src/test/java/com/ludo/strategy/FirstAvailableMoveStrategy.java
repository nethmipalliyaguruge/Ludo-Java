package com.ludo.strategy;

import com.ludo.model.Player;
import com.ludo.move.PieceMove;

import java.util.List;

public class FirstAvailableMoveStrategy implements PlayerStrategy {

    @Override
    public PieceMove chooseMove(List<PieceMove> options, Player self) {
        return options.getFirst();
    }
}