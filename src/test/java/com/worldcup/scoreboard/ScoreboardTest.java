package com.worldcup.scoreboard;

import com.worldcup.scoreboard.exception.ScoreboardException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ScoreboardTest {

    private Scoreboard scoreboard;

    @BeforeEach
    void setUp() {
        scoreboard = new Scoreboard();
    }

    @Test
    void startGame_addsMatchWithZeroScore() {
        scoreboard.startGame("Mexico", "Canada");

        List<Match> summary = scoreboard.getSummary();
        assertEquals(1, summary.size());
        Match match = summary.get(0);

        assertEquals("Mexico", match.homeTeam());
        assertEquals("Canada", match.awayTeam());
        assertEquals(0, match.homeScore());
        assertEquals(0, match.awayScore());
    }

    @Test
    void updateScore_changesExistingMatch() {
        scoreboard.startGame("Spain", "Brazil");
        scoreboard.updateScore("Spain", "Brazil", 10, 2);

        Match updated = scoreboard.getSummary().get(0);
        assertEquals(12, updated.totalScore());
    }

    @Test
    void finishGame_removesMatch() {
        scoreboard.startGame("Germany", "France");
        scoreboard.finishGame("Germany", "France");

        assertTrue(scoreboard.getSummary().isEmpty());
    }

    @Test
    void getSummary_ordersByTotalScoreThenRecency() throws InterruptedException {
        scoreboard.startGame("Mexico", "Canada");
        scoreboard.updateScore("Mexico", "Canada", 0, 5);

        scoreboard.startGame("Spain", "Brazil");
        scoreboard.updateScore("Spain", "Brazil", 10, 2);

        scoreboard.startGame("Germany", "France");
        scoreboard.updateScore("Germany", "France", 2, 2);

        Thread.sleep(10);

        scoreboard.startGame("Uruguay", "Italy");
        scoreboard.updateScore("Uruguay", "Italy", 6, 6);

        scoreboard.startGame("Argentina", "Australia");
        scoreboard.updateScore("Argentina", "Australia", 3, 1);

        List<Match> summary = scoreboard.getSummary();
        assertEquals("Uruguay 6 – Italy 6", summary.get(0).toString());
    }

    @Test
    void startGame_duplicateThrows() {
        scoreboard.startGame("A", "B");
        assertThrows(ScoreboardException.class, () -> scoreboard.startGame("A", "B"));
    }

    @Test
    void updateScore_nonExistingThrows() {
        assertThrows(ScoreboardException.class, () -> scoreboard.updateScore("A", "B", 1, 1));
    }

    @Test
    void finishGame_nonExistingThrows() {
        assertThrows(ScoreboardException.class, () -> scoreboard.finishGame("A", "B"));
    }

    @Test
    void updateScore_negativeNotAllowed() {
        scoreboard.startGame("A", "B");
        assertThrows(ScoreboardException.class, () -> scoreboard.updateScore("A", "B", -1, 2));
    }
}
