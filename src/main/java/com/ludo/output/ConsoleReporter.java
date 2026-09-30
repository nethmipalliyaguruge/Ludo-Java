package com.ludo.output;

import com.ludo.event.GameEventListener;
import com.ludo.game.PieceStatus;
import com.ludo.game.PlayerStatus;
import com.ludo.model.*;

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
    public void onMysteryCellSpawned(int cell, int rounds) {
        out.println("A mystery cell has spawned in location " + cell
                + " and will be at this location for the next " + inWords(rounds) + " rounds.");
    }

    @Override
    public void onMysteryCellCountdown(int cell, int roundsLeft) {
        out.println("The mystery cell is at " + cell + " and will be at that location for the next "
                + roundsLeft + " rounds.");
    }

    @Override
    public void onTeleported(Piece piece, TeleportDestination destination) {
        String colour = name(piece.getColour());
        out.println(colour + " player lands on a mystery cell and is teleported to "
                + piece.describeLocation() + ".");
        out.println(colour + " piece " + piece.getName() + " teleported to " + destination.getLabel() + ".");
    }

    @Override
    public void onEnergised(Piece piece) {
        out.println(name(piece.getColour()) + " piece " + piece.getName()
                + " feels energized, and movement speed doubles.");
    }

    @Override
    public void onSick(Piece piece) {
        out.println(name(piece.getColour()) + " piece " + piece.getName()
                + " feels sick, and movement speed halves.");
    }

    @Override
    public void onBriefing(Piece piece) {
        out.println(name(piece.getColour()) + " piece " + piece.getName()
                + " attends briefing and cannot move for four rounds.");
    }

    @Override
    public void onSentToBaseFromBriefing(Piece piece) {
        out.println(name(piece.getColour()) + " piece " + piece.getName()
                + " is movement-restricted and has rolled three consecutively. Teleporting piece "
                + piece.getName() + " to base.");
    }

    @Override
    public void onTurnedCounterClockwise(Piece piece) {
        out.println("The " + name(piece.getColour()) + " piece " + piece.getName()
                + ", which was moving clockwise, has changed to moving counterclockwise.");
    }

    @Override
    public void onSentFromGammaToBeta(Piece piece) {
        out.println("The " + name(piece.getColour()) + " piece " + piece.getName()
                + " is moving in a counterclockwise direction. Teleporting to Beta from Gamma.");
    }

    @Override
    public void onBlockMoved(Player owner, List<Piece> block, String from, String to, int steps,
                             Direction direction) {
        List<String> names = new ArrayList<>();
        for (Piece piece : block) {
            names.add(piece.getName());
        }
        out.println(name(owner.getColour()) + " moves the block of " + String.join(" and ", names)
                + " from location " + from + " to " + to + " by " + steps + " units in "
                + describe(direction) + " direction.");
    }

    @Override
    public void onBlockadeBroken(Player player) {
        out.println(name(player.getColour()) + " rolled a six three times in a row while holding a blockade."
                + " The blockade is broken by moving all pieces but one in their original direction"
                + " by six units in total.");
    }

    @Override
    public void onStalemate(int round) {
        out.println("Stalemate after round " + round + ": no piece has moved for many rounds because blocks"
                + " are stopping every remaining piece. Remaining players are ranked by distance to home.");
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

    @Override
    public void onPieceBlocked(Piece piece, String from, String to, Piece blocker) {
        out.println(name(piece.getColour()) + " piece " + piece.getName() + " is blocked from moving from "
                + from + " to " + to + " by " + name(blocker.getColour()) + " piece " + blocker.getName() + ".");
    }

    @Override
    public void onMovedUpToBlock(Piece piece) {
        out.println(name(piece.getColour()) + " does not have other pieces in the board to move instead of the"
                + " blocked piece. Moved the piece to square " + piece.describeLocation()
                + " which is the cell before the block.");
    }

    @Override
    public void onBlockedThrowIgnored(Piece piece) {
        out.println(name(piece.getColour()) + " does not have other pieces in the board to move instead of the"
                + " blocked piece. Ignoring the throw and moving on to the next player.");
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

    private static String inWords(int number) {
        return number == MysteryCell.ROUNDS_AT_ONE_CELL ? "four" : String.valueOf(number);
    }
}