package com.worldcup.scoreboard;

import java.time.Instant;
import java.util.Objects;

public final class Match implements Comparable<Match> {

    private final String homeTeam;
    private final String awayTeam;
    private final int homeScore;
    private final int awayScore;
    private final Instant startTime;

    private Match(String homeTeam, String awayTeam,
                  int homeScore, int awayScore, Instant startTime) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.startTime = startTime;
    }

    public static Match start(String homeTeam, String awayTeam) {
        return new Match(homeTeam, awayTeam, 0, 0, Instant.now());
    }

    public Match withScore(int homeScore, int awayScore) {
        return new Match(this.homeTeam, this.awayTeam, homeScore, awayScore, this.startTime);
    }

    public String homeTeam() { return homeTeam; }
    public String awayTeam() { return awayTeam; }
    public int homeScore() { return homeScore; }
    public int awayScore() { return awayScore; }
    public int totalScore() { return homeScore + awayScore; }
    public Instant startTime() { return startTime; }

    @Override
    public int compareTo(Match other) {
        int scoreCompare = Integer.compare(other.totalScore(), this.totalScore());
        return scoreCompare != 0 ? scoreCompare : other.startTime.compareTo(this.startTime);
    }

    @Override
    public String toString() {
        return "%s %d – %s %d".formatted(homeTeam, homeScore, awayTeam, awayScore);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Match match)) return false;
        return Objects.equals(homeTeam, match.homeTeam) &&
               Objects.equals(awayTeam, match.awayTeam) &&
               homeScore == match.homeScore &&
               awayScore == match.awayScore &&
               Objects.equals(startTime, match.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(homeTeam, awayTeam, homeScore, awayScore, startTime);
    }
}
