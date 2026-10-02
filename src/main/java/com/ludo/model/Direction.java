package com.ludo.model;

public enum Direction {
    CLOCKWISE(1),
    COUNTER_CLOCKWISE(-1);

    private final int sign;

    Direction(int sign) {
        this.sign = sign;
    }

    public int move(int fromCell, int steps) {
        // floorMod keeps the result in 0-51 when moving backwards past cell 0 (plain % could give a negative number)
        return Math.floorMod(fromCell + sign * steps, Board.STANDARD_PATH_LENGTH);
    }

    public int stepsBetween(int fromCell, int toCell) {
        return Math.floorMod(sign * (toCell - fromCell), Board.STANDARD_PATH_LENGTH);
    }
}
