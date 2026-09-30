package com.ludo.strategy;

import com.ludo.model.Colour;
import com.ludo.model.MysteryCell;
import com.ludo.random.FixedPicker;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StrategyFactoryTest {
    private final MysteryCell mysteryCell = new MysteryCell(new FixedPicker());

    @Test
    void eachColourGetsItsOwnBehaviour() {
        assertInstanceOf(RedAggressiveStrategy.class, StrategyFactory.forColour(Colour.RED, mysteryCell));
        assertInstanceOf(GreenBlockingStrategy.class, StrategyFactory.forColour(Colour.GREEN, mysteryCell));
        assertInstanceOf(YellowWinningStrategy.class, StrategyFactory.forColour(Colour.YELLOW, mysteryCell));
        assertInstanceOf(BlueCyclicStrategy.class, StrategyFactory.forColour(Colour.BLUE, mysteryCell));
    }
}