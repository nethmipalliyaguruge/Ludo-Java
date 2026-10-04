package com.ludo.random;

import java.util.Random;

// A seeded dice for full-game tests, so they do not share the RandomDice singleton
public class SeededDice implements Dice {
    private final Random random;

    public SeededDice(Random random) {
        this.random = random;
    }

    @Override
    public int roll() {
        return random.nextInt(6) + 1;
    }
}
