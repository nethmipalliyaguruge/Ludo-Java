package com.ludo.random;

import com.ludo.model.Direction;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RandomCoinTossTest {

    @Test
    void seededCoinLandsOnBothSidesOverManyTosses() {
        CoinToss coin = new RandomCoinToss(new Random(42L));
        Set<Direction> seen = EnumSet.noneOf(Direction.class);

        for (int i = 0; i < 100; i++) {
            seen.add(coin.toss());
        }

        assertEquals(EnumSet.allOf(Direction.class), seen);
    }

    @Test
    void coinsWithTheSameSeedGiveTheSameTosses() {
        CoinToss coinA = new RandomCoinToss(new Random(7L));
        CoinToss coinB = new RandomCoinToss(new Random(7L));

        for (int i = 0; i < 10; i++) {
            assertEquals(coinA.toss(), coinB.toss());
        }
    }
}