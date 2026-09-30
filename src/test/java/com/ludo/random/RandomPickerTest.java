package com.ludo.random;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomPickerTest {

    @Test
    void alwaysPicksOneOfTheGivenOptions() {
        Picker picker = new RandomPicker(new Random(42L));
        List<Integer> options = List.of(3, 17, 40);

        for (int i = 0; i < 100; i++) {
            assertTrue(options.contains(picker.pickOneOf(options)));
        }
    }
}