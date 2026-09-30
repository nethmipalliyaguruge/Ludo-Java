package com.ludo.model;

public record SickCondition(int fullRoundsLeft) implements PieceCondition {

    @Override
    public int stepsFor(int roll) {
        return roll / 2;
    }

    @Override
    public PieceCondition afterRound() {
        return fullRoundsLeft > 0 ? new SickCondition(fullRoundsLeft - 1) : NORMAL;
    }
}