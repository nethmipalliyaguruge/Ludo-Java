package com.ludo.game;

import com.ludo.event.GameEventListener;
import com.ludo.model.Colour;
import com.ludo.model.Direction;
import com.ludo.model.MysteryCell;
import com.ludo.model.Piece;
import com.ludo.model.Player;
import com.ludo.move.PieceMove;
import com.ludo.random.Dice;
import com.ludo.random.FixedCoinToss;
import com.ludo.random.FixedPicker;
import com.ludo.random.RandomCoinToss;
import com.ludo.random.RandomDice;
import com.ludo.random.RandomPicker;
import com.ludo.strategy.PlayerStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LudoGameMockitoTest {
    @Mock
    private Dice dice;
    @Mock
    private GameEventListener events;
    @Mock
    private PlayerStrategy strategy;
    @Captor
    private ArgumentCaptor<List<PieceMove>> optionsCaptor;
    @Captor
    private ArgumentCaptor<List<Player>> rankingCaptor;

    private Player red;
    private List<Player> players;
    private Map<Colour, PlayerStrategy> strategies;
    private LudoGame game;

    @BeforeEach
    void setUp() {
        red = new Player(Colour.RED);
        players = List.of(red, new Player(Colour.GREEN), new Player(Colour.YELLOW),
                new Player(Colour.BLUE));
        strategies = new EnumMap<>(Colour.class);
        for (Colour colour : Colour.values()) {
            strategies.put(colour, strategy);
        }
        game = new LudoGame(players, strategies, dice, new FixedCoinToss(Direction.CLOCKWISE),
                new MysteryCell(new FixedPicker()), events);
    }

    @Test
    void noLegalMoveIsReportedWithoutAskingTheStrategy() {
        when(dice.roll()).thenReturn(4);

        game.playTurn(red);

        verify(events).onNoMoveAvailable(red);
        verify(strategy, never()).chooseMove(anyList(), any());
    }

    @Test
    void rollingSixEntersAPieceAndGivesAnotherRoll() {
        strategyPicksTheFirstOption();
        when(dice.roll()).thenReturn(6, 3);
        Piece redOne = red.getPieces().getFirst();

        game.playTurn(red);

        InOrder inOrder = inOrder(events);
        inOrder.verify(events).onRoll(red, 6);
        inOrder.verify(events).onPieceEnteredBoard(red, redOne);
        inOrder.verify(events).onRoll(red, 3);
        verify(dice, times(2)).roll();
    }

    @Test
    void thirdSixInARowIsIgnoredAndEndsTheTurn() {
        strategyPicksTheFirstOption();
        when(dice.roll()).thenReturn(6, 6, 6);

        game.playTurn(red);

        verify(events).onThirdSixIgnored(red);
        verify(strategy, times(2)).chooseMove(anyList(), eq(red));
        verify(dice, times(3)).roll();
    }

    @Test
    void strategyIsOfferedOnlyTheLegalMoves() {
        strategyPicksTheFirstOption();
        when(dice.roll()).thenReturn(5);
        Piece redOne = red.getPieces().getFirst();
        redOne.moveToStart(26, Direction.CLOCKWISE);

        game.playTurn(red);

        verify(strategy).chooseMove(optionsCaptor.capture(), eq(red));
        List<PieceMove> offered = optionsCaptor.getValue();
        assertEquals(1, offered.size());
        assertSame(redOne, offered.getFirst().getPiece());
    }

    @Test
    void finishedGameReportsAFullRankingOfAllPlayers() {
        strategyPicksTheFirstOption();
        Random random = new Random(42L);
        LudoGame wholeGame = new LudoGame(players, strategies, new RandomDice(random),
                new RandomCoinToss(random), new MysteryCell(new RandomPicker(random)), events);

        wholeGame.play();

        verify(events).onGameStart(players);
        verify(events).onGameOver(rankingCaptor.capture());
        List<Player> ranking = rankingCaptor.getValue();
        assertEquals(4, ranking.size());
        assertTrue(ranking.containsAll(players));
    }

    private void strategyPicksTheFirstOption() {
        when(strategy.chooseMove(anyList(), any()))
                .thenAnswer(invocation -> invocation.<List<PieceMove>>getArgument(0).getFirst());
    }
}