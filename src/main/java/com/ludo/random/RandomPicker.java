package com.ludo.random;

import java.util.List;
import java.util.Random;

public class RandomPicker implements Picker {
    private final Random random;

    public RandomPicker(Random random) {
        this.random = random;
    }

    @Override
    public <T> T pickOneOf(List<T> options) {
        return options.get(random.nextInt(options.size()));
    }
}
