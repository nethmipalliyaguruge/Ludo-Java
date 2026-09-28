package com.ludo;

public enum Direction {
    CLOCKWISE(1),
    COUNTER_CLOCKWISE(-1);

    private final int sign;

    Direction(int sign) {
        this.sign = sign;
    }

    public int move(int fromCell, int steps) {
        return Math.floorMod(fromCell + sign * steps, Board.STANDARD_PATH_LENGTH);
    }

    public int stepsBetween(int fromCell, int toCell) {
        return Math.floorMod(sign * (toCell - fromCell), Board.STANDARD_PATH_LENGTH);
    }
}
