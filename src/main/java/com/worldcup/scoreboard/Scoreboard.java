package com.worldcup.scoreboard;

import com.worldcup.scoreboard.exception.ScoreboardException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Scoreboard {

    private final Map<String, Match> matches = new ConcurrentHashMap<>();

    public synchronized void startGame(String homeTeam, String awayTeam) {
        String key = keyOf(homeTeam, awayTeam);
        if (matches.containsKey(key)) {
            throw new ScoreboardException("Матч %s уже идёт".formatted(key));
        }
        matches.put(key, Match.start(homeTeam, awayTeam));
    }

    public synchronized void updateScore(String homeTeam, String awayTeam, int homeScore, int awayScore) {
        validateScore(homeScore, awayScore);
        String key = keyOf(homeTeam, awayTeam);
        Match ongoing = matches.get(key);
        if (ongoing == null) {
            throw new ScoreboardException("Матч %s не найден".formatted(key));
        }
        matches.put(key, ongoing.withScore(homeScore, awayScore));
    }

    public synchronized void finishGame(String homeTeam, String awayTeam) {
        String key = keyOf(homeTeam, awayTeam);
        if (matches.remove(key) == null) {
            throw new ScoreboardException("Матч %s не найден".formatted(key));
        }
    }

    public List<Match> getSummary() {
        return matches.values().stream().sorted().toList();
    }

    private static String keyOf(String home, String away) {
        return home + "-" + away;
    }

    private static void validateScore(int home, int away) {
        if (home < 0 || away < 0) {
            throw new ScoreboardException("Счёт не может быть отрицательным");
        }
    }
}
