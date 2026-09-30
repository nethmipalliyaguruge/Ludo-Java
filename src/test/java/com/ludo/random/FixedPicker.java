package com.ludo.random;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

public class FixedPicker implements Picker {
    private final Queue<Object> answers;

    public FixedPicker(Object... answers) {
        this.answers = new ArrayDeque<>(Arrays.asList(answers));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T pickOneOf(List<T> options) {
        Object answer = answers.remove();
        if (!options.contains(answer)) {
            throw new IllegalStateException(answer + " is not one of the options " + options);
        }
        return (T) answer;
    }
}