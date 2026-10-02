package com.ludo.model;

// Null Object: the condition of a piece with no special effect
public class NormalCondition implements PieceCondition {

    @Override
    public int stepsFor(int roll) {
        return roll;
    }

    @Override
    public PieceCondition afterRound() {
        return this;
    }
}