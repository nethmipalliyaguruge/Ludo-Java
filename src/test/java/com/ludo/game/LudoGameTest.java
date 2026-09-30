package com.ludo.game;

import com.ludo.event.RecordingListener;
import com.ludo.model.*;
import com.ludo.random.*;
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
    private Player blue;
    private List<Player> players;
    private Map<Colour, PlayerStrategy> strategies;
    private RecordingListener listener;

    @BeforeEach
    void setUp() {
        red = new Player(Colour.RED);
        blue = new Player(Colour.BLUE);
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
        LudoGame game = new LudoGame(players, strategies, new RandomDice(random), new RandomCoinToss(random), new MysteryCell(new RandomPicker(random)), listener);

        List<Player> finishingOrder = game.play();

        assertEquals(3, finishingOrder.size());
    }

    private LudoGame gameWithDice(FixedDice dice) {
        return new LudoGame(players, strategies, dice, new FixedCoinToss(Direction.CLOCKWISE), new MysteryCell(new FixedPicker()), listener);
    }

    @Test
    void capturingAnOpponentGivesABonusRoll() {
        Piece redOne = red.getPieces().get(0);
        Piece blueOne = players.get(3).getPieces().getFirst();
        redOne.moveToStart(26, Direction.CLOCKWISE);
        blueOne.moveToStart(29, Direction.CLOCKWISE);
        LudoGame game = gameWithDice(new FixedDice(3, 2));
        game.playTurn(red);

        assertEquals(List.of("RED rolled 3", "moved R1 26->29", "R1 captured B1", "RED rolled 2", "moved R1 29->31"), listener.getEvents());
    }

    @Test
    void energisedPieceMovesDoubleTheRoll() {
        Piece red1 = red.getPieces().getFirst();
        red1.moveToStart(26, Direction.CLOCKWISE);
        red1.applyCondition(new EnergisedCondition(4));
        LudoGame game = gameWithDice(new FixedDice(4));

        game.playTurn(red);

        assertEquals(34, red1.getPosition());
    }

    @Test
    void rollingThreeThreesInARowDuringBriefingSendsThePieceToBase() {
        Piece red1 = red.getPieces().getFirst();
        red1.moveToStart(26, Direction.CLOCKWISE);
        red1.applyCondition(new BriefingCondition(4, 0));
        LudoGame game = gameWithDice(new FixedDice(3, 3, 3));

        game.playTurn(red);
        game.playTurn(red);
        game.playTurn(red);

        assertTrue(red1.isInBase());
        assertTrue(listener.getEvents().contains("R1 sent to base from briefing"));
    }
}