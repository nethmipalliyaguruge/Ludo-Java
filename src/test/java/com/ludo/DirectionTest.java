package com.ludo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DirectionTest {
    @Test
    void clockwiseMoveAdvancesByRollValue(){
        assertEquals(13, Direction.CLOCKWISE.move(10, 3));
    }

    @Test
    void clockwiseMovePastLastCellWrapsToStartOfPath(){
        assertEquals(2, Direction.CLOCKWISE.move(50, 4));
    }

    @Test
    void counterClockwiseMoveGoesBackByRollValue(){
        assertEquals(14, Direction.COUNTER_CLOCKWISE.move(20, 6));
    }

    @Test
    void counterClockwiseMovePastCellZeroWrapsToEndOfPath(){
        assertEquals(49, Direction.COUNTER_CLOCKWISE.move(2, 5));
    }
}
