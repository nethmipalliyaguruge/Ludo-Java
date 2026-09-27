package com.ludo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RandomDiceTest {
    @Test
    void rollIsAlwaysBetweenOneAndSix() {
        Dice dice = new RandomDice(42L);

        for (int i = 0; i < 1000; i++) {
            int value = dice.roll();
            assertTrue(value >= 1 && value <= 6);
        }
    }

    @Test
    void diceWithSameSeedProduceSameSequenceOfRolls() {
        Dice diceA = new RandomDice(42L);
        Dice diceB = new RandomDice(42L);

        for (int i = 0; i < 10; i++) {
            assertEquals(diceA.roll(), diceB.roll());
        }
    }
}
