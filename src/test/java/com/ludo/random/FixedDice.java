package com.ludo.random;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

public class FixedDice implements Dice {
    private final Queue<Integer> rolls;

    public FixedDice(Integer...rolls){
        this.rolls = new ArrayDeque<>(List.of(rolls));
    }

    @Override
    public int roll(){
        return rolls.remove();
    }
}
