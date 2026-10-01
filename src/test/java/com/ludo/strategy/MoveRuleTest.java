package com.ludo.strategy;

import com.ludo.model.Board;
import com.ludo.model.Colour;
import com.ludo.model.Direction;
import com.ludo.model.Piece;
import com.ludo.model.Player;
import com.ludo.move.EnterBoardMove;
import com.ludo.move.PieceMove;
import com.ludo.move.StepMove;
import com.ludo.random.FixedCoinToss;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;

class MoveRuleTest {
    private List<Piece> pieces;
    private PieceMove nearHome;
    private PieceMove farFromHome;
    private PieceMove entry;

    @BeforeEach
    void setUp() {
        Player red = new Player(Colour.RED);
        Board board = new Board(List.of(red));
        pieces = red.getPieces();
        pieces.get(0).moveToStart(20, Direction.CLOCKWISE);
        pieces.get(1).moveToStart(30, Direction.CLOCKWISE);
        nearHome = new StepMove(red, pieces.get(0), board, 1);
        farFromHome = new StepMove(red, pieces.get(1), board, 1);
        entry = new EnterBoardMove(red, pieces.get(2), board, new FixedCoinToss(Direction.CLOCKWISE));
    }

    @Test
    void ruleThatCanDecideChoosesTheMove() {
        MoveRule rules = new EntryRule(new ClosestToHomeRule());

        assertSame(entry, rules.choose(List.of(nearHome, entry)));
    }

    @Test
    void ruleThatCannotDecidePassesTheOptionsToTheNextRule() {
        MoveRule rules = new EntryRule(new ClosestToHomeRule());

        assertSame(nearHome, rules.choose(List.of(farFromHome, nearHome)));
    }

    @Test
    void withoutBlockRuleSkipsAMoveThatWouldCreateABlock() {
        pieces.get(3).moveToStart(21, Direction.CLOCKWISE);
        MoveRule rules = new ClosestToHomeWithoutBlockRule(new ClosestToHomeRule());

        assertSame(farFromHome, rules.choose(List.of(nearHome, farFromHome)));
    }

    @Test
    void withoutBlockRulePassesOnWhenEveryMoveCreatesABlock() {
        pieces.get(3).moveToStart(21, Direction.CLOCKWISE);
        MoveRule rules = new ClosestToHomeWithoutBlockRule(new ClosestToHomeRule());

        assertSame(nearHome, rules.choose(List.of(nearHome)));
    }

    @Test
    void keepingBlockRuleSkipsAMoveThatWouldBreakABlock() {
        pieces.get(3).moveToStart(20, Direction.CLOCKWISE);
        MoveRule rules = new ClosestToHomeKeepingBlockRule(new ClosestToHomeRule());

        assertSame(farFromHome, rules.choose(List.of(nearHome, farFromHome)));
    }

    @Test
    void keepingBlockRulePassesOnWhenEveryMoveBreaksABlock() {
        pieces.get(3).moveToStart(20, Direction.CLOCKWISE);
        MoveRule rules = new ClosestToHomeKeepingBlockRule(new ClosestToHomeRule());

        assertSame(nearHome, rules.choose(List.of(nearHome)));
    }
}
