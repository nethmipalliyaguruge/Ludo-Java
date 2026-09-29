package com.ludo.random;

import com.ludo.model.Direction;

public class FixedCoinToss implements CoinToss {
    private final Direction result;

    public FixedCoinToss(Direction result) {
        this.result = result;
    }

    @Override
    public Direction toss(){
        return result;
    }
}
