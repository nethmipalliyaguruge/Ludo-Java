package com.ludo.random;

import java.util.Random;

// Singleton: the game has exactly one real dice. It is still passed to LudoGame
// through the Dice interface, so tests can replace it with a FixedDice or a mock.
public final class RandomDice implements Dice {
    private static final int FACES = 6;
    private static RandomDice instance;

    private final Random random;

    private RandomDice(Random random) {
        this.random = random;
    }

    // The first call creates the dice; later calls return the same dice
    public static RandomDice getInstance(Random random) {
        if (instance == null) {
            instance = new RandomDice(random);
        }
        return instance;
    }

    @Override
    public int roll() {
        return random.nextInt(FACES) + 1;
    }
}
