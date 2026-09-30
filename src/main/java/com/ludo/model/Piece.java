package com.ludo.model;

import com.ludo.exception.IllegalMoveException;

public class Piece {
    private final Colour colour;
    private final int number;
    private PieceState state;
    private int position;
    private Direction direction;
    private int homePathIndex;
    private int captureCount;
    private static final int STEPS_FROM_BASE_TO_HOME = Board.STANDARD_PATH_LENGTH + Board.HOME_PATH_LENGTH;
    private int approachPasses;

    public Piece(Colour colour, int number) {
        this.colour = colour;
        this.number = number;
        this.state = PieceState.BASE;
        this.direction = Direction.CLOCKWISE;
    }

    public String getName() {
        return colour.getLetter() + number;
    }

    public Colour getColour() {
        return colour;
    }

    public int getPosition() {
        return position;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getCaptureCount() {
        return captureCount;
    }

    public void moveToStart(int startCell, Direction direction) {
        this.state = PieceState.STANDARD_PATH;
        this.position = startCell;
        this.direction = direction;
    }

    public void move(int steps) {
        if (!canMoveBy(steps)) {
            throw new IllegalMoveException(
                    getName() + " cannot move " + steps + " steps from " + describeLocation());
        }
        if (isInHomePath()) {
            advanceInHomePath(steps);
        } else if (staysOnStandardPathAfter(steps)) {
            countApproachPass(steps);
            position = cellAfter(steps);
        } else {
            enterHomePath(steps - stepsToApproach());
        }
    }

    public void returnToBase() {
        homePathIndex = 0;
        captureCount = 0;
        approachPasses = 0;
        state = PieceState.BASE;
    }

    public boolean isInBase() {
        return state == PieceState.BASE;
    }

    public boolean isOnStandardPath() {
        return state == PieceState.STANDARD_PATH;
    }

    public boolean isHome() {
        return state == PieceState.HOME;
    }

    public boolean isInHomePath() {
        return state == PieceState.HOME_PATH;
    }

    public void recordCapture() {
        captureCount++;
    }

    public int cellAfter(int steps) {
        return direction.move(position, steps);
    }

    private int stepsToApproach() {
        return direction.stepsBetween(getPosition(), colour.getApproachCell());
    }

    public boolean canEnterHomePath() {
        boolean hasCaptured = captureCount > 0;
        boolean readyForHomePath = direction == Direction.CLOCKWISE || approachPasses > 0;
        return hasCaptured && readyForHomePath;
    }

    public int stepsToHome() {
        return switch (state) {
            case BASE -> STEPS_FROM_BASE_TO_HOME;
            case STANDARD_PATH -> stepsToApproach() + Board.HOME_PATH_LENGTH + 1;
            case HOME_PATH -> Board.HOME_PATH_LENGTH - homePathIndex;
            case HOME -> 0;
        };
    }

    public boolean staysOnStandardPathAfter(int steps) {
        return !canEnterHomePath() || steps <= stepsToApproach();
    }

    public boolean canMoveBy(int steps) {
        if (isInHomePath()) {
            return homePathIndex + steps <= Board.HOME_PATH_LENGTH;
        }
        return isOnStandardPath();
    }

    public String describeLocation() {
        return switch (state) {
            case BASE -> "Base";
            case STANDARD_PATH -> String.valueOf(position);
            case HOME_PATH -> colour.name().toLowerCase() + "homepath" + homePathIndex;
            case HOME -> "Home";
        };
    }

    private void enterHomePath(int stepsPastApproach) {
        state = PieceState.HOME_PATH;
        homePathIndex = 0;
        advanceInHomePath(stepsPastApproach - 1);
    }

    private void advanceInHomePath(int steps) {
        homePathIndex += steps;
        if (homePathIndex == Board.HOME_PATH_LENGTH) {
            state = PieceState.HOME;
        }
    }

    private void countApproachPass(int steps) {
        int distance = stepsToApproach();
        if (distance > 0 && steps >= distance) {
            approachPasses++;
        }
    }
}
