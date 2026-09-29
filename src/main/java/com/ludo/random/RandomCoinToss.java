package com.ludo.random;

import com.ludo.model.Direction;

import java.util.Random;

public class RandomCoinToss implements CoinToss{
    private final Random random;

    public RandomCoinToss(Random random) {
        this.random =random;
    }

    @Override
    public Direction toss() {
        boolean heads = random.nextBoolean();
        return heads ? Direction.CLOCKWISE : Direction.COUNTER_CLOCKWISE;
    }
}
