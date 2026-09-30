package com.ludo.random;

import java.util.List;

public interface Picker {
    <T> T pickOneOf(List<T> options);
}
