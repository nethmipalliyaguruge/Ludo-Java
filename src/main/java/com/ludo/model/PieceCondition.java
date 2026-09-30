package com.ludo.model;

public interface PieceCondition {
    int ROUNDS_OF_EFFECT = 4;
    PieceCondition NORMAL = new NormalCondition();

    int stepsFor(int roll);

    PieceCondition afterRound();

    default PieceCondition afterOwnerRoll(int roll) {
        return this;
    }

    default boolean mustReturnToBase() {
        return false;
    }
}