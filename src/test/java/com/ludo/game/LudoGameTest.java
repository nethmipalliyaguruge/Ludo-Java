package com.ludo.game;

import com.ludo.event.RecordingListener;
import com.ludo.model.Colour;
import com.ludo.model.Direction;
import com.ludo.model.Player;
import com.ludo.random.FixedCoinToss;
import com.ludo.random.FixedDice;
import com.ludo.random.RandomCoinToss;
import com.ludo.random.RandomDice;
import com.ludo.strategy.FirstAvailableMoveStrategy;
import com.ludo.strategy.PlayerStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class LudoGameTest {
    private Player red;
    private List<Player> players;
    private Map<Colour, PlayerStrategy> strategies;
    private RecordingListener listener;

    @BeforeEach
    void setUp() {
        red = new Player(Colour.RED);
        players = List.of(red, new Player(Colour.GREEN), new Player(Colour.YELLOW), new Player(Colour.BLUE));
        strategies = new EnumMap<>(Colour.class);
        for (Colour colour : Colour.values()) {
            strategies.put(colour, new FirstAvailableMoveStrategy());
        }
        listener = new RecordingListener();
    }

    @Test
    void rollingSixGivesThePlayerAnotherRoll() {
        LudoGame game = gameWithDice(new FixedDice(6, 3));

        game.playTurn(red);

        assertEquals(List.of("RED rolled 6", "entered R1", "RED rolled 3",
                "moved R1 26->29"), listener.getEvents());
    }

    @Test
    void thirdSixInARowIsIgnoredAndEndsTheTurn() {
        LudoGame game = gameWithDice(new FixedDice(6, 6, 6));

        game.playTurn(red);

        assertEquals("third six RED", listener.getEvents().getLast());
    }

    @Test
    void noLegalMovePassesTheTurnWithoutMovingAnything() {
        LudoGame game = gameWithDice(new FixedDice(4));

        game.playTurn(red);

        assertEquals(List.of("RED rolled 4", "no move RED"), listener.getEvents());
        assertEquals(4, red.countPiecesInBase());
    }

    @Test
    void completeGameEndsWithThreePlayersFinished() {
        Random random = new Random(42L);
        LudoGame game = new LudoGame(players, strategies, new RandomDice(random),
                new RandomCoinToss(random), listener);

        List<Player> finishingOrder = game.play();

        assertEquals(3, finishingOrder.size());
    }

    private LudoGame gameWithDice(FixedDice dice) {
        return new LudoGame(players, strategies, dice, new FixedCoinToss(Direction.CLOCKWISE), listener);
    }
}