package com.ludo.model;

public record BriefingCondition(int fullRoundsLeft, int threesInARow) implements PieceCondition {
    private static final int THREE = 3;
    private static final int THREES_TO_BE_SENT_TO_BASE = 3;

    @Override
    public int stepsFor(int roll) {
        return 0;
    }

    @Override
    public PieceCondition afterRound() {
        // Lasts for the rest of the current round plus four full rounds
        return fullRoundsLeft > 0 ? new BriefingCondition(fullRoundsLeft - 1, threesInARow) : NORMAL;
    }

    @Override
    public PieceCondition afterOwnerRoll(int roll) {
        // T-13: count the owner's 3s in a row; any other roll resets the count
        int threes = (roll == THREE) ? threesInARow + 1 : 0;
        return new BriefingCondition(fullRoundsLeft, threes);
    }

    @Override
    public boolean mustReturnToBase() {
        return threesInARow == THREES_TO_BE_SENT_TO_BASE;
    }
}