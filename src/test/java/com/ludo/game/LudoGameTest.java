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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    void thirdSixWithABlockadeBreaksTheBlockade() {
        List<Piece> pieces = red.getPieces();
        pieces.get(0).moveToStart(30, Direction.CLOCKWISE);
        pieces.get(1).moveToStart(30, Direction.CLOCKWISE);
        pieces.get(2).moveToStart(40, Direction.CLOCKWISE);
        pieces.get(3).moveToStart(40, Direction.CLOCKWISE);
        LudoGame game = gameWithDice(new FixedDice(6, 6, 6));

        game.playTurn(red);

        assertTrue(listener.getEvents().contains("blockade broken RED"));
        assertEquals(46, pieces.get(3).getPosition());
    }

    private LudoGame gameWithDice(FixedDice dice) {
        return new LudoGame(players, strategies, dice, new FixedCoinToss(Direction.CLOCKWISE), new MysteryCell(new FixedPicker()), listener);
    }
}
