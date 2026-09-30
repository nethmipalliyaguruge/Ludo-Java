package com.ludo.strategy;

import com.ludo.model.Colour;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class StrategyFactoryTest  {
    @Test
    void eachColourGetsItsOwnBehaviour() {
        assertInstanceOf(RedAggressiveStrategy.class, StrategyFactory.forColour(Colour.RED));
        assertInstanceOf(GreenBlockingStrategy.class, StrategyFactory.forColour(Colour.GREEN));
        assertInstanceOf(YellowWinningStrategy.class, StrategyFactory.forColour(Colour.YELLOW));
        assertInstanceOf(BlueCyclicStrategy.class, StrategyFactory.forColour(Colour.BLUE));
    }
}
