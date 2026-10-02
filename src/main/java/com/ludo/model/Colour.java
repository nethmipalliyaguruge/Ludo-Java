package com.ludo.model;

public enum Colour {
    // Cells are numbered 0-51 clockwise from Yellow's X; each approach cell is two cells before its X
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
