package com.ludo.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PieceConditionTest {

    @Test
    void normalPieceMovesTheValueOfTheRoll() {
        assertEquals(5, PieceCondition.NORMAL.stepsFor(5));
    }

    @Test
    void energisedPieceMovesDoubleTheRoll() {
        assertEquals(10, new EnergisedCondition(4).stepsFor(5));
    }

    @Test
    void sickPieceMovesHalfTheRollRoundedDown() {
        assertEquals(2, new SickCondition(4).stepsFor(5));
    }

    @Test
    void pieceInBriefingCannotMove() {
        assertEquals(0, new BriefingCondition(4, 0).stepsFor(6));
    }

    @Test
    void effectLastsForFourFullRoundsAfterTheTeleportRound() {
        PieceCondition condition = new EnergisedCondition(PieceCondition.ROUNDS_OF_EFFECT);

        for (int roundEnd = 0; roundEnd < 4; roundEnd++) {
            condition = condition.afterRound();
            assertInstanceOf(EnergisedCondition.class, condition);
        }
        condition = condition.afterRound();

        assertSame(PieceCondition.NORMAL, condition);
    }

    @Test
    void threeThreesInARowDuringBriefingSendsThePieceToBase() {
        PieceCondition briefing = new BriefingCondition(4, 0);

        PieceCondition afterThreeThrees = briefing.afterOwnerRoll(3).afterOwnerRoll(3).afterOwnerRoll(3);

        assertTrue(afterThreeThrees.mustReturnToBase());
    }

    @Test
    void anotherRollInBetweenResetsTheCountOfThrees() {
        PieceCondition briefing = new BriefingCondition(4, 0);

        PieceCondition condition = briefing.afterOwnerRoll(3).afterOwnerRoll(3).afterOwnerRoll(5).afterOwnerRoll(3);

        assertFalse(condition.mustReturnToBase());
    }
}