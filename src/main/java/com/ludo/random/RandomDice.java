package com.ludo.random;

import java.util.Random;

public class RandomDice implements Dice {
    private static final int FACES = 6;
    private final Random random;

    public RandomDice(long seed) {
        this(new Random(seed));
    }

    public RandomDice(Random random) {
        this.random = random;
    }

    @Override
    public int roll() {
        return random.nextInt(FACES) + 1;
    }
}