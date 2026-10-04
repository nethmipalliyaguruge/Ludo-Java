package com.ludo.random;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomDiceTest {
    @Test
    void rollIsAlwaysBetweenOneAndSix() {
        Dice dice = RandomDice.getInstance(new Random(42L));

        for (int i = 0; i < 1000; i++) {
            int value = dice.roll();
            assertTrue(value >= 1 && value <= 6);
        }
    }

    @Test
    void thereIsOnlyEverOneDice() {
        Dice first = RandomDice.getInstance(new Random(1L));
        Dice second = RandomDice.getInstance(new Random(2L));

        assertSame(first, second);
    }
}
