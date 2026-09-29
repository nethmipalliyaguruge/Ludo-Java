package com.ludo.strategy;

import com.ludo.model.Colour;

public final class StrategyFactory {

    private StrategyFactory() {
    }

    public static PlayerStrategy forColour(Colour colour) {
        return switch (colour) {
            case RED -> new RedAggressiveStrategy();
            case GREEN -> new GreenBlockingStrategy();
            case YELLOW -> new YellowWinningStrategy();
            case BLUE -> new BlueCyclicStrategy();
        };
    }
}