package com.ludo.model;

public record SickCondition(int fullRoundsLeft) implements PieceCondition {

    @Override
    public int stepsFor(int roll) {
        // Integer division rounds down, so a roll of 1 means the piece cannot move
        return roll / 2;
    }

    @Override
    public PieceCondition afterRound() {
        return fullRoundsLeft > 0 ? new SickCondition(fullRoundsLeft - 1) : NORMAL;
    }
}