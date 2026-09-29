package com.ludo.game;

import com.ludo.event.GameEventListener;
import com.ludo.model.Board;
import com.ludo.model.Colour;
import com.ludo.model.Player;
import com.ludo.move.Move;
import com.ludo.move.MoveGenerator;
import com.ludo.move.NoMove;
import com.ludo.move.PieceMove;
import com.ludo.random.CoinToss;
import com.ludo.random.Dice;
import com.ludo.strategy.PlayerStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LudoGame {
    private static final int SIX = 6;
    private static final int SIXES_BEFORE_ROLL_IS_IGNORED = 3;
    private static final int MAX_ROUNDS = 1000;

    private final List<Player> players;
    private final Map<Colour, PlayerStrategy> strategies;
    private final Dice dice;
    private final GameEventListener events;
    private final MoveGenerator moveGenerator;
    private final TurnManager turnManager;
    private final List<Player> finishingOrder = new ArrayList<>();

    public LudoGame(List<Player> players, Map<Colour, PlayerStrategy> strategies,
                    Dice dice, CoinToss coin, GameEventListener events) {
        this.players = List.copyOf(players);
        this.strategies = Map.copyOf(strategies);
        this.dice = dice;
        this.events = events;
        this.moveGenerator = new MoveGenerator(new Board(this.players), coin);
        this.turnManager = new TurnManager(this.players);
    }

    public List<Player> play() {
        events.onGameStart(players);
        List<Player> roundOrder = turnManager.decideRoundOrder(dice, events);
        int round = 0;
        while (!isOver() && round < MAX_ROUNDS) {
            round++;
            playRound(roundOrder);
            events.onRoundEnd(round, currentStatuses());
        }
        return List.copyOf(finishingOrder);
    }

    void playTurn(Player player) {
        int sixesInARow = 0;
        boolean rollAgain = true;
        while (rollAgain) {
            int roll = dice.roll();
            events.onRoll(player, roll);
            sixesInARow = (roll == SIX) ? sixesInARow + 1 : 0;
            if (sixesInARow == SIXES_BEFORE_ROLL_IS_IGNORED) {
                events.onThirdSixIgnored(player);
                return;
            }
            chooseMove(player, roll).execute(events);
            recordIfFinished(player);
            rollAgain = roll == SIX && !player.hasWon();
        }
    }

    private void playRound(List<Player> roundOrder) {
        for (Player player : roundOrder) {
            if (!player.hasWon() && !isOver()) {
                playTurn(player);
            }
        }
    }

    private Move chooseMove(Player player, int roll) {
        List<PieceMove> options = moveGenerator.legalMoves(player, roll);
        if (options.isEmpty()) {
            return new NoMove(player);
        }
        return strategies.get(player.getColour()).chooseMove(options, player);
    }

    private void recordIfFinished(Player player) {
        if (player.hasWon() && !finishingOrder.contains(player)) {
            finishingOrder.add(player);
            events.onPlayerFinished(player, finishingOrder.size());
        }
    }

    private boolean isOver() {
        return finishingOrder.size() >= players.size() - 1;
    }

    private List<PlayerStatus> currentStatuses() {
        List<PlayerStatus> statuses = new ArrayList<>();
        for (Player player : players) {
            statuses.add(PlayerStatus.of(player));
        }
        return statuses;
    }
}