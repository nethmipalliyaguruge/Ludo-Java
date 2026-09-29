package com.ludo.game;

import com.ludo.event.GameEventListener;
import com.ludo.model.Player;
import com.ludo.random.Dice;

import java.util.ArrayList;
import java.util.List;

public class TurnManager {
    private final List<Player> clockwiseOrder;

    public TurnManager(List<Player> clockwiseOrder) {
        this.clockwiseOrder = List.copyOf(clockwiseOrder);
    }

    public List<Player> decideRoundOrder(Dice dice, GameEventListener events) {
        Player firstPlayer = findHighestRoller(clockwiseOrder, dice, events);
        List<Player> roundOrder = orderStartingFrom(firstPlayer);
        events.onRoundOrderDecided(roundOrder);
        return roundOrder;
    }

    private Player findHighestRoller(List<Player> contenders, Dice dice, GameEventListener events) {
        List<Player> highestRollers = new ArrayList<>();
        int highestRoll = 0;
        for (Player player : contenders) {
            int roll = dice.roll();
            events.onFirstPlayerRoll(player, roll);
            if (roll > highestRoll) {
                highestRoll = roll;
                highestRollers.clear();
            }
            if (roll == highestRoll) {
                highestRollers.add(player);
            }
        }
        if (highestRollers.size() == 1) {
            return highestRollers.getFirst();
        }
        return findHighestRoller(highestRollers, dice, events);
    }

    private List<Player> orderStartingFrom(Player firstPlayer) {
        int start = clockwiseOrder.indexOf(firstPlayer);
        List<Player> order = new ArrayList<>();
        for (int offset = 0; offset < clockwiseOrder.size(); offset++) {
            order.add(clockwiseOrder.get((start + offset) % clockwiseOrder.size()));
        }
        return List.copyOf(order);
    }
}