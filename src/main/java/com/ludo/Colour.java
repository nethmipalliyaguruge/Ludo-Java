package com.ludo;

public enum Colour {
    RED("R", 26, 24), GREEN("G", 39, 37), YELLOW("Y", 0, 50), BLUE("B", 13, 11);

    private final String letter;
    private final int startCell;
    private final int approachCell;

    Colour(String letter, int startCell, int approachCell) {
        this.letter = letter;
        this.startCell = startCell;
        this.approachCell = approachCell;
    }

    public String getLetter() {
        return letter;
    }

    public int getStartCell() {
        return startCell;
    }

    public int getApproachCell() {
        return approachCell;
    }

}
