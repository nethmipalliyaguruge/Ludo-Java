package com.ludo.model;

public enum TeleportDestination {
    ALPHA("Alpha"), BETA("Beta"), GAMMA("Gamma"), BASE("Base"), X("X"), APPROACH("Approach");

    private static final int ALPHA_DISTANCE = 9;
    private static final int BETA_DISTANCE = 27;
    private static final int GAMMA_DISTANCE = 46;

    private final String label;

    TeleportDestination(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void sendHere(Piece piece) {
        switch (this) {
            case ALPHA -> piece.teleportTo(cellFromYellowApproach(ALPHA_DISTANCE));
            case BETA -> piece.teleportTo(cellFromYellowApproach(BETA_DISTANCE));
            case GAMMA -> piece.teleportTo(cellFromYellowApproach(GAMMA_DISTANCE));
            case BASE -> piece.returnToBase();
            case X -> piece.teleportTo(piece.getColour().getStartCell());
            case APPROACH -> piece.teleportTo(piece.getColour().getApproachCell());
        }
    }

    static int cellFromYellowApproach(int distance) {
        return (Colour.YELLOW.getApproachCell() + distance) % Board.STANDARD_PATH_LENGTH;
    }
}