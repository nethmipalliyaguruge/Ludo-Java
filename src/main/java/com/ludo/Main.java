package com.ludo;

import com.ludo.game.LudoGame;
import com.ludo.model.Colour;
import com.ludo.model.Player;
import com.ludo.output.ConsoleReporter;
import com.ludo.random.RandomCoinToss;
import com.ludo.random.RandomDice;
import com.ludo.strategy.FirstAvailableMoveStrategy;
import com.ludo.strategy.PlayerStrategy;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Main {
    private static final long DEFAULT_SEED = 42L;

    public static void main(String[] args) {
        long seed = args.length > 0 ? Long.parseLong(args[0]) : DEFAULT_SEED;
        Random random = new Random(seed);

        List<Player> players = new ArrayList<>();
        Map<Colour, PlayerStrategy> strategies = new EnumMap<>(Colour.class);
        for (Colour colour : Colour.values()) {
            players.add(new Player(colour));
            strategies.put(colour, new FirstAvailableMoveStrategy());
        }

        LudoGame game = new LudoGame(players, strategies,
                new RandomDice(random), new RandomCoinToss(random), new ConsoleReporter(System.out));
        game.play();
    }
}