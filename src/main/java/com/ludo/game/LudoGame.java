package com.ludo.game;

import com.ludo.event.GameEventListener;
import com.ludo.model.Board;
import com.ludo.model.Colour;
import com.ludo.model.Piece;
import com.ludo.model.Player;
import com.ludo.move.Move;
import com.ludo.move.MoveGenerator;
import com.ludo.move.NoMove;
import com.ludo.move.PieceMove;
import com.ludo.model.MysteryCell;
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
    private static final int ROUNDS_WITHOUT_PROGRESS_FOR_STALEMATE = 50;

    private final List<Player> players;
    private final Map<Colour, PlayerStrategy> strategies;
    private final Dice dice;
    private final GameEventListener events;
    private final Board board;
    private final MysteryCell mysteryCell;
    private final MoveGenerator moveGenerator;
    private final TurnManager turnManager;
    private final List<Player> finishingOrder = new ArrayList<>();

    public LudoGame(List<Player> players, Map<Colour, PlayerStrategy> strategies,
                    Dice dice, CoinToss coin, MysteryCell mysteryCell, GameEventListener events) {
        this.players = List.copyOf(players);
        this.strategies = Map.copyOf(strategies);
        this.dice = dice;
        this.events = events;
        this.mysteryCell = mysteryCell;
        this.board = new Board(this.players);
        this.moveGenerator = new MoveGenerator(board, coin);
        this.turnManager = new TurnManager(this.players);
    }

    public List<Player> play() {
        events.onGameStart(players);
        List<Player> roundOrder = turnManager.decideRoundOrder(dice, events);
        int round = 0;
        int roundsWithoutProgress = 0;
        while (!isOver() && round < MAX_ROUNDS
                && roundsWithoutProgress < ROUNDS_WITHOUT_PROGRESS_FOR_STALEMATE) {
            round++;
            List<PlayerStatus> before = currentStatuses();
            playRound(roundOrder);
            endRoundForEveryPlayer();
            List<PlayerStatus> after = currentStatuses();
            // Nothing changed this round, so count it towards a stalemate
            roundsWithoutProgress = after.equals(before) ? roundsWithoutProgress + 1 : 0;
            events.onRoundEnd(round, after);
            // The mystery cell timer only moves on at the end of a full round
            mysteryCell.endRound(board, events);
        }
        if (roundsWithoutProgress >= ROUNDS_WITHOUT_PROGRESS_FOR_STALEMATE) {
            events.onStalemate(round);
        } else if (!isOver()) {
            events.onRoundLimitReached(round);
        }
        events.onGameOver(finalRanking());
        return List.copyOf(finishingOrder);
    }

    void playTurn(Player player) {
        int sixesInARow = 0;
        boolean rollAgain = true;
        while (rollAgain) {
            int roll = dice.roll();
            events.onRoll(player, roll);
            // Every roll counts towards the three 3s rule for pieces in a briefing (T-13)
            applyBriefingRule(player, roll);
            sixesInARow = (roll == SIX) ? sixesInARow + 1 : 0;
            // Rule 4 and T-6: the third six in a row is ignored, or breaks a blockade
            if (sixesInARow == SIXES_BEFORE_ROLL_IS_IGNORED) {
                handleThirdSix(player);
                return;
            }
            Move move = chooseMove(player, roll);
            // Checked before the move runs, because the captured piece is gone afterwards
            boolean earnedBonusRoll = move.capturesOpponent();
            perform(player, move);
            // Another roll after a six (Rule 4) or a capture (T-2), unless the throw was ignored (Rule 7)
            rollAgain = (roll == SIX || earnedBonusRoll) && !move.ignoresThrow() && !player.hasWon();
        }
    }

    private void playRound(List<Player> roundOrder) {
        for (Player player : roundOrder) {
            if (!player.hasWon() && !isOver()) {
                playTurn(player);
            }
        }
    }

    private void endRoundForEveryPlayer() {
        for (Player player : players) {
            player.endRound();
        }
    }

    private void applyBriefingRule(Player player, int roll) {
        for (Piece piece : player.getPieces()) {
            piece.noteOwnerRoll(roll);
            if (piece.mustReturnToBase()) {
                piece.returnToBase();
                events.onSentToBaseFromBriefing(piece);
            }
        }
    }

    private void handleThirdSix(Player player) {
        List<PieceMove> breakingMoves = moveGenerator.blockadeBreakingMoves(player);
        if (breakingMoves.isEmpty()) {
            events.onThirdSixIgnored(player);
            return;
        }
        events.onBlockadeBroken(player);
        for (PieceMove move : breakingMoves) {
            perform(player, move);
        }
    }

    private Move chooseMove(Player player, int roll) {
        List<PieceMove> options = moveGenerator.legalMoves(player, roll);
        if (options.isEmpty()) {
            return new NoMove(player);
        }
        return strategies.get(player.getColour()).chooseMove(options, player);
    }

    private void perform(Player player, Move move) {
        move.execute(events);
        for (Piece piece : move.movedPieces()) {
            mysteryCell.teleportIfLandedOn(piece, events);
        }
        recordIfFinished(player);
    }

    private void recordIfFinished(Player player) {
        if (player.hasWon() && !finishingOrder.contains(player)) {
            finishingOrder.add(player);
            events.onPlayerFinished(player, finishingOrder.size());
        }
    }

    private boolean isOver() {
        // The game ends when only one player is still playing
        return finishingOrder.size() >= players.size() - 1;
    }

    private List<PlayerStatus> currentStatuses() {
        List<PlayerStatus> statuses = new ArrayList<>();
        for (Player player : players) {
            statuses.add(PlayerStatus.of(player));
        }
        return statuses;
    }

    private List<Player> finalRanking() {
        List<Player> ranking = new ArrayList<>(finishingOrder);
        List<Player> unfinished = new ArrayList<>();
        for (Player player : players) {
            if (!finishingOrder.contains(player)) {
                unfinished.add(player);
            }
        }
        while (!unfinished.isEmpty()) {
            Player closest = closestToHome(unfinished);
            ranking.add(closest);
            unfinished.remove(closest);
        }
        return ranking;
    }

    private Player closestToHome(List<Player> candidates) {
        Player closest = candidates.getFirst();
        for (Player player : candidates) {
            if (player.totalStepsToHome() < closest.totalStepsToHome()) {
                closest = player;
            }
        }
        return closest;
    }
}
