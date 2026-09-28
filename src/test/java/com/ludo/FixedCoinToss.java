package com.ludo;

class FixedCoinToss implements CoinToss {
    private final Direction result;

    FixedCoinToss(Direction result) {
        this.result = result;
    }

    @Override
    public Direction toss(){
        return result;
    }
}
