package com.ludo;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

class FixedDice implements Dice {
    private final Queue<Integer> rolls;

    FixedDice(Integer...rolls){
        this.rolls = new ArrayDeque<>(List.of(rolls));
    }

    @Override
    public int roll(){
        return rolls.remove();
    }
}
