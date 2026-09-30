package com.ludo.strategy;

import com.ludo.move.PieceMove;

import java.util.List;

interface MoveRule {
    PieceMove choose(List<PieceMove> options);
}