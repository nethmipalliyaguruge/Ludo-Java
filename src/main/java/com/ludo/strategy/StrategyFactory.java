package com.ludo.strategy;

import com.ludo.model.Colour;
import com.ludo.model.MysteryCell;

public final class StrategyFactory {

    private StrategyFactory() {
    }

    public static PlayerStrategy forColour(Colour colour, MysteryCell mysteryCell) {
        return switch (colour) {
            case RED -> new RedAggressiveStrategy();
            case GREEN -> new GreenBlockingStrategy();
            case YELLOW -> new YellowWinningStrategy();
            case BLUE -> new BlueCyclicStrategy(mysteryCell);
        };
    }
}