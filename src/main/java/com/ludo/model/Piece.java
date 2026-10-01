package com.ludo.model;

import com.ludo.exception.IllegalMoveException;

public class Piece {
    private static final int STEPS_FROM_BASE_TO_HOME = Board.STANDARD_PATH_LENGTH + Board.HOME_PATH_LENGTH;

    private final Colour colour;
    private final int number;
    private PieceState state;
    private int position;
    private Direction direction;
    private PieceCondition condition = PieceCondition.NORMAL;
    private int homePathIndex;
    private int captureCount;
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

    public boolean isInBase() {
        return state == PieceState.BASE;
    }

    public boolean isOnStandardPath() {
        return state == PieceState.STANDARD_PATH;
    }

    public boolean isInHomePath() {
        return state == PieceState.HOME_PATH;
    }

    public boolean isHome() {
        return state == PieceState.HOME;
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
        state = PieceState.BASE;
        direction = Direction.CLOCKWISE;
        condition = PieceCondition.NORMAL;
        homePathIndex = 0;
        captureCount = 0;
        approachPasses = 0;
    }

    public void teleportTo(int cell) {
        placeOnStandardPath(cell, "be teleported");
    }

    public void moveWithBlockTo(int cell) {
        placeOnStandardPath(cell, "move with a block");
    }

    public void turnCounterClockwise() {
        direction = Direction.COUNTER_CLOCKWISE;
        approachPasses = 0;
    }

    public void recordCapture() {
        captureCount++;
    }

    public int stepsFor(int roll) {
        return condition.stepsFor(roll);
    }

    public void applyCondition(PieceCondition newCondition) {
        condition = newCondition;
    }

    public void endRound() {
        condition = condition.afterRound();
    }

    public void noteOwnerRoll(int roll) {
        condition = condition.afterOwnerRoll(roll);
    }

    public boolean mustReturnToBase() {
        return condition.mustReturnToBase();
    }

    public int cellAfter(int steps) {
        return direction.move(position, steps);
    }

    public boolean canMoveBy(int steps) {
        if (isInHomePath()) {
            return homePathIndex + steps <= Board.HOME_PATH_LENGTH;
        }
        return isOnStandardPath();
    }

    public boolean canEnterHomePath() {
        boolean hasCaptured = captureCount > 0;
        boolean readyForHomePath = direction == Direction.CLOCKWISE || approachPasses > 0;
        return hasCaptured && readyForHomePath;
    }

    public boolean staysOnStandardPathAfter(int steps) {
        return !canEnterHomePath() || steps <= stepsToApproach();
    }

    public int stepsToHome() {
        return switch (state) {
            case BASE -> STEPS_FROM_BASE_TO_HOME;
            case STANDARD_PATH -> stepsToHomeFromStandardPath();
            case HOME_PATH -> Board.HOME_PATH_LENGTH - homePathIndex;
            case HOME -> 0;
        };
    }

    public String describeLocation() {
        return switch (state) {
            case BASE -> "Base";
            case STANDARD_PATH -> String.valueOf(position);
            case HOME_PATH -> colour.name().toLowerCase() + "homepath" + homePathIndex;
            case HOME -> "Home";
        };
    }

    private int stepsToApproach() {
        return direction.stepsBetween(position, colour.getApproachCell());
    }

    private int stepsToHomeFromStandardPath() {
        int steps = stepsToApproach() + Board.HOME_PATH_LENGTH + 1;
        boolean mustGoRoundOnceMore = direction == Direction.COUNTER_CLOCKWISE && approachPasses == 0;
        if (mustGoRoundOnceMore) {
            steps = steps + Board.STANDARD_PATH_LENGTH;
        }
        return steps;
    }

    private void countApproachPass(int steps) {
        boolean movesBeyondApproach = steps > stepsToApproach();
        if (movesBeyondApproach) {
            approachPasses++;
        }
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

    private void placeOnStandardPath(int cell, String action) {
        if (!isOnStandardPath()) {
            throw new IllegalMoveException(getName() + " can only " + action + " from the standard path");
        }
        position = cell;
    }
}
