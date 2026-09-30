package com.ludo.model;

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