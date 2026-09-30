package com.ludo.game;

import com.ludo.event.RecordingListener;
import com.ludo.model.Colour;
import com.ludo.model.Player;
import com.ludo.random.FixedDice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TurnManagerTest {
    private Player red;
    private Player green;
    private Player yellow;
    private Player blue;
    private TurnManager turnManager;

    @BeforeEach
    void setUp() {
        red = new Player(Colour.RED);
        green = new Player(Colour.GREEN);
        yellow = new Player(Colour.YELLOW);
        blue = new Player(Colour.BLUE);
        turnManager = new TurnManager(List.of(red, green, yellow, blue));
    }

    @Test
    void highestRollerStartsAndOrderContinuesClockwise() {
        FixedDice dice = new FixedDice(2, 5, 3, 1);

        List<Player> order = turnManager.decideRoundOrder(dice, new RecordingListener());

        assertEquals(List.of(green, yellow, blue, red), order);
    }

    @Test
    void playersTiedForHighestRollRollAgain() {
        FixedDice dice = new FixedDice(6, 6, 1, 1, 2, 5);

        List<Player> order = turnManager.decideRoundOrder(dice, new RecordingListener());

        assertEquals(green, order.getFirst());
    }
}