package com.ludo;

public class Piece {
    private final Colour colour;
    private final int number;
    private PieceState state;
    private int position;
    private Direction direction;

    public Piece(Colour colour, int number) {
        this.colour = colour;
        this.number = number;
        this.state = PieceState.BASE;
    }

    public String getName() {
        return colour.getLetter() + number;
    }

    public boolean isInBase() {
        return state == PieceState.BASE;
    }

    public int getPosition() {
        return position;
    }

    public void moveToStart(int startCell, Direction direction) {
        this.state = PieceState.STANDARD_PATH;
        this.position = startCell;
        this.direction = direction;
    }

    public void move(int steps) {
        position = direction.move(position, steps);
    }

    public void returnToBase() {
        state = PieceState.BASE;
        direction = null;
    }

    public Colour getColour() {
        return colour;
    }

    public boolean isOnStandardPath() {
        return state == PieceState.STANDARD_PATH;
    }

    public boolean isHome() {
        return state == PieceState.HOME;
    }
}
