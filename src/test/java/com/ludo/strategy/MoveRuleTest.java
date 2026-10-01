package com.ludo.strategy;

import com.ludo.model.*;
import com.ludo.move.EnterBoardMove;
import com.ludo.move.PieceMove;
import com.ludo.move.StepMove;
import com.ludo.random.FixedCoinToss;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;

class MoveRuleTest {
    private Player red;
    private Board board;
    private PieceMove nearHome;
    private PieceMove farFromHome;
    private PieceMove entry;

    @BeforeEach
    void setUp() {
        red = new Player(Colour.RED);
        board = new Board(List.of(red));
        List<Piece> pieces = red.getPieces();
        pieces.get(0).moveToStart(20, Direction.CLOCKWISE);
        pieces.get(1).moveToStart(30, Direction.CLOCKWISE);
        nearHome = new StepMove(red, pieces.get(0), board, 1);
        farFromHome = new StepMove(red, pieces.get(1), board, 1);
        entry = new EnterBoardMove(red, pieces.get(2), board, new FixedCoinToss(Direction.CLOCKWISE));
    }

    @Test
    void ruleThatCanDecideChoosesTheMove() {
        MoveRule rules = new EntryRule(ClosestToHomeRule.anyMove());

        assertSame(entry, rules.choose(List.of(nearHome, entry)));
    }

    @Test
    void ruleThatCannotDecidePassesTheOptionsToTheNextRule() {
        MoveRule rules = new EntryRule(options -> farFromHome);

        assertSame(farFromHome, rules.choose(List.of(nearHome, farFromHome)));
    }

    @Test
    void lastRuleOnlyLooksAtPreferredMovesWhenThereAreAny() {
        MoveRule rules = ClosestToHomeRule.preferring(move -> move == farFromHome);

        assertSame(farFromHome, rules.choose(List.of(nearHome, farFromHome)));
    }

    @Test
    void lastRuleFallsBackToAllMovesWhenNoneArePreferred() {
        MoveRule rules = ClosestToHomeRule.preferring(move -> false);

        assertSame(nearHome, rules.choose(List.of(farFromHome, nearHome)));
    }
}