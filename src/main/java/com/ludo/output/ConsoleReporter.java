package com.ludo.output;

import com.ludo.event.GameEventListener;
import com.ludo.game.PieceStatus;
import com.ludo.game.PlayerStatus;
import com.ludo.model.Colour;
import com.ludo.model.Direction;
import com.ludo.model.Piece;
import com.ludo.model.Player;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class ConsoleReporter implements GameEventListener {
    private static final String DIVIDER = "============================";
    private static final int PIECES_PER_PLAYER = 4;

    private final PrintStream out;

    public ConsoleReporter(PrintStream out) {
        this.out = out;
    }

    @Override
    public void onGameStart(List<Player> players) {
        for (Player player : players) {
            List<String> names = new ArrayList<>();
            for (Piece piece : player.getPieces()) {
                names.add(piece.getName());
            }
            out.println("The " + name(player.getColour()).toLowerCase() + " player has four (04) pieces named "
                    + joinWithAnd(names) + ".");
        }
    }

    @Override
    public void onFirstPlayerRoll(Player player, int roll) {
        out.println(name(player.getColour()) + " rolls " + roll);
    }

    @Override
    public void onRoundOrderDecided(List<Player> order) {
        List<String> names = new ArrayList<>();
        for (Player player : order) {
            names.add(name(player.getColour()));
        }
        out.println(names.getFirst() + " player has the highest roll and will begin the game.");
        out.println("The order of a single round is " + joinWithAnd(names) + ".");
    }

    @Override
    public void onRoll(Player player, int roll) {
        out.println(name(player.getColour()) + " player rolled " + roll + ".");
    }

    @Override
    public void onPieceEnteredBoard(Player owner, Piece piece) {
        out.println(name(owner.getColour()) + " player moves piece " + piece.getName()
                + " to the starting point.");
        printPieceCounts(owner);
    }

    @Override
    public void onPieceMoved(Piece piece, String from, String to, int steps) {
        out.println(name(piece.getColour()) + " moves piece " + piece.getName() + " from location "
                + from + " to " + to + " by " + steps + " units in "
                + describe(piece.getDirection()) + " direction.");
    }

    @Override
    public void onCapture(Player attacker, Piece attackingPiece, Piece capturedPiece) {
        out.println(name(attacker.getColour()) + " piece " + attackingPiece.getName() + " lands on square "
                + attackingPiece.describeLocation() + ", captures " + name(capturedPiece.getColour())
                + " piece " + capturedPiece.getName() + ", and returns it to the base.");
        printPieceCounts(attacker);
    }

    @Override
    public void onNoMoveAvailable(Player player) {
        out.println(name(player.getColour()) + " has no piece that can move with this roll."
                + " Ignoring the throw and moving on to the next player.");
    }

    @Override
    public void onThirdSixIgnored(Player player) {
        out.println(name(player.getColour()) + " rolled a six three times in a row."
                + " The roll is ignored and the dice passes to the next player.");
    }

    @Override
    public void onPlayerFinished(Player player, int place) {
        if (place == 1) {
            out.println(name(player.getColour()) + " player wins!!!");
        } else {
            out.println(name(player.getColour()) + " player finishes in place " + place + ".");
        }
    }

    @Override
    public void onGameOver(List<Player> ranking) {
        out.println();
        out.println("======== FINAL RESULT ========");
        out.println("--" + ranking.getFirst().getColour() + " IS THE WINNER--");
        for (int i = 0; i < ranking.size(); i++) {
            out.println("Place " + (i + 1) + ": " + name(ranking.get(i).getColour()));
        }
    }

    @Override
    public void onRoundEnd(int round, List<PlayerStatus> statuses) {
        out.println();
        out.println("End of round " + round);
        for (PlayerStatus status : statuses) {
            printStatus(status);
        }
        out.println();
    }

    private void printStatus(PlayerStatus status) {
        String colour = name(status.colour());
        out.println(colour + " player now has " + status.piecesOnBoard() + "/" + PIECES_PER_PLAYER
                + " pieces on the board and " + status.piecesInBase() + "/" + PIECES_PER_PLAYER
                + " pieces on the base.");
        out.println(DIVIDER);
        out.println("Location of pieces " + colour);
        out.println(DIVIDER);
        for (PieceStatus piece : status.pieces()) {
            out.println("Piece " + piece.name() + " -> " + piece.location());
        }
    }

    private void printPieceCounts(Player player) {
        out.println(name(player.getColour()) + " player now has " + player.countPiecesOnBoard() + "/"
                + PIECES_PER_PLAYER + " pieces on the board and " + player.countPiecesInBase() + "/"
                + PIECES_PER_PLAYER + " pieces on the base.");
    }

    private static String name(Colour colour) {
        String lower = colour.name().toLowerCase();
        return Character.toUpperCase(lower.charAt(0)) + lower.substring(1);
    }

    private static String describe(Direction direction) {
        return direction == Direction.CLOCKWISE ? "clockwise" : "counter-clockwise";
    }

    private static String joinWithAnd(List<String> items) {
        String allButLast = String.join(", ", items.subList(0, items.size() - 1));
        return allButLast + ", and " + items.getLast();
    }
}